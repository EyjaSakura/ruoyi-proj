<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="96px">
      <el-form-item label="课程名称" prop="courseId">
        <el-select v-model="queryParams.courseId" placeholder="请选择课程名称" clearable filterable style="width: 220px">
          <el-option v-for="item in educationOptions.courseOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="作业标题" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入作业标题" clearable style="width: 220px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="发布状态" prop="publishStatus">
        <el-select v-model="queryParams.publishStatus" placeholder="请选择发布状态" clearable style="width: 220px">
          <el-option v-for="dict in dict.type.edu_assignment_publish_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('assignment', 'add')" v-hasPermi="['education:assignment:list']" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('assignment', 'edit')" v-hasPermi="['education:assignment:list']" type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('assignment', 'remove')" v-hasPermi="['education:assignment:list']" type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('assignment', 'export')" v-hasPermi="['education:assignment:list']" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="assignmentList" @selection-change="handleSelectionChange">
      <el-table-column v-if="canEducationAction('assignment', 'edit') || canEducationAction('assignment', 'remove')" type="selection" width="55" align="center" />
      <el-table-column label="作业ID" align="center" prop="assignmentId" :show-overflow-tooltip="true" />
      <el-table-column label="课程名称" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('courseOptions', scope.row.courseId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="作业标题" align="center" prop="title" :show-overflow-tooltip="true" />
      <el-table-column label="作业类型" align="center" prop="assignmentType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.edu_assignment_type" :value="scope.row.assignmentType" />
        </template>
      </el-table-column>
      <el-table-column label="总分" align="center" prop="totalScore" :show-overflow-tooltip="true" />
      <el-table-column label="最大提交次数，0表示不限" align="center" prop="submitLimit" :show-overflow-tooltip="true" />
      <el-table-column label="截止时间" align="center" prop="deadlineTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.deadlineTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发布状态" align="center" prop="publishStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.edu_assignment_publish_status" :value="scope.row.publishStatus" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button v-if="canEducationAction('assignment', 'edit')" v-hasPermi="['education:assignment:list']" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button v-if="canEducationAction('assignment', 'remove')" v-hasPermi="['education:assignment:list']" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="课程名称" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择课程名称" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.courseOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="作业标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入作业标题" />
        </el-form-item>
        <el-form-item label="作业类型" prop="assignmentType">
          <el-select v-model="form.assignmentType" placeholder="请选择作业类型" clearable style="width: 100%">
            <el-option v-for="dict in dict.type.edu_assignment_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="作业要求" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="3" placeholder="请输入作业要求" />
        </el-form-item>
        <el-form-item label="总分" prop="totalScore">
          <el-input v-model="form.totalScore" placeholder="请输入总分" />
        </el-form-item>
        <el-form-item label="最大提交次数，0表示不限" prop="submitLimit">
          <el-input v-model="form.submitLimit" placeholder="请输入最大提交次数，0表示不限" />
        </el-form-item>
        <el-form-item label="是否允许重交" prop="allowResubmit">
          <el-select v-model="form.allowResubmit" placeholder="请选择是否允许重交" clearable style="width: 100%">
            <el-option v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="截止时间" prop="deadlineTime">
          <el-date-picker v-model="form.deadlineTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择截止时间" clearable style="width: 100%" />
        </el-form-item>
        <el-form-item label="发布状态" prop="publishStatus">
          <el-select v-model="form.publishStatus" placeholder="请选择发布状态" clearable style="width: 100%">
            <el-option v-for="dict in dict.type.edu_assignment_publish_status" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="发布教师" prop="publishUserId">
          <el-select v-model="form.publishUserId" placeholder="请选择发布教师" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.teacherUserOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="发布时间" prop="publishTime">
          <el-date-picker v-model="form.publishTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择发布时间" clearable style="width: 100%" />
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
import { listAssignment, getAssignment, delAssignment, addAssignment, updateAssignment } from '@/api/education/assignment'
import educationOptions from '@/mixins/educationOptions'
import educationActionAuth from '@/mixins/educationActionAuth'

export default {
  name: 'Assignment',
  mixins: [educationOptions, educationActionAuth],
  dicts: ['edu_assignment_type', 'sys_yes_no', 'edu_assignment_publish_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      assignmentList: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseId: null,
        title: null,
        publishStatus: null
      },
      form: {},
      rules: {
        courseId: [
          { required: true, message: '请选择课程名称', trigger: 'change' }
        ],
        title: [
          { required: true, message: '请输入作业标题', trigger: 'blur' }
        ],
        publishUserId: [
          { required: true, message: '请选择发布教师', trigger: 'change' }
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
      listAssignment(this.queryParams).then(response => {
        this.assignmentList = response.rows
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
        assignmentId: null,
        courseId: null,
        title: null,
        assignmentType: '1',
        content: null,
        totalScore: 100.00,
        submitLimit: 0,
        allowResubmit: '1',
        deadlineTime: null,
        publishStatus: '0',
        publishUserId: null,
        publishTime: null,
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
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.assignmentId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增作业管理'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.assignmentId || this.ids[0]
      getAssignment(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改作业管理'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.form.assignmentId != null) {
          updateAssignment(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addAssignment(this.form).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.assignmentId || this.ids
      this.$modal.confirm('是否确认删除作业管理编号为"' + ids + '"的数据项？').then(() => {
        return delAssignment(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('education/assignment/export', {
        ...this.queryParams
      }, 'assignment_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
