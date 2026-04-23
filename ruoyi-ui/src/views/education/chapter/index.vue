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
        <el-button type="info" plain icon="el-icon-sort" size="mini" @click="toggleExpandAll">展开/折叠</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('chapter', 'export')" v-hasPermi="['education:chapter:list']" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-if="refreshTable" v-loading="loading" :data="chapterList" row-key="chapterId" :default-expand-all="isExpandAll" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
      <el-table-column prop="chapterTitle" label="章节" min-width="300">
        <template slot-scope="scope">
          <!-- 虚拟课程根节点：chapterType='0'，只显示课程名 -->
          <span v-if="scope.row._isVirtualRoot">{{ scope.row.chapterTitle }}</span>
          <!-- 真实章节节点 -->
          <span v-else>{{ scope.row.chapterTitle }}
            <span style="margin-left: 8px; color: #999; font-size: 12px">
              ({{ scope.row.chapterType == 1 ? '章' : '节' }})
            </span>
            <span v-if="scope.row.durationMinutes" style="margin-left: 8px; color: #999; font-size: 12px">{{ scope.row.durationMinutes }}分钟</span>
            <span style="margin-left: 8px">
              <el-tag v-if="scope.row.status == '0' || scope.row.status == 0" size="mini" type="success">正常</el-tag>
              <el-tag v-else size="mini" type="danger">停用</el-tag>
            </span>
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="chapterDesc" label="章节说明" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column prop="orderNum" label="排序" width="80" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140">
        <template slot-scope="scope">
          <!-- 虚拟课程根节点不可操作 -->
          <el-button v-if="!scope.row._isVirtualRoot && canEducationAction('chapter', 'edit')" v-hasPermi="['education:chapter:list']" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button v-if="!scope.row._isVirtualRoot && canEducationAction('chapter', 'remove')" v-hasPermi="['education:chapter:list']" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="课程名称" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择课程名称" clearable filterable style="width: 100%" @change="onCourseChange">
            <el-option v-for="item in educationOptions.courseOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="章节类型" prop="parentChapterType">
          <el-radio-group v-model="form.parentChapterType" size="small">
            <el-radio-button label="top">新建顶级章节（章）</el-radio-button>
            <el-radio-button label="chapter">作为顶级章节的子节（节）</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.parentChapterType !== 'top'" label="父章节" prop="parentId">
          <el-select v-model="form.parentId" placeholder="请选择父章节" clearable filterable style="width: 100%">
            <el-option
              v-for="item in getParentChapterOptions(form.courseId, form.chapterId, form.parentChapterType)"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
          <span v-if="form.parentChapterType === 'chapter' && form.courseId && getParentChapterOptions(form.courseId, form.chapterId, 'chapter').length === 0" style="color: #E6A23C; font-size: 12px; margin-top: 4px;">
            提示：该课程下暂无顶级章节，请先新建顶级章节
          </span>
          <span v-if="form.parentChapterType === 'chapter' && !form.courseId" style="color: #909399; font-size: 12px; margin-top: 4px;">
            请先选择课程名称
          </span>
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
      isExpandAll: true,
      refreshTable: true,
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
    getCourseLabel(courseId) {
      return this.getEducationOptionLabel('courseOptions', courseId)
    },
    getParentChapterOptions(courseId, excludeId, parentChapterType) {
      const all = this.educationOptions.chapterOptions || []
      return all.filter(item => {
        if (excludeId != null && String(item.value) === String(excludeId)) return false
        if (!courseId) return false
        if (String(item.courseId) !== String(courseId)) return false
        // 'chapter': 只能以章(type=1)为父
        if (parentChapterType === 'chapter' && String(item.chapterType) !== '1') return false
        return true
      })
    },
    onCourseChange() {
      // 切换课程后重置章节类型和父节点
      this.form.parentChapterType = 'top'
      this.form.parentId = 0
    },
    buildChapterTree(rows) {
      if (!rows || rows.length === 0) return []
      const map = {}
      rows.forEach(row => {
        map[row.chapterId] = { ...row, children: [] }
      })
      const courseRoots = []
      const courseMap = {}
      rows.forEach(row => {
        const node = map[row.chapterId]
        const parentNode = map[row.parentId]
        if (row.parentId === 0 || !parentNode) {
          const courseId = row.courseId
          if (!courseMap[courseId]) {
            // 优先从 courseOptions 查找，找不到时用 courseId兜底
            let courseName = this.getCourseLabel(courseId)
            if (!courseName) courseName = '课程(ID:' + courseId + ')'
            courseMap[courseId] = {
              chapterId: -courseId,
              chapterTitle: courseName,
              courseId: courseId,
              chapterType: '0',
              children: [],
              _isVirtualRoot: true,
              status: '0'
            }
            courseRoots.push(courseMap[courseId])
          }
          courseMap[courseId].children.push(node)
        } else {
          parentNode.children.push(node)
        }
      })
      return courseRoots
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
    toggleExpandAll() {
      this.refreshTable = false
      this.isExpandAll = !this.isExpandAll
      this.$nextTick(() => {
        this.refreshTable = true
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
        parentChapterType: 'top',
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
      getChapter(row.chapterId).then(response => {
        this.form = response.data
        // 根据 chapterType 反推 parentChapterType（章=1 → top；节=2 → chapter）
        const ct = String(this.form.chapterType)
        if (ct === '1') {
          this.form.parentChapterType = 'top'
          this.form.parentId = 0
        } else {
          // 节(type=2)或其他 → 作为子节
          this.form.parentChapterType = 'chapter'
        }
        this.open = true
        this.title = '修改课程章节'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        // 根据 parentChapterType 计算 chapterType 和 parentId
        if (this.form.parentChapterType === 'top') {
          this.form.chapterType = '1'
          this.form.parentId = 0
        } else {
          // 'chapter': 作为顶级章节的子节
          this.form.chapterType = '2'
          this.form.parentId = this.form.parentId || 0
        }
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
      this.$modal.confirm('是否确认删除课程章节编号为"' + row.chapterId + '"的数据项？').then(() => {
        return delChapter(row.chapterId)
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
