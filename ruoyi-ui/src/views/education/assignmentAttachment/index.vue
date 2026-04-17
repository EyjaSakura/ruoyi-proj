<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="96px">
      <el-form-item label="作业标题" prop="assignmentId">
        <el-select v-model="queryParams.assignmentId" placeholder="请选择作业标题" clearable filterable style="width: 220px">
          <el-option v-for="item in educationOptions.assignmentOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="文件原名称" prop="fileName">
        <el-input v-model="queryParams.fileName" placeholder="请输入文件原名称" clearable style="width: 220px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('assignmentAttachment', 'add')" v-hasPermi="['education:assignmentAttachment:list']" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button v-if="canEducationAction('assignmentAttachment', 'remove')" v-hasPermi="['education:assignmentAttachment:list']" type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('assignmentAttachment', 'export')" v-hasPermi="['education:assignmentAttachment:list']" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="assignmentAttachmentList" @selection-change="handleSelectionChange">
      <el-table-column v-if="canEducationAction('assignmentAttachment', 'edit') || canEducationAction('assignmentAttachment', 'remove')" type="selection" width="55" align="center" />
      <el-table-column label="作业附件ID" align="center" prop="assignmentAttachmentId" :show-overflow-tooltip="true" />
      <el-table-column label="作业标题" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('assignmentOptions', scope.row.assignmentId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="文件原名称" align="center" prop="fileName" :show-overflow-tooltip="true" />
      <el-table-column label="文件后缀" align="center" prop="fileSuffix" :show-overflow-tooltip="true" />
      <el-table-column label="文件大小，单位字节" align="center" prop="fileSize" :show-overflow-tooltip="true" />
      <el-table-column label="排序号" align="center" prop="orderNum" :show-overflow-tooltip="true" />
      <el-table-column label="上传人" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('userOptions', scope.row.uploadUserId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button v-if="canEducationAction('assignmentAttachment', 'edit')" v-hasPermi="['education:assignmentAttachment:list']" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button v-if="canEducationAction('assignmentAttachment', 'remove')" v-hasPermi="['education:assignmentAttachment:list']" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
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
        <el-form-item label="文件原名称" prop="fileName">
          <el-input v-model="form.fileName" placeholder="请输入文件原名称" />
        </el-form-item>
        <el-form-item label="文件地址" prop="fileUrl">
          <file-upload v-model="form.fileUrl" />
        </el-form-item>
        <el-form-item label="文件后缀" prop="fileSuffix">
          <el-input v-model="form.fileSuffix" placeholder="请输入文件后缀" />
        </el-form-item>
        <el-form-item label="文件大小，单位字节" prop="fileSize">
          <el-input v-model="form.fileSize" placeholder="请输入文件大小，单位字节" />
        </el-form-item>
        <el-form-item label="排序号" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入排序号" />
        </el-form-item>
        <el-form-item label="上传人" prop="uploadUserId">
          <el-select v-model="form.uploadUserId" placeholder="请选择上传人" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.userOptions" :key="item.value" :label="item.label" :value="item.value" />
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
import { listAssignmentAttachment, getAssignmentAttachment, delAssignmentAttachment, addAssignmentAttachment, updateAssignmentAttachment } from '@/api/education/assignmentAttachment'
import educationOptions from '@/mixins/educationOptions'
import educationActionAuth from '@/mixins/educationActionAuth'

export default {
  name: 'AssignmentAttachment',
  mixins: [educationOptions, educationActionAuth],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      assignmentAttachmentList: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        assignmentId: null,
        fileName: null
      },
      form: {},
      rules: {
        assignmentId: [
          { required: true, message: '请选择作业标题', trigger: 'change' }
        ],
        fileName: [
          { required: true, message: '请输入文件原名称', trigger: 'blur' }
        ],
        fileUrl: [
          { required: true, message: '请输入文件地址', trigger: 'change' }
        ],
        uploadUserId: [
          { required: true, message: '请选择上传人', trigger: 'change' }
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
      listAssignmentAttachment(this.queryParams).then(response => {
        this.assignmentAttachmentList = response.rows
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
        assignmentAttachmentId: null,
        assignmentId: null,
        fileName: null,
        fileUrl: null,
        fileSuffix: '',
        fileSize: 0,
        orderNum: 1,
        uploadUserId: null,
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
      this.ids = selection.map(item => item.assignmentAttachmentId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增作业附件'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.assignmentAttachmentId || this.ids[0]
      getAssignmentAttachment(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改作业附件'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.form.assignmentAttachmentId != null) {
          updateAssignmentAttachment(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addAssignmentAttachment(this.form).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.assignmentAttachmentId || this.ids
      this.$modal.confirm('是否确认删除作业附件编号为"' + ids + '"的数据项？').then(() => {
        return delAssignmentAttachment(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('education/assignmentAttachment/export', {
        ...this.queryParams
      }, 'assignmentAttachment_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
