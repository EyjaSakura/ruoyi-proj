<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="96px">
      <el-form-item label="作业标题" prop="assignmentId">
        <el-select v-model="queryParams.assignmentId" placeholder="请选择作业标题" clearable filterable style="width: 220px">
          <el-option v-for="item in educationOptions.assignmentOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="学生姓名" prop="studentUserId">
        <el-select v-model="queryParams.studentUserId" placeholder="请选择学生姓名" clearable filterable style="width: 220px">
          <el-option v-for="item in educationOptions.studentUserOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="批改状态" prop="reviewStatus">
        <el-select v-model="queryParams.reviewStatus" placeholder="请选择批改状态" clearable style="width: 220px">
          <el-option v-for="dict in dict.type.edu_review_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('submission', 'add')" v-hasPermi="['education:submission:list']" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button v-if="canEducationAction('submission', 'remove')" v-hasPermi="['education:submission:list']" type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('submission', 'export')" v-hasPermi="['education:submission:list']" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="submissionList" @selection-change="handleSelectionChange">
      <el-table-column v-if="canEducationAction('submission', 'edit') || canEducationAction('submission', 'remove')" type="selection" width="55" align="center" />
      <el-table-column type="index" label="#" width="60" align="center" />
      <el-table-column label="作业标题" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('assignmentOptions', scope.row.assignmentId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="课程名称" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('courseOptions', scope.row.courseId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="学生姓名" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('studentUserOptions', scope.row.studentUserId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="第几次提交" align="center" prop="submitRound" :show-overflow-tooltip="true" />
      <el-table-column label="批改状态" align="center" prop="reviewStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.edu_review_status" :value="scope.row.reviewStatus" />
        </template>
      </el-table-column>
      <el-table-column label="得分" align="center" prop="score" :show-overflow-tooltip="true" />
      <el-table-column label="提交时间" align="center" prop="submitTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.submitTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button v-if="canEducationAction('submission', 'edit')" v-hasPermi="['education:submission:list']" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button v-if="canEducationAction('submission', 'remove')" v-hasPermi="['education:submission:list']" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="作业标题" prop="assignmentId">
          <el-select v-model="form.assignmentId" placeholder="请选择作业标题" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.assignmentOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程名称" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择课程名称" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.courseOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="学生姓名" prop="studentUserId">
          <el-select v-model="form.studentUserId" placeholder="请选择学生姓名" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.studentUserOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="第几次提交" prop="submitRound">
          <el-input v-model="form.submitRound" placeholder="请输入第几次提交" />
        </el-form-item>
        <el-form-item label="提交说明" prop="submitRemark">
          <el-input v-model="form.submitRemark" type="textarea" :rows="3" placeholder="请输入提交说明" />
        </el-form-item>
        <el-form-item label="提交时间" prop="submitTime">
          <el-date-picker v-model="form.submitTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择提交时间" clearable style="width: 100%" />
        </el-form-item>
        <el-form-item label="是否逾期" prop="lateFlag">
          <el-select v-model="form.lateFlag" placeholder="请选择是否逾期" clearable style="width: 100%">
            <el-option v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否当前最新提交" prop="isLatest">
          <el-select v-model="form.isLatest" placeholder="请选择是否当前最新提交" clearable style="width: 100%">
            <el-option v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="批改状态" prop="reviewStatus">
          <el-select v-model="form.reviewStatus" placeholder="请选择批改状态" clearable style="width: 100%">
            <el-option v-for="dict in dict.type.edu_review_status" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="得分" prop="score">
          <el-input v-model="form.score" placeholder="请输入得分" />
        </el-form-item>
        <el-form-item label="教师评语" prop="teacherComment">
          <el-input v-model="form.teacherComment" type="textarea" :rows="3" placeholder="请输入教师评语" />
        </el-form-item>
        <el-form-item label="批改教师" prop="reviewUserId">
          <el-select v-model="form.reviewUserId" placeholder="请选择批改教师" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.teacherUserOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="批改时间" prop="reviewTime">
          <el-date-picker v-model="form.reviewTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择批改时间" clearable style="width: 100%" />
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
import { listSubmission, getSubmission, delSubmission, addSubmission, updateSubmission } from '@/api/education/submission'
import educationOptions from '@/mixins/educationOptions'
import educationActionAuth from '@/mixins/educationActionAuth'

export default {
  name: 'Submission',
  mixins: [educationOptions, educationActionAuth],
  dicts: ['sys_yes_no', 'edu_review_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      submissionList: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        assignmentId: null,
        studentUserId: null,
        reviewStatus: null
      },
      form: {},
      rules: {
        assignmentId: [
          { required: true, message: '请选择作业标题', trigger: 'change' }
        ],
        courseId: [
          { required: true, message: '请选择课程名称', trigger: 'change' }
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
      listSubmission(this.queryParams).then(response => {
        this.submissionList = response.rows
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
        submissionId: null,
        assignmentId: null,
        courseId: null,
        studentUserId: null,
        submitRound: 1,
        submitRemark: '',
        submitTime: null,
        lateFlag: '0',
        isLatest: '1',
        reviewStatus: '0',
        score: null,
        teacherComment: '',
        reviewUserId: null,
        reviewTime: null,
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
      this.ids = selection.map(item => item.submissionId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增作业提交'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.submissionId || this.ids[0]
      getSubmission(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改作业提交'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.form.submissionId != null) {
          updateSubmission(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addSubmission(this.form).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.submissionId || this.ids
      this.$modal.confirm('是否确认删除作业提交编号为"' + ids + '"的数据项？').then(() => {
        return delSubmission(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('education/submission/export', {
        ...this.queryParams
      }, 'submission_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
