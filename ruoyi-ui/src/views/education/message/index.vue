<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="96px">
      <el-form-item label="接收用户" prop="userId">
        <el-select v-model="queryParams.userId" placeholder="请选择接收用户" clearable filterable style="width: 220px">
          <el-option v-for="item in educationOptions.userOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="消息标题" prop="messageTitle">
        <el-input v-model="queryParams.messageTitle" placeholder="请输入消息标题" clearable style="width: 220px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="已读状态" prop="readStatus">
        <el-select v-model="queryParams.readStatus" placeholder="请选择已读状态" clearable style="width: 220px">
          <el-option v-for="dict in dict.type.edu_message_read_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('message', 'add')" v-hasPermi="['education:message:list']" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('message', 'edit')" v-hasPermi="['education:message:list']" type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('message', 'remove')" v-hasPermi="['education:message:list']" type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('message', 'export')" v-hasPermi="['education:message:list']" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="messageList" @selection-change="handleSelectionChange">
      <el-table-column v-if="canEducationAction('message', 'edit') || canEducationAction('message', 'remove')" type="selection" width="55" align="center" />
      <el-table-column label="消息ID" align="center" prop="messageId" :show-overflow-tooltip="true" />
      <el-table-column label="接收用户" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('userOptions', scope.row.userId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="业务类型" align="center" prop="bizType" :show-overflow-tooltip="true" />
      <el-table-column label="消息标题" align="center" prop="messageTitle" :show-overflow-tooltip="true" />
      <el-table-column label="已读状态" align="center" prop="readStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.edu_message_read_status" :value="scope.row.readStatus" />
        </template>
      </el-table-column>
      <el-table-column label="发送时间" align="center" prop="sendTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.sendTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="过期时间" align="center" prop="expireTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button v-if="canEducationAction('message', 'edit')" v-hasPermi="['education:message:list']" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button v-if="canEducationAction('message', 'remove')" v-hasPermi="['education:message:list']" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="接收用户" prop="userId">
          <el-select v-model="form.userId" placeholder="请选择接收用户" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.userOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程名称" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择课程名称" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.courseOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="作业标题" prop="assignmentId">
          <el-select v-model="form.assignmentId" placeholder="请选择作业标题" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.assignmentOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="公告标题" prop="noticeId">
          <el-select v-model="form.noticeId" placeholder="请选择公告标题" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.noticeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="业务类型" prop="bizType">
          <el-input v-model="form.bizType" placeholder="请输入业务类型" />
        </el-form-item>
        <el-form-item label="消息标题" prop="messageTitle">
          <el-input v-model="form.messageTitle" placeholder="请输入消息标题" />
        </el-form-item>
        <el-form-item label="消息内容" prop="messageContent">
          <el-input v-model="form.messageContent" type="textarea" :rows="3" placeholder="请输入消息内容" />
        </el-form-item>
        <el-form-item label="发送人" prop="senderUserId">
          <el-select v-model="form.senderUserId" placeholder="请选择发送人" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.userOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="发送时间" prop="sendTime">
          <el-date-picker v-model="form.sendTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择发送时间" clearable style="width: 100%" />
        </el-form-item>
        <el-form-item label="已读状态" prop="readStatus">
          <el-select v-model="form.readStatus" placeholder="请选择已读状态" clearable style="width: 100%">
            <el-option v-for="dict in dict.type.edu_message_read_status" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="已读时间" prop="readTime">
          <el-date-picker v-model="form.readTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择已读时间" clearable style="width: 100%" />
        </el-form-item>
        <el-form-item label="页面跳转路径" prop="linkPath">
          <el-input v-model="form.linkPath" placeholder="请输入页面跳转路径" />
        </el-form-item>
        <el-form-item label="过期时间" prop="expireTime">
          <el-date-picker v-model="form.expireTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择过期时间" clearable style="width: 100%" />
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
import { listMessage, getMessage, delMessage, addMessage, updateMessage } from '@/api/education/message'
import educationOptions from '@/mixins/educationOptions'
import educationActionAuth from '@/mixins/educationActionAuth'

export default {
  name: 'Message',
  mixins: [educationOptions, educationActionAuth],
  dicts: ['edu_message_read_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      messageList: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        messageTitle: null,
        readStatus: null
      },
      form: {},
      rules: {
        userId: [
          { required: true, message: '请选择接收用户', trigger: 'change' }
        ],
        bizType: [
          { required: true, message: '请输入业务类型', trigger: 'blur' }
        ],
        messageTitle: [
          { required: true, message: '请输入消息标题', trigger: 'blur' }
        ],
        messageContent: [
          { required: true, message: '请输入消息内容', trigger: 'blur' }
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
      listMessage(this.queryParams).then(response => {
        this.messageList = response.rows
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
        messageId: null,
        userId: null,
        courseId: null,
        assignmentId: null,
        noticeId: null,
        bizType: null,
        messageTitle: null,
        messageContent: null,
        senderUserId: null,
        sendTime: null,
        readStatus: '0',
        readTime: null,
        linkPath: '',
        expireTime: null,
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
      this.ids = selection.map(item => item.messageId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增消息提醒'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.messageId || this.ids[0]
      getMessage(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改消息提醒'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.form.messageId != null) {
          updateMessage(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addMessage(this.form).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.messageId || this.ids
      this.$modal.confirm('是否确认删除消息提醒编号为"' + ids + '"的数据项？').then(() => {
        return delMessage(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('education/message/export', {
        ...this.queryParams
      }, 'message_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
