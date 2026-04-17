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
      <el-form-item label="是否完成" prop="completedFlag">
        <el-select v-model="queryParams.completedFlag" placeholder="请选择是否完成" clearable style="width: 220px">
          <el-option v-for="dict in dict.type.edu_progress_completed_flag" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('progress', 'add')" v-hasPermi="['education:progress:list']" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button v-if="canEducationAction('progress', 'remove')" v-hasPermi="['education:progress:list']" type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('progress', 'export')" v-hasPermi="['education:progress:list']" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="progressList" @selection-change="handleSelectionChange">
      <el-table-column v-if="canEducationAction('progress', 'edit') || canEducationAction('progress', 'remove')" type="selection" width="55" align="center" />
      <el-table-column label="学习进度ID" align="center" prop="progressId" :show-overflow-tooltip="true" />
      <el-table-column label="课程名称" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('courseOptions', scope.row.courseId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="章节名称" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('chapterOptions', scope.row.chapterId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="学生姓名" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('studentUserOptions', scope.row.studentUserId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="累计学习时长" align="center" prop="learnMinutes" :show-overflow-tooltip="true" />
      <el-table-column label="章节完成进度" align="center" prop="progressPercent" :show-overflow-tooltip="true" />
      <el-table-column label="是否完成" align="center" prop="completedFlag">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.edu_progress_completed_flag" :value="scope.row.completedFlag" />
        </template>
      </el-table-column>
      <el-table-column label="最近学习时间" align="center" prop="lastStudyTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastStudyTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button v-if="canEducationAction('progress', 'edit')" v-hasPermi="['education:progress:list']" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button v-if="canEducationAction('progress', 'remove')" v-hasPermi="['education:progress:list']" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
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
        <el-form-item label="章节名称" prop="chapterId">
          <el-select v-model="form.chapterId" placeholder="请选择章节名称" clearable filterable style="width: 100%">
            <el-option
              v-for="item in getChapterOptions(form.courseId)"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="学生姓名" prop="studentUserId">
          <el-select v-model="form.studentUserId" placeholder="请选择学生姓名" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.studentUserOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="累计学习时长" prop="learnMinutes">
          <el-input v-model="form.learnMinutes" placeholder="请输入累计学习时长" />
        </el-form-item>
        <el-form-item label="章节完成进度" prop="progressPercent">
          <el-input v-model="form.progressPercent" placeholder="请输入章节完成进度" />
        </el-form-item>
        <el-form-item label="是否完成" prop="completedFlag">
          <el-select v-model="form.completedFlag" placeholder="请选择是否完成" clearable style="width: 100%">
            <el-option v-for="dict in dict.type.edu_progress_completed_flag" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="首次学习时间" prop="firstStudyTime">
          <el-date-picker v-model="form.firstStudyTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择首次学习时间" clearable style="width: 100%" />
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
import { listProgress, getProgress, delProgress, addProgress, updateProgress } from '@/api/education/progress'
import educationOptions from '@/mixins/educationOptions'
import educationActionAuth from '@/mixins/educationActionAuth'

export default {
  name: 'Progress',
  mixins: [educationOptions, educationActionAuth],
  dicts: ['edu_progress_completed_flag'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      progressList: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseId: null,
        studentUserId: null,
        completedFlag: null
      },
      form: {},
      rules: {
        courseId: [
          { required: true, message: '请选择课程名称', trigger: 'change' }
        ],
        chapterId: [
          { required: true, message: '请选择章节名称', trigger: 'change' }
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
    getChapterOptions(courseId) {
      if (!courseId) {
        return this.educationOptions.chapterOptions || []
      }
      return (this.educationOptions.chapterOptions || []).filter(item => item.label.indexOf(this.getEducationOptionLabel('courseOptions', courseId)) > -1)
    },
    getList() {
      this.loading = true
      listProgress(this.queryParams).then(response => {
        this.progressList = response.rows
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
        progressId: null,
        courseId: null,
        chapterId: null,
        studentUserId: null,
        learnMinutes: 0,
        progressPercent: 0.00,
        completedFlag: '0',
        firstStudyTime: null,
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
      this.ids = selection.map(item => item.progressId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增学习进度'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.progressId || this.ids[0]
      getProgress(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改学习进度'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.form.progressId != null) {
          updateProgress(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addProgress(this.form).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.progressId || this.ids
      this.$modal.confirm('是否确认删除学习进度编号为"' + ids + '"的数据项？').then(() => {
        return delProgress(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('education/progress/export', {
        ...this.queryParams
      }, 'progress_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
