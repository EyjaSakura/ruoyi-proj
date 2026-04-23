<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="80px">
      <el-form-item label="课程号" prop="courseCode">
        <el-input v-model="queryParams.courseCode" placeholder="请输入课程号" clearable style="width: 200px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="课程名称" prop="courseName">
        <el-input v-model="queryParams.courseName" placeholder="请输入课程名称" clearable style="width: 200px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleImport">导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" label="#" width="60" align="center" />
      <el-table-column label="课程号" align="center" prop="courseCode" :show-overflow-tooltip="true" width="120" />
      <el-table-column label="课程名称" align="center" prop="courseName" :show-overflow-tooltip="true" />
      <el-table-column label="学分" align="center" prop="credit" width="80" />
      <el-table-column label="开课院系" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('deptOptions', scope.row.deptId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改课程库对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="课程号" prop="courseCode">
              <el-input v-model="form.courseCode" placeholder="请输入课程号" maxlength="20" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学分" prop="credit">
              <el-input-number v-model="form.credit" :min="0" :max="20" :precision="1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="课程名称" prop="courseName">
              <el-input v-model="form.courseName" placeholder="请输入课程名称" maxlength="100" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="课程类型" prop="courseType">
              <el-select v-model="form.courseType" placeholder="请选择课程类型" clearable style="width: 100%">
                <el-option v-for="dict in dict.type.edu_course_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="课程简介" prop="intro">
              <el-input v-model="form.intro" type="textarea" :rows="2" placeholder="请输入课程简介" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog title="导入课程" :visible.sync="importOpen" width="500px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :on-error="handleFileError"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">
          <el-checkbox v-model="upload.updateSupport" />是否更新已存在的课程数据
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="importOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCourse, getCourse, delCourse, addCourse, updateCourse, importTemplate } from '@/api/education/courseLib'
import { getToken } from '@/utils/auth'
import educationOptions from '@/mixins/educationOptions'
import educationActionAuth from '@/mixins/educationActionAuth'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'CourseLib',
  components: { Treeselect },
  mixins: [educationOptions, educationActionAuth],
  dicts: ['edu_course_type'],
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
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseCode: null,
        courseName: null
      },
      form: {},
      rules: {
        courseCode: [
          { required: true, message: '请输入课程号', trigger: 'blur' }
        ],
        courseName: [
          { required: true, message: '请输入课程名称', trigger: 'blur' }
        ]
      },
      // 导入参数
      importOpen: false,
      upload: {
        isUploading: false,
        updateSupport: false,
        headers: { Authorization: 'Bearer ' + getToken() },
        url: process.env.VUE_APP_BASE_API + '/education/courseLib/importData'
      }
    }
  },
  computed: {
    /** 是否为系统管理员 */
    isSystemAdmin() {
      return this.hasEducationRole('admin')
    },
    /** 是否为学院管理员 */
    isCollegeAdmin() {
      return this.hasEducationRole('master')
    }
  },
  created() {
    this.loadEducationOptions()
    this.getList()
  },
  methods: {
    /** 查询课程库列表 */
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
    /** 取消按钮 */
    cancel() {
      this.open = false
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        courseId: null,
        courseCode: null,
        courseName: null,
        courseCover: '',
        credit: 3.0,
        courseType: '2',  // 默认选修
        deptId: null,
        intro: null,
        remark: null
      }
      // 学院管理员：自动填入当前院系
      if (this.isCollegeAdmin) {
        this.form.deptId = this.$store.state.user.deptId || null
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.courseId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加课程'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.courseId || this.ids[0]
      getCourse(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改课程'
      })
    },
    /** 提交按钮 */
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.courseId || this.ids
      this.$modal.confirm('是否确认删除选中的课程？').then(() => {
        return delCourse(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.url = process.env.VUE_APP_BASE_API + '/education/courseLib/importData?updateSupport=' + this.upload.updateSupport
      this.importOpen = true
      this.$nextTick(() => {
        this.$refs.upload.clearFiles()
      })
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('/education/courseLib/importTemplate', {
      }, `course_template_${new Date().getTime()}.xlsx`)
    },
    /** 文件上传中处理 */
    handleFileUploadProgress() {
      this.upload.isUploading = true
    },
    /** 文件上传成功处理 */
    handleFileSuccess(response) {
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      this.$alert('<div style="word-break: break-all;">' + response.msg + '</div>', '导入结果', { dangerouslyUseHTMLString: true })
      this.importOpen = false
      this.getList()
    },
    /** 文件上传失败处理 */
    handleFileError(err) {
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      this.$modal.msgError('导入失败')
    },
    /** 提交上传文件 */
    submitFileForm() {
      if (this.$refs.upload.uploadFiles.length === 0) {
        this.$modal.msgWarning('请选择上传文件')
        return
      }
      this.$refs.upload.submit()
    }
  }
}
</script>

<style scoped lang="scss">
</style>
