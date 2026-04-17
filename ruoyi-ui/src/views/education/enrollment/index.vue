<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="96px">
      <el-form-item label="课程名称" prop="courseId">
        <el-select v-model="queryParams.courseId" placeholder="请选择课程名称" clearable filterable style="width: 220px">
          <el-option v-for="item in educationOptions.courseOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="学生姓名" prop="studentUserId">
        <el-select v-model="queryParams.studentUserId" placeholder="请选择学生姓名" clearable filterable style="width: 220px">
          <el-option v-for="item in educationOptions.studentUserOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="选课状态" prop="enrollStatus">
        <el-select v-model="queryParams.enrollStatus" placeholder="请选择选课状态" clearable style="width: 220px">
          <el-option v-for="dict in dict.type.edu_enroll_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('enrollment', 'add')" v-hasPermi="['education:enrollment:list']" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button v-if="canEducationAction('enrollment', 'remove')" v-hasPermi="['education:enrollment:list']" type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('enrollment', 'export')" v-hasPermi="['education:enrollment:list']" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="enrollmentList" @selection-change="handleSelectionChange">
      <el-table-column v-if="canEducationAction('enrollment', 'edit') || canEducationAction('enrollment', 'remove')" type="selection" width="55" align="center" />
      <el-table-column label="选课记录ID" align="center" prop="enrollmentId" :show-overflow-tooltip="true" />
      <el-table-column label="课程名称" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('courseOptions', scope.row.courseId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属学期" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('termOptions', scope.row.termId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="学生姓名" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('studentUserOptions', scope.row.studentUserId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="来源类型" align="center" prop="sourceType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.edu_enroll_source_type" :value="scope.row.sourceType" />
        </template>
      </el-table-column>
      <el-table-column label="选课状态" align="center" prop="enrollStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.edu_enroll_status" :value="scope.row.enrollStatus" />
        </template>
      </el-table-column>
      <el-table-column label="课程完成进度" align="center" prop="progressPercent" :show-overflow-tooltip="true" />
      <el-table-column label="最近学习时间" align="center" prop="lastStudyTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastStudyTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button v-if="canEducationAction('enrollment', 'edit')" v-hasPermi="['education:enrollment:list']" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button v-if="canEducationAction('enrollment', 'remove')" v-hasPermi="['education:enrollment:list']" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="720px" top="8vh" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="课程名称" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择课程名称" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.courseOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属学期" prop="termId">
          <el-select v-model="form.termId" placeholder="请选择所属学期" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.termOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="学生姓名" prop="studentUserId">
          <el-select v-model="form.studentUserId" placeholder="请选择学生姓名" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.studentUserOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="来源类型" prop="sourceType">
          <el-select v-model="form.sourceType" placeholder="请选择来源类型" clearable style="width: 100%">
            <el-option v-for="dict in dict.type.edu_enroll_source_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="选课时间" prop="enrollTime">
          <el-date-picker v-model="form.enrollTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择选课时间" clearable style="width: 100%" />
        </el-form-item>
        <el-form-item label="退课时间" prop="dropTime">
          <el-date-picker v-model="form.dropTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择退课时间" clearable style="width: 100%" />
        </el-form-item>
        <el-form-item label="完成时间" prop="finishTime">
          <el-date-picker v-model="form.finishTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择完成时间" clearable style="width: 100%" />
        </el-form-item>
        <el-form-item label="选课状态" prop="enrollStatus">
          <el-select v-model="form.enrollStatus" placeholder="请选择选课状态" clearable style="width: 100%">
            <el-option v-for="dict in dict.type.edu_enroll_status" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程完成进度" prop="progressPercent">
          <el-input v-model="form.progressPercent" placeholder="请输入课程完成进度" />
        </el-form-item>
        <el-form-item label="应完成任务数" prop="requiredTaskCount">
          <el-input v-model="form.requiredTaskCount" placeholder="请输入应完成任务数" />
        </el-form-item>
        <el-form-item label="已完成任务数" prop="finishedTaskCount">
          <el-input v-model="form.finishedTaskCount" placeholder="请输入已完成任务数" />
        </el-form-item>
        <el-form-item label="最近学习时间" prop="lastStudyTime">
          <el-date-picker v-model="form.lastStudyTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择最近学习时间" clearable style="width: 100%" />
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
import { listEnrollment, getEnrollment, delEnrollment, addEnrollment, updateEnrollment } from '@/api/education/enrollment'
import educationOptions from '@/mixins/educationOptions'
import educationActionAuth from '@/mixins/educationActionAuth'

export default {
  name: 'Enrollment',
  mixins: [educationOptions, educationActionAuth],
  dicts: ['edu_enroll_source_type', 'edu_enroll_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      enrollmentList: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseId: null,
        studentUserId: null,
        enrollStatus: null
      },
      form: {},
      rules: {
        courseId: [
          { required: true, message: '请选择课程名称', trigger: 'change' }
        ],
        termId: [
          { required: true, message: '请选择所属学期', trigger: 'change' }
        ],
        studentUserId: [
          { required: true, message: '请选择学生姓名', trigger: 'change' }
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
      listEnrollment(this.queryParams).then(response => {
        this.enrollmentList = response.rows
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
        enrollmentId: null,
        courseId: null,
        termId: null,
        studentUserId: null,
        sourceType: '1',
        enrollTime: null,
        dropTime: null,
        finishTime: null,
        enrollStatus: '1',
        progressPercent: 0.00,
        requiredTaskCount: 0,
        finishedTaskCount: 0,
        lastStudyTime: null,
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
      this.queryParams.studentUserId = null
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.enrollmentId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增选课管理'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.enrollmentId || this.ids[0]
      getEnrollment(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改选课管理'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.form.enrollmentId != null) {
          updateEnrollment(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addEnrollment(this.form).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.enrollmentId || this.ids
      this.$modal.confirm('是否确认删除选课管理编号为"' + ids + '"的数据项？').then(() => {
        return delEnrollment(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('education/enrollment/export', {
        ...this.queryParams
      }, 'enrollment_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
