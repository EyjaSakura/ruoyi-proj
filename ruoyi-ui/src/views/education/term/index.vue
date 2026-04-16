<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="96px">
      <el-form-item label="学期编码" prop="termCode">
        <el-input v-model="queryParams.termCode" placeholder="请输入学期编码" clearable style="width: 220px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="学期名称" prop="termName">
        <el-input v-model="queryParams.termName" placeholder="请输入学期名称" clearable style="width: 220px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 220px">
          <el-option v-for="dict in dict.type.edu_term_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('term', 'add')" v-hasPermi="['education:term:list']" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('term', 'edit')" v-hasPermi="['education:term:list']" type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('term', 'remove')" v-hasPermi="['education:term:list']" type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('term', 'export')" v-hasPermi="['education:term:list']" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="termList" @selection-change="handleSelectionChange">
      <el-table-column v-if="canEducationAction('term', 'edit') || canEducationAction('term', 'remove')" type="selection" width="55" align="center" />
      <el-table-column label="学期ID" align="center" prop="termId" :show-overflow-tooltip="true" />
      <el-table-column label="学期编码" align="center" prop="termCode" :show-overflow-tooltip="true" />
      <el-table-column label="学期名称" align="center" prop="termName" :show-overflow-tooltip="true" />
      <el-table-column label="学年" align="center" prop="schoolYear" :show-overflow-tooltip="true" />
      <el-table-column label="学期序号" align="center" prop="semesterNo" :show-overflow-tooltip="true" />
      <el-table-column label="开学日期" align="center" prop="startDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束日期" align="center" prop="endDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.edu_term_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button v-if="canEducationAction('term', 'edit')" v-hasPermi="['education:term:list']" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button v-if="canEducationAction('term', 'remove')" v-hasPermi="['education:term:list']" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="学年" prop="schoolYear">
          <el-select v-model="form.schoolYear" placeholder="请选择学年" clearable style="width: 100%" @change="onSchoolYearChange">
            <el-option v-for="year in schoolYearOptions" :key="year" :label="year" :value="year" />
          </el-select>
        </el-form-item>
        <el-form-item label="学期序号" prop="semesterNo">
          <el-select v-model="form.semesterNo" placeholder="请选择学期序号" clearable style="width: 100%" @change="onSemesterNoChange">
            <el-option label="第一学期" value="1" />
            <el-option label="第二学期" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="学期编码" prop="termCode">
          <el-input v-model="form.termCode" placeholder="选择学年和学期后自动生成" readonly />
        </el-form-item>
        <el-form-item label="学期名称" prop="termName">
          <el-input v-model="form.termName" placeholder="选择学年和学期后自动生成" readonly />
        </el-form-item>
        <el-form-item label="开学日期" prop="startDate">
          <el-date-picker v-model="form.startDate" type="date" value-format="yyyy-MM-dd" placeholder="选择学期后自动填充，可手动修改" clearable style="width: 100%" />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker v-model="form.endDate" type="date" value-format="yyyy-MM-dd" placeholder="选择学期后自动填充，可手动修改" clearable style="width: 100%" />
        </el-form-item>
        <el-form-item label="选课开始时间" prop="selectStartTime">
          <el-date-picker v-model="form.selectStartTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择学期后自动填充，可手动修改" clearable style="width: 100%" />
        </el-form-item>
        <el-form-item label="选课结束时间" prop="selectEndTime">
          <el-date-picker v-model="form.selectEndTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择学期后自动填充，可手动修改" clearable style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" clearable style="width: 100%">
            <el-option v-for="dict in dict.type.edu_term_status" :key="dict.value" :label="dict.label" :value="dict.value" />
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
import { listTerm, getTerm, delTerm, addTerm, updateTerm } from '@/api/education/term'
import educationActionAuth from '@/mixins/educationActionAuth'

export default {
  name: 'Term',
  mixins: [educationActionAuth],
  dicts: ['edu_term_status'],
  data() {
    const currentYear = new Date().getFullYear()
    const schoolYearOptions = []
    for (let i = -2; i <= 3; i++) {
      schoolYearOptions.push((currentYear + i - 1) + '-' + (currentYear + i))
    }
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      termList: [],
      title: '',
      open: false,
      schoolYearOptions: schoolYearOptions,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        termCode: null,
        termName: null,
        status: null
      },
      form: {},
      rules: {
        schoolYear: [
          { required: true, message: '请选择学年', trigger: 'change' }
        ],
        semesterNo: [
          { required: true, message: '请选择学期序号', trigger: 'change' }
        ],
        startDate: [
          { required: true, message: '请选择开学日期', trigger: 'change' }
        ],
        endDate: [
          { required: true, message: '请选择结束日期', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listTerm(this.queryParams).then(response => {
        this.termList = response.rows
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
        termId: null,
        termCode: null,
        termName: null,
        schoolYear: null,
        semesterNo: null,
        startDate: null,
        endDate: null,
        selectStartTime: null,
        selectEndTime: null,
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
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.termId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增学期管理'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.termId || this.ids[0]
      getTerm(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改学期管理'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.form.termId != null) {
          updateTerm(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addTerm(this.form).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.termId || this.ids
      this.$modal.confirm('是否确认删除学期管理编号为"' + ids + '"的数据项？').then(() => {
        return delTerm(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 学年变化时，重新拼接学期编码和名称，并尝试重新填充日期 */
    onSchoolYearChange(val) {
      this.form.termCode = null
      this.form.termName = null
      if (val && this.form.semesterNo) {
        this.fillTermCodeAndName()
        this.fillDefaultDates()
      }
    },
    /** 学期序号变化时，拼接学期编码和名称，并自动填充日期 */
    onSemesterNoChange(val) {
      this.form.termCode = null
      this.form.termName = null
      if (val && this.form.schoolYear) {
        this.fillTermCodeAndName()
        this.fillDefaultDates()
      }
    },
    /** 自动生成学期编码和学期名称 */
    fillTermCodeAndName() {
      const { schoolYear, semesterNo } = this.form
      if (!schoolYear || !semesterNo) return
      this.form.termCode = schoolYear + '-' + semesterNo
      this.form.termName = schoolYear + '学年第' + semesterNo + '学期'
    },
    /** 根据学期序号自动填充开学/结束日期和选课时间 */
    fillDefaultDates() {
      const { schoolYear, semesterNo } = this.form
      if (!schoolYear || !semesterNo) return
      const parts = schoolYear.split('-')
      const yearStart = parseInt(parts[0])
      const yearEnd = parseInt(parts[1])

      let startDateStr, endDateStr
      if (semesterNo === '1') {
        // 第一学期：9月1日 ~ 次年1月16日
        startDateStr = yearStart + '-09-01'
        endDateStr = yearEnd + '-01-16'
      } else {
        // 第二学期：3月1日 ~ 7月16日
        startDateStr = yearEnd + '-03-01'
        endDateStr = yearEnd + '-07-16'
      }

      this.form.startDate = startDateStr
      this.form.endDate = endDateStr

      // 选课时间：开学前15天 ~ 开学后15天
      const startDate = new Date(startDateStr)
      const fmt = (d) => {
        const pad = (n) => String(n).padStart(2, '0')
        return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate())
      }
      const before15 = new Date(startDate)
      before15.setDate(before15.getDate() - 15)
      const after15 = new Date(startDate)
      after15.setDate(after15.getDate() + 15)

      this.form.selectStartTime = fmt(before15) + ' 00:00:00'
      this.form.selectEndTime = fmt(after15) + ' 23:59:59'
    },
    handleExport() {
      this.download('education/term/export', {
        ...this.queryParams
      }, 'term_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
