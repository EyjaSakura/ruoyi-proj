<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="96px">
      <el-form-item label="教师工号" prop="teacherNo">
        <el-input v-model="queryParams.teacherNo" placeholder="请输入教师工号" clearable style="width: 220px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="教师姓名" prop="teacherName">
        <el-input v-model="queryParams.teacherName" placeholder="请输入教师姓名" clearable style="width: 220px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="所属院系" prop="deptId">
        <treeselect v-model="queryParams.deptId" :options="educationOptions.deptOptions" placeholder="请选择所属院系" style="width: 220px" />
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
        <el-button v-if="canEducationAction('teacher', 'add')" v-hasPermi="['education:teacher:list']" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button v-if="canEducationAction('teacher', 'remove')" v-hasPermi="['education:teacher:list']" type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('teacher', 'export')" v-hasPermi="['education:teacher:list']" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="teacherList" @selection-change="handleSelectionChange">
      <el-table-column v-if="canEducationAction('teacher', 'edit') || canEducationAction('teacher', 'remove')" type="selection" width="55" align="center" />
      <el-table-column type="index" label="#" width="60" align="center" />
      <el-table-column label="教师工号" align="center" prop="teacherNo" :show-overflow-tooltip="true" />
      <el-table-column label="教师姓名" align="center" prop="teacherName" :show-overflow-tooltip="true" />
      <el-table-column label="所属院系" align="center" prop="deptName" :show-overflow-tooltip="true" />
      <el-table-column label="职称" align="center" prop="jobTitle" :show-overflow-tooltip="true" />
      <el-table-column label="联系电话" align="center" prop="phone" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button v-if="canEducationAction('teacher', 'edit')" v-hasPermi="['education:teacher:list']" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button v-if="canEducationAction('teacher', 'remove')" v-hasPermi="['education:teacher:list']" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="关联用户" prop="userId">
          <el-select v-model="form.userId" placeholder="请选择关联用户" clearable filterable style="width: 100%">
            <el-option v-for="item in educationOptions.userOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="教师工号" prop="teacherNo">
          <el-input v-model="form.teacherNo" placeholder="请输入教师工号" />
        </el-form-item>
        <el-form-item label="教师姓名" prop="teacherName">
          <el-input v-model="form.teacherName" placeholder="请输入教师姓名" />
        </el-form-item>
        <el-form-item v-if="!isCollegeAdmin" label="所属院系" prop="deptId">
          <treeselect v-model="form.deptId" :options="educationOptions.deptOptions" placeholder="请选择所属院系" />
        </el-form-item>
        <el-form-item label="职称" prop="jobTitle">
          <el-input v-model="form.jobTitle" placeholder="请输入职称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="办公地点" prop="officeAddress">
          <el-input v-model="form.officeAddress" placeholder="请输入办公地点" />
        </el-form-item>
        <el-form-item label="研究方向" prop="researchDirection">
          <el-input v-model="form.researchDirection" placeholder="请输入研究方向" />
        </el-form-item>
        <el-form-item label="教师简介" prop="intro">
          <el-input v-model="form.intro" type="textarea" :rows="3" placeholder="请输入教师简介" />
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
import { listTeacher, getTeacher, delTeacher, addTeacher, updateTeacher } from '@/api/education/teacher'
import educationOptions from '@/mixins/educationOptions'
import educationActionAuth from '@/mixins/educationActionAuth'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'Teacher',
  components: { Treeselect },
  mixins: [educationOptions, educationActionAuth],
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      teacherList: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        teacherNo: null,
        teacherName: null,
        deptId: null,
        status: null
      },
      form: {},
      rules: {
        userId: [
          { required: true, message: '请选择关联用户', trigger: 'change' }
        ],
        teacherNo: [
          { required: true, message: '请输入教师工号', trigger: 'blur' }
        ],
        teacherName: [
          { required: true, message: '请输入教师姓名', trigger: 'blur' }
        ],
        deptId: [
          { required: true, message: '请选择所属院系', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    /** 是否为学院管理员（新增时隐藏院系选择，自动填入当前院系） */
    isCollegeAdmin() {
      return this.hasEducationRole('master')
    }
  },
  created() {
    this.loadEducationOptions()
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listTeacher(this.queryParams).then(response => {
        this.teacherList = response.rows
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
        teacherId: null,
        userId: null,
        teacherNo: null,
        teacherName: null,
        deptId: null,
        jobTitle: '',
        phone: '',
        email: '',
        officeAddress: '',
        researchDirection: '',
        intro: '',
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
      this.queryParams.deptId = null
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.teacherId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增教师档案'
      // 学院管理员：隐藏院系选择，自动填入当前院系
      if (this.hasEducationRole('master')) {
        this.form.deptId = this.$store.state.user.deptId || null
      }
    },
    handleUpdate(row) {
      this.reset()
      const id = row.teacherId || this.ids[0]
      getTeacher(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改教师档案'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.form.teacherId != null) {
          updateTeacher(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addTeacher(this.form).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.teacherId || this.ids
      this.$modal.confirm('是否确认删除教师档案编号为"' + ids + '"的数据项？').then(() => {
        return delTeacher(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('education/teacher/export', {
        ...this.queryParams
      }, 'teacher_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
