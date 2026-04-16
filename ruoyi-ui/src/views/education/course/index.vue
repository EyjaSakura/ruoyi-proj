<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="96px">
      <el-form-item label="课程号" prop="courseCode">
        <el-input v-model="queryParams.courseCode" placeholder="请输入课程号" clearable style="width: 220px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="课序号" prop="classNo">
        <el-input v-model="queryParams.classNo" placeholder="如 01 02" clearable style="width: 120px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="课程名称" prop="courseName">
        <el-input v-model="queryParams.courseName" placeholder="请输入课程名称" clearable style="width: 220px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 220px">
          <el-option v-for="dict in dict.type.edu_course_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item v-if="isSystemAdmin" label="所属学院" prop="deptId">
        <treeselect v-model="queryParams.deptId" :options="educationOptions.deptOptions" placeholder="请选择所属学院" clearable style="width: 220px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-if="canCreateCourse" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canManageCourse" type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canManageCourse" type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('course', 'export')" v-hasPermi="['education:course:list']" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
      <el-table-column v-if="canManageCourse" type="selection" width="55" align="center" />
      <el-table-column label="课程号" align="center" prop="courseCode" :show-overflow-tooltip="true" />
      <el-table-column label="课序号" align="center" prop="classNo" width="80" />
      <el-table-column label="课程名称" align="center" prop="courseName" :show-overflow-tooltip="true" />
      <el-table-column label="所属学期" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('termOptions', scope.row.termId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开课院系" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('deptOptions', scope.row.deptId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="学分" align="center" prop="credit" :show-overflow-tooltip="true" />
      <el-table-column label="总课时" align="center" prop="totalHours" :show-overflow-tooltip="true" />
      <el-table-column label="当前已选人数" align="center" prop="selectedCount" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.edu_course_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column v-if="canManageCourse" label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属学期" prop="termId">
              <el-select v-model="form.termId" placeholder="请选择所属学期" clearable filterable style="width: 100%" @change="onTermChange">
                <el-option v-for="item in educationOptions.termOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开课院系" prop="deptId">
              <treeselect v-model="form.deptId" :options="educationOptions.deptOptions" placeholder="请选择开课院系" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="课程号" prop="courseCode">
              <el-input v-model="form.courseCode" placeholder="如10010001" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="课序号" prop="classNo">
              <el-input v-model="form.classNo" placeholder="如01、02（1-4位字母或数字）" maxlength="4" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="课程名称" prop="courseName">
              <el-input v-model="form.courseName" placeholder="请输入课程名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="课程封面" prop="courseCover">
              <image-upload v-model="form.courseCover" :class="'course-cover-upload'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="课程类型" prop="courseType">
              <el-select v-model="form.courseType" placeholder="请选择课程类型" clearable style="width: 100%">
                <el-option v-for="dict in dict.type.edu_course_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" clearable style="width: 100%">
                <el-option v-for="dict in dict.type.edu_course_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学分" prop="credit">
              <el-input-number v-model="form.credit" :min="0" :max="20" :precision="1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总课时" prop="totalHours">
              <el-input-number v-model="form.totalHours" :min="0" :max="999" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="容量上限" prop="capacity">
              <el-select v-model="form.capacity" placeholder="请选择容量上限" clearable style="width: 100%">
                <el-option v-for="opt in capacityOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="选课开始时间" prop="selectStartTime">
              <el-date-picker v-model="form.selectStartTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择学期后自动填充，可手动修改" clearable style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="选课结束时间" prop="selectEndTime">
              <el-date-picker v-model="form.selectEndTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择学期后自动填充，可手动修改" clearable style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="课程简介" prop="intro">
              <el-input v-model="form.intro" type="textarea" :rows="2" placeholder="请输入课程简介" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="课程完成规则" prop="completeRule">
              <el-input v-model="form.completeRule" type="textarea" :rows="2" placeholder="请输入课程完成规则说明" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCourse, getCourse, delCourse, addCourse, updateCourse } from '@/api/education/course'
import educationOptions from '@/mixins/educationOptions'
import educationActionAuth from '@/mixins/educationActionAuth'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'Course',
  components: { Treeselect },
  mixins: [educationOptions, educationActionAuth],
  dicts: ['edu_course_type', 'edu_course_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      courseList: [],
      title: '',
      open: false,
      /** 容量上限下拉选项 */
      capacityOptions: [
        { label: '10人', value: 10 },
        { label: '20人', value: 20 },
        { label: '30人', value: 30 },
        { label: '40人', value: 40 },
        { label: '50人', value: 50 },
        { label: '60人', value: 60 },
        { label: '不限', value: 0 }
      ],
      /** 学期选项原始数据（用于按学期ID查开学日期） */
      termListCache: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseCode: null,
        classNo: null,
        courseName: null,
        deptId: null,
        status: null
      },
      form: {},
      rules: {
        termId: [
          { required: true, message: '请选择所属学期', trigger: 'change' }
        ],
        deptId: [
          { required: true, message: '请选择开课院系', trigger: 'change' }
        ],
        courseCode: [
          { required: true, message: '请输入课程号', trigger: 'blur' }
        ],
        classNo: [
          { required: true, message: '请输入课序号', trigger: 'blur' },
          { pattern: /^[0-9A-Za-z]{1,4}$/, message: '课序号为1-4位字母或数字', trigger: 'blur' }
        ],
        courseName: [
          { required: true, message: '请输入课程名称', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    /** 是否为系统管理员（仅系统管理员可见跨学院查询） */
    isSystemAdmin() {
      return this.hasEducationRole('admin')
    },
    canCreateCourse() {
      return this.canEducationAction('course', 'add')
    },
    canManageCourse() {
      return this.canEducationAction('course', 'edit') || this.canEducationAction('course', 'remove')
    }
  },
  created() {
    this.loadEducationOptions()
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listCourse(this.queryParams).then(response => {
        this.courseList = response.rows
        this.total = response.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        courseId: null,
        termId: null,
        deptId: null,
        courseCode: null,
        classNo: '01',
        courseName: null,
        courseCover: '',
        courseType: '2',
        credit: 3.0,
        totalHours: 30,
        capacity: 30,
        selectStartTime: null,
        selectEndTime: null,
        intro: null,
        completeRule: '修满课程全部章节内容并完成相关作业及测验，达到规定学分要求后视为课程完成。',
        status: '0',
        remark: null
      }
      this.resetForm('form')
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.courseId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    handleAdd() {
      if (!this.canCreateCourse) {
        return
      }
      this.reset()
      const deptOptions = this.educationOptions.deptOptions || []
      if (deptOptions.length === 1 && (!deptOptions[0].children || deptOptions[0].children.length === 0)) {
        this.form.deptId = deptOptions[0].id
      }
      this.open = true
      this.title = '新增课程管理'
    },
    handleUpdate(row) {
      if (!this.canManageCourse) {
        return
      }
      this.reset()
      const id = row.courseId || this.ids[0]
      getCourse(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改课程管理'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.form.courseId != null) {
          updateCourse(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addCourse(this.form).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      if (!this.canManageCourse) {
        return
      }
      const ids = row.courseId || this.ids
      this.$modal.confirm('是否确认删除课程管理编号为"' + ids + '"的数据项？').then(() => {
        return delCourse(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      if (!this.canEducationAction('course', 'export')) {
        return
      }
      this.download('education/course/export', {
        ...this.queryParams
      }, 'course_' + new Date().getTime() + '.xlsx')
    },
    /** 切换学期时，自动填充选课时间（开学前15天～后15天） */
    onTermChange(termId) {
      this.form.selectStartTime = null
      this.form.selectEndTime = null
      if (!termId) return
      this.fillCourseSelectTimes(termId)
    },
    /** 根据学期ID从学期选项中取开学日期，自动填充选课时间 */
    fillCourseSelectTimes(termId) {
      const termOptions = this.educationOptions.termOptions || []
      const termItem = termOptions.find(t => String(t.value) === String(termId))
      if (!termItem || !termItem.startDate) return
      const startDateStr = termItem.startDate
      const startDate = new Date(startDateStr)
      const pad = n => String(n).padStart(2, '0')
      const fmt = d => d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate())
      const before = new Date(startDate)
      before.setDate(before.getDate() - 15)
      const after = new Date(startDate)
      after.setDate(after.getDate() + 15)
      this.form.selectStartTime = fmt(before) + ' 00:00:00'
      this.form.selectEndTime = fmt(after) + ' 23:59:59'
    }
  }
}
</script>
<style scoped lang="scss">
/* 课程封面上传框尺寸从185x185缩小到160x160 */
.course-cover-upload ::v-deep .el-upload--picture-card {
  width: 80px;
  height: 80px;
  line-height: 80px;
}
.course-cover-upload ::v-deep .el-upload-list__item {
  width: 80px;
  height: 80px;
}
</style>
