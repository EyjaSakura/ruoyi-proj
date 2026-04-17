<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="96px">
      <el-form-item label="课程名称" prop="courseId">
        <el-select v-model="queryParams.courseId" placeholder="请选择课程名称" clearable filterable style="width: 220px">
          <el-option v-for="item in educationOptions.courseOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="资源标题" prop="resourceTitle">
        <el-input v-model="queryParams.resourceTitle" placeholder="请输入资源标题" clearable style="width: 220px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="资源类型" prop="resourceType">
        <el-select v-model="queryParams.resourceType" placeholder="请选择资源类型" clearable style="width: 220px">
          <el-option v-for="dict in dict.type.edu_resource_type" :key="dict.value" :label="dict.label" :value="dict.value" />
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
        <el-button v-if="canEducationAction('resource', 'add')" v-hasPermi="['education:resource:list']" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button v-if="canEducationAction('resource', 'remove')" v-hasPermi="['education:resource:list']" type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('resource', 'export')" v-hasPermi="['education:resource:list']" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="resourceList" @selection-change="handleSelectionChange">
      <el-table-column v-if="canEducationAction('resource', 'edit') || canEducationAction('resource', 'remove')" type="selection" width="55" align="center" />
      <el-table-column type="index" label="#" width="60" align="center" />
      <el-table-column label="课程名称" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('courseOptions', scope.row.courseId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属章节" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.chapterId ? getEducationOptionLabel('chapterOptions', scope.row.chapterId) : '' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="资源标题" align="center" prop="resourceTitle" :show-overflow-tooltip="true" />
      <el-table-column label="资源类型" align="center" prop="resourceType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.edu_resource_type" :value="scope.row.resourceType" />
        </template>
      </el-table-column>
      <el-table-column label="文件原名称" align="center" prop="fileName" :show-overflow-tooltip="true" />
      <el-table-column label="下载次数" align="center" prop="downloadCount" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button v-if="canEducationAction('resource', 'edit')" v-hasPermi="['education:resource:list']" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button v-if="canEducationAction('resource', 'remove')" v-hasPermi="['education:resource:list']" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
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
        <el-form-item label="所属章节" prop="chapterId">
          <el-select v-model="form.chapterId" placeholder="请选择所属章节" clearable filterable style="width: 100%">
            <el-option
              v-for="item in getChapterOptions(form.courseId)"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="资源标题" prop="resourceTitle">
          <el-input v-model="form.resourceTitle" placeholder="请输入资源标题" />
        </el-form-item>
        <el-form-item label="资源类型" prop="resourceType">
          <el-select v-model="form.resourceType" placeholder="请选择资源类型" clearable style="width: 100%">
            <el-option v-for="dict in dict.type.edu_resource_type" :key="dict.value" :label="dict.label" :value="dict.value" />
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
        <el-form-item label="下载次数" prop="downloadCount">
          <el-input v-model="form.downloadCount" placeholder="请输入下载次数" />
        </el-form-item>
        <el-form-item label="上传人" prop="uploadUserId">
          <el-select v-model="form.uploadUserId" placeholder="请选择上传人" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.userOptions" :key="item.value" :label="item.label" :value="item.value" />
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
import { listResource, getResource, delResource, addResource, updateResource } from '@/api/education/resource'
import educationOptions from '@/mixins/educationOptions'
import educationActionAuth from '@/mixins/educationActionAuth'

export default {
  name: 'Resource',
  mixins: [educationOptions, educationActionAuth],
  dicts: ['edu_resource_type', 'sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      resourceList: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseId: null,
        resourceTitle: null,
        resourceType: null,
        status: null
      },
      form: {},
      rules: {
        courseId: [
          { required: true, message: '请选择课程名称', trigger: 'change' }
        ],
        resourceTitle: [
          { required: true, message: '请输入资源标题', trigger: 'blur' }
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
    getChapterOptions(courseId) {
      if (!courseId) {
        return this.educationOptions.chapterOptions || []
      }
      return (this.educationOptions.chapterOptions || []).filter(item => item.label.indexOf(this.getEducationOptionLabel('courseOptions', courseId)) > -1)
    },
    getList() {
      this.loading = true
      listResource(this.queryParams).then(response => {
        this.resourceList = response.rows
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
        resourceId: null,
        courseId: null,
        chapterId: null,
        resourceTitle: null,
        resourceType: '1',
        fileName: null,
        fileUrl: null,
        fileSuffix: '',
        fileSize: 0,
        downloadCount: 0,
        uploadUserId: null,
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
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.resourceId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增学习资源'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.resourceId || this.ids[0]
      getResource(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改学习资源'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.form.resourceId != null) {
          updateResource(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addResource(this.form).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.resourceId || this.ids
      this.$modal.confirm('是否确认删除学习资源编号为"' + ids + '"的数据项？').then(() => {
        return delResource(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('education/resource/export', {
        ...this.queryParams
      }, 'resource_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
