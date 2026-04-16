<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="96px">
      <el-form-item label="课程名称" prop="courseId">
        <el-select v-model="queryParams.courseId" placeholder="请选择课程名称" clearable filterable style="width: 220px">
          <el-option v-for="item in educationOptions.courseOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="授课教师" prop="teacherUserId">
        <el-select v-model="queryParams.teacherUserId" placeholder="请选择授课教师" clearable filterable style="width: 220px">
          <el-option v-for="item in educationOptions.teacherUserOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 220px">
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('courseTeacher', 'add')" v-hasPermi="['education:courseTeacher:list']" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('courseTeacher', 'edit')" v-hasPermi="['education:courseTeacher:list']" type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('courseTeacher', 'remove')" v-hasPermi="['education:courseTeacher:list']" type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('courseTeacher', 'export')" v-hasPermi="['education:courseTeacher:list']" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="courseTeacherList" @selection-change="handleSelectionChange">
      <el-table-column v-if="canEducationAction('courseTeacher', 'edit') || canEducationAction('courseTeacher', 'remove')" type="selection" width="55" align="center" />
      <el-table-column label="授课关联ID" align="center" prop="courseTeacherId" :show-overflow-tooltip="true" />
      <el-table-column label="课程名称" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('courseOptions', scope.row.courseId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="授课教师" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('teacherUserOptions', scope.row.teacherUserId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="教师角色" align="center" prop="teacherRole">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.edu_teacher_role" :value="scope.row.teacherRole" />
        </template>
      </el-table-column>
      <el-table-column label="是否负责人" align="center" prop="isOwner">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.isOwner" />
        </template>
      </el-table-column>
      <el-table-column label="排序号" align="center" prop="orderNum" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button v-if="canEducationAction('courseTeacher', 'edit')" v-hasPermi="['education:courseTeacher:list']" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button v-if="canEducationAction('courseTeacher', 'remove')" v-hasPermi="['education:courseTeacher:list']" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="课程名称" prop="courseId">
          <el-select v-model="form.courseId" placeholder="输入课程名称搜索" filterable remote :remote-method="searchCourse" :loading="courseSearchLoading" style="width: 100%">
            <el-option v-for="item in courseSearchResults" :key="item.courseId" :label="item.courseName" :value="item.courseId" />
          </el-select>
        </el-form-item>
        <el-form-item label="授课教师" prop="teacherUserId">
          <el-select v-model="form.teacherUserId" placeholder="输入教师姓名或工号搜索" filterable remote :remote-method="searchTeacher" :loading="teacherSearchLoading" style="width: 100%">
            <el-option v-for="item in teacherSearchResults" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="教师角色" prop="teacherRole">
          <el-select v-model="form.teacherRole" placeholder="请选择教师角色" clearable style="width: 100%">
            <el-option v-for="dict in dict.type.edu_teacher_role" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否负责人" prop="isOwner">
          <el-select v-model="form.isOwner" placeholder="请选择是否负责人" clearable style="width: 100%">
            <el-option v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序号" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入排序号" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" clearable style="width: 100%">
            <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCourseTeacher, getCourseTeacher, delCourseTeacher, addCourseTeacher, updateCourseTeacher, searchCourse, searchTeacher } from '@/api/education/courseTeacher'
import educationOptions from '@/mixins/educationOptions'
import educationActionAuth from '@/mixins/educationActionAuth'

export default {
  name: 'CourseTeacher',
  mixins: [educationOptions, educationActionAuth],
  dicts: ['edu_teacher_role', 'sys_yes_no', 'sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      courseTeacherList: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseId: null,
        teacherUserId: null,
        status: null
      },
      form: {},
      // 远程搜索相关数据
      courseSearchLoading: false,
      courseSearchResults: [],
      teacherSearchLoading: false,
      teacherSearchResults: [],
      rules: {
        courseId: [
          { required: true, message: '请选择课程名称', trigger: 'change' }
        ],
        teacherUserId: [
          { required: true, message: '请选择授课教师', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.loadEducationOptions()
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listCourseTeacher(this.queryParams).then(response => {
        this.courseTeacherList = response.rows
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
        courseTeacherId: null,
        courseId: null,
        teacherUserId: null,
        teacherRole: '1',
        isOwner: '2',
        orderNum: 1,
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
      this.queryParams.courseId = null
      this.queryParams.teacherUserId = null
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.courseTeacherId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增授课安排'
      // 清空搜索结果
      this.courseSearchResults = []
      this.teacherSearchResults = []
    },
    // 搜索课程（远程搜索）
    searchCourse(query) {
      if (!query) {
        this.courseSearchResults = []
        return
      }
      this.courseSearchLoading = true
      searchCourse(query).then(response => {
        this.courseSearchResults = response.data || []
        this.courseSearchLoading = false
      }).catch(() => {
        this.courseSearchLoading = false
      })
    },
    // 搜索教师（远程搜索）
    searchTeacher(query) {
      if (!query) {
        this.teacherSearchResults = []
        return
      }
      this.teacherSearchLoading = true
      searchTeacher(query).then(response => {
        this.teacherSearchResults = response.data || []
        this.teacherSearchLoading = false
      }).catch(() => {
        this.teacherSearchLoading = false
      })
    },
    handleUpdate(row) {
      this.reset()
      const id = row.courseTeacherId || this.ids[0]
      getCourseTeacher(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改授课安排'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.form.courseTeacherId != null) {
          updateCourseTeacher(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addCourseTeacher(this.form).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.courseTeacherId || this.ids
      this.$modal.confirm('是否确认删除授课安排编号为"' + ids + '"的数据项？').then(() => {
        return delCourseTeacher(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('education/courseTeacher/export', {
        ...this.queryParams
      }, 'courseTeacher_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
