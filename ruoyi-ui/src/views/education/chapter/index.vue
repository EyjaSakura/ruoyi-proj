<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="96px">
      <el-form-item label="课程名称" prop="courseId">
        <el-select v-model="queryParams.courseId" placeholder="请选择课程名称" clearable filterable style="width: 220px">
          <el-option v-for="item in educationOptions.courseOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="章节标题" prop="chapterTitle">
        <el-input v-model="queryParams.chapterTitle" placeholder="请输入章节标题" clearable style="width: 220px" @keyup.enter.native="handleQuery" />
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
        <el-button v-if="canEducationAction('chapter', 'add')" v-hasPermi="['education:chapter:list']" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button v-if="canEducationAction('chapter', 'remove')" v-hasPermi="['education:chapter:list']" type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('chapter', 'export')" v-hasPermi="['education:chapter:list']" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="chapterList" row-key="chapterId" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
      <el-table-column label="章节标题" align="left" prop="chapterTitle" :show-overflow-tooltip="true" min-width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.chapterTitle }}</span>
          <span style="margin-left: 8px; color: #999; font-size: 12px">
            ({{ scope.row.chapterType === '1' ? '章' : '节' }})
          </span>
          <span style="margin-left: 8px; color: #999; font-size: 12px">
            {{ scope.row.durationMinutes }}分钟
          </span>
          <span style="margin-left: 8px">
            <el-tag v-if="scope.row.status === '0'" size="mini" type="success">正常</el-tag>
            <el-tag v-else size="mini" type="danger">停用</el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="章节说明" align="center" prop="chapterDesc" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="排序号" align="center" prop="orderNum" width="80" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140">
        <template slot-scope="scope">
          <el-button v-if="canEducationAction('chapter', 'edit')" v-hasPermi="['education:chapter:list']" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button v-if="canEducationAction('chapter', 'remove')" v-hasPermi="['education:chapter:list']" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="课程名称" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择课程名称" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.courseOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="父章节" prop="parentId">
          <el-select v-model="form.parentId" placeholder="请选择父章节" clearable filterable style="width: 100%">
            <el-option label="顶级章节（章）" :value="0" />
            <el-option
              v-for="item in getParentChapterOptions(form.courseId, form.chapterId)"
              :key="item.value"
              :label="item.label + '（节）'"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <!-- ancestors 由后端自动计算，无需前端维护 -->
        <el-form-item label="章节标题" prop="chapterTitle">
          <el-input v-model="form.chapterTitle" placeholder="请输入章节标题" />
        </el-form-item>
        <el-form-item label="章节说明" prop="chapterDesc">
          <el-input v-model="form.chapterDesc" type="textarea" :rows="3" placeholder="请输入章节说明" />
        </el-form-item>
        <el-form-item label="排序号" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入排序号" />
        </el-form-item>
        <el-form-item label="建议学习时长" prop="durationMinutes">
          <el-input v-model="form.durationMinutes" placeholder="请输入建议学习时长" />
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
import { listChapter, getChapter, delChapter, addChapter, updateChapter } from '@/api/education/chapter'
import educationOptions from '@/mixins/educationOptions'
import educationActionAuth from '@/mixins/educationActionAuth'

export default {
  name: 'Chapter',
  mixins: [educationOptions, educationActionAuth],
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      showSearch: true,
      chapterList: [],
      title: '',
      open: false,
      queryParams: {
        courseId: null,
        chapterTitle: null,
        status: null
      },
      form: {},
      rules: {
        courseId: [
          { required: true, message: '请选择课程名称', trigger: 'change' }
        ],
        chapterTitle: [
          { required: true, message: '请输入章节标题', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadEducationOptions()
    this.getList()
  },
  methods: {
    getParentChapterOptions(courseId, excludeId) {
      return (this.educationOptions.chapterOptions || []).filter(item => {
        if (excludeId != null && String(item.value) === String(excludeId)) {
          return false
        }
        if (!courseId) {
          return true
        }
        return item.courseId != null && String(item.courseId) === String(courseId)
      })
    },
    buildChapterTree(rows) {
      if (!rows || rows.length === 0) return []
      const map = {}
      const roots = []
      rows.forEach(row => {
        map[row.chapterId] = { ...row, children: [] }
      })
      rows.forEach(row => {
        const node = map[row.chapterId]
        if (row.parentId === 0 || !map[row.parentId]) {
          roots.push(node)
        } else {
          map[row.parentId].children.push(node)
        }
      })
      return roots
    },
    getList() {
      this.loading = true
      listChapter(this.queryParams).then(response => {
        this.chapterList = this.buildChapterTree(response.rows)
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
        chapterId: null,
        courseId: null,
        parentId: 0,
        ancestors: '',
        chapterTitle: null,
        chapterType: '1',
        chapterDesc: '',
        orderNum: 1,
        durationMinutes: 120,
        status: '0',
        remark: null
      }
      this.resetForm('form')
    },
    handleQuery() {
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParams.courseId = null
      this.handleQuery()
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增课程章节'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.chapterId || this.ids[0]
      getChapter(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改课程章节'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.form.chapterId != null) {
          updateChapter(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addChapter(this.form).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.chapterId || this.ids
      this.$modal.confirm('是否确认删除课程章节编号为"' + ids + '"的数据项？').then(() => {
        return delChapter(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('education/chapter/export', {
        ...this.queryParams
      }, 'chapter_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
