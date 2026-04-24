<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="96px">
      <el-form-item label="课程号" prop="courseCode">
        <el-input v-model="queryParams.courseCode" placeholder="请输入课程号" clearable style="width: 220px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="课序号" prop="classNo">
        <el-input v-model="queryParams.classNo" placeholder="如 01 02" clearable style="width: 120px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="课程名称" prop="courseName">
        <el-input v-model="queryParams.courseName" placeholder="请输入课程名称" clearable style="width: 220px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item v-if="isSystemAdmin" label="所属学院" prop="deptId">
        <treeselect v-model="queryParams.deptId" :options="educationOptions.deptOptions" placeholder="请选择所属学院" clearable style="width: 220px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-if="canCreateCourse" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button v-if="canManageCourse" type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="canEducationAction('course', 'export')" v-hasPermi="['education:course:list']" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
      <el-table-column v-if="canManageCourse" type="selection" width="55" align="center" />
      <el-table-column type="index" label="#" width="60" align="center" />
      <el-table-column label="课程号" align="center" prop="courseCode" :show-overflow-tooltip="true" />
      <el-table-column label="课序号" align="center" prop="classNo" width="80" />
      <el-table-column label="课程名称" align="center" prop="courseName" :show-overflow-tooltip="true" />
      <el-table-column label="所属学期" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('termOptions', scope.row.termId) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="isCollegeAdmin" label="授课教师" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('teacherUserOptions', scope.row.teacherUserId) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="!isCollegeAdmin" label="开课院系" align="center">
        <template slot-scope="scope">
          <span>{{ getEducationOptionLabel('deptOptions', scope.row.deptId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总课时" align="center" prop="totalHours" :show-overflow-tooltip="true" />
      <el-table-column label="学分" align="center" prop="credit" :show-overflow-tooltip="true" />
      <el-table-column v-if="canManageCourse" label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属学期" prop="termId">
              <el-select v-model="form.termId" placeholder="请选择所属学期" clearable filterable style="width: 100%" @change="onTermChange">
                <el-option v-for="item in educationOptions.termOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="!isCollegeAdmin" label="开课院系" prop="deptId">
              <treeselect v-model="form.deptId" :options="educationOptions.deptOptions" placeholder="请选择开课院系" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="课程" prop="courseCode">
              <el-select
                v-model="form.courseCode"
                placeholder="请输入课程名称/课程号搜索"
                clearable
                filterable
                remote
                :remote-method="searchCourse"
                :loading="courseLoading"
                style="width: 100%"
                @change="onCourseChange"
                @visible-change="onCourseVisible"
              >
                <el-option
                  v-for="item in courseOptions"
                  :key="item.courseId"
                  :label="item.courseName + '（' + item.courseCode + '）'"
                  :value="item.courseCode"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="课堂数量" prop="classCount">
              <el-input-number v-model="form.classCount" :min="1" :max="99" controls-position="right" style="width: 100%" @change="onClassCountChange" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总课时" prop="totalHours">
              <el-input-number v-model="form.totalHours" :min="0" :max="999" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="选课开始时间" prop="selectStartTime">
              <el-date-picker v-model="form.selectStartTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择学期后自动填充，可手动修改" clearable style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="选课结束时间" prop="selectEndTime">
              <el-date-picker v-model="form.selectEndTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择学期后自动填充，可手动修改" clearable style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col v-if="form.courseId == null" :span="12">
            <el-form-item label="课程类型" prop="courseType">
              <el-select v-model="form.courseType" placeholder="请选择课程类型" clearable disabled style="width: 100%">
                <el-option v-for="dict in dict.type.edu_course_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col v-if="form.courseId == null" :span="12">
            <el-form-item label="学分" prop="credit">
              <el-input-number v-model="form.credit" :min="0" :max="20" :precision="1" controls-position="right" disabled style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col v-if="form.courseId == null" :span="24">
            <el-form-item label="课程简介" prop="intro">
              <el-input v-model="form.intro" type="textarea" :rows="2" disabled placeholder="请输入课程简介" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 班级卡片区域：每个班单独配置教师和容量（新增时显示） -->
        <el-row v-if="form.courseId == null" :gutter="16" class="mt16">
          <el-col v-for="(card, index) in classCards" :key="index" :span="12">
            <el-card shadow="hover" class="class-card">
              <div slot="header" class="card-header">
                <span>课堂 {{ card.classNo }}</span>
              </div>
              <el-form-item label="授课教师">
                <el-select
                  v-model="card.teacherUserId"
                  placeholder="输入教师姓名或工号搜索"
                  filterable
                  remote
                  :remote-method="(query) => searchTeacherForCard(query, card)"
                  :loading="card.teacherLoading"
                  style="width: 100%"
                >
                  <el-option v-for="item in card.teacherOptions" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </el-form-item>
              <el-form-item label="容量">
                <el-select v-model="card.capacity" placeholder="请选择容量" style="width: 100%">
                  <el-option v-for="opt in capacityOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
                </el-select>
              </el-form-item>
            </el-card>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCourse, getCourse, delCourse, addCourse, updateCourse, addCourseTeacher } from '@/api/education/course'
import { listCourse as listCourseLib } from '@/api/education/courseLib'
import { searchTeacher } from '@/api/education/courseTeacher'
import educationOptions from '@/mixins/educationOptions'
import educationActionAuth from '@/mixins/educationActionAuth'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'Course',
  components: { Treeselect },
  mixins: [educationOptions, educationActionAuth],
  dicts: ['edu_course_type', 'edu_course_status'],
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
      /** 容量上限下拉选项 */
      capacityOptions: [
        { label: '10人', value: 10 },
        { label: '20人', value: 20 },
        { label: '30人', value: 30 },
        { label: '40人', value: 40 },
        { label: '50人', value: 50 },
        { label: '60人', value: 60 },
        { label: '不限', value: 0 }
      ],
      /** 学期选项原始数据（用于按学期ID查开学日期） */
      termListCache: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseCode: null,
        classNo: null,
        courseName: null,
        deptId: null,
        status: null
      },
      form: {},
      /** 课程远程搜索选项（支持课程号和课程名称） */
      courseOptions: [],
      courseLoading: false,
      /** 当前选中的课程库课程（用于自动填充） */
      selectedLibCourse: null,
      /** 班级卡片数组（用于批量新增时每个班单独配置教师和容量） */
      classCards: [],
      rules: {
        termId: [
          { required: true, message: '请选择所属学期', trigger: 'change' }
        ],
        deptId: [
          { required: true, message: '请选择开课院系', trigger: 'change' }
        ],
        courseCode: [
          { required: true, message: '请选择或输入课程号', trigger: 'change' }
        ],
        classCount: [
          { required: true, message: '请输入课堂数量', trigger: 'blur' }
        ],
        courseName: [
          { required: true, message: '请选择或输入课程名称', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    /** 是否为系统管理员（仅系统管理员可见跨学院查询） */
    isSystemAdmin() {
      return this.hasEducationRole('admin')
    },
    /** 是否为学院管理员（新增时隐藏院系选择，自动填入当前院系） */
    isCollegeAdmin() {
      return this.hasEducationRole('master')
    },
    canCreateCourse() {
      return this.canEducationAction('course', 'add')
    },
    canManageCourse() {
      return this.canEducationAction('course', 'edit') || this.canEducationAction('course', 'remove')
    }
  },
  created() {
    this.loadEducationOptions()
    this.getList()
  },
  methods: {
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
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        courseId: null,
        termId: null,
        deptId: null,
        courseCode: null,
        classCount: 1,
        courseName: null,
        courseCover: '',
        courseType: '2',
        credit: 3.0,
        totalHours: 30,
        selectStartTime: null,
        selectEndTime: null,
        intro: null,
        status: '0',
        remark: null
      }
      // 清空搜索选项
      this.courseOptions = []
      this.selectedLibCourse = null
      // 清空班级卡片
      this.classCards = []
      // 初始化单个班级卡片
      this.initClassCards()
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
      this.ids = selection.map(item => item.courseId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    handleAdd() {
      if (!this.canCreateCourse) {
        return
      }
      this.reset()
      // 学院管理员：自动填入当前院系
      if (this.isCollegeAdmin) {
        this.form.deptId = this.$store.state.user.deptId || null
      }
      // 根据系统时间预填当前所属学期
      this.autoFillCurrentTerm()
      this.open = true
      this.title = '新增课堂'
    },
    /** 根据当前日期自动匹配学期并预填 */
    autoFillCurrentTerm() {
      const now = new Date()
      const termOptions = this.educationOptions.termOptions || []
      // 优先找包含当前日期的学期
      let matched = null
      for (const t of termOptions) {
        if (t.startDate && t.endDate) {
          const start = new Date(t.startDate)
          const end = new Date(t.endDate)
          if (now >= start && now <= end) {
            matched = t
            break
          }
        }
      }
      // 找不到则取最近一个未来学期（startDate最近的）
      if (!matched) {
        const futurTerms = termOptions.filter(t => t.startDate && new Date(t.startDate) > now)
        if (futurTerms.length > 0) {
          matched = futurTerms.sort((a, b) => new Date(a.startDate) - new Date(b.startDate))[0]
        }
      }
      // 仍找不到则取最后一个学期
      if (!matched && termOptions.length > 0) {
        matched = termOptions[termOptions.length - 1]
      }
      if (matched) {
        this.form.termId = matched.value
        this.fillCourseSelectTimes(matched.value)
      }
    },
    /** 课程号远程搜索 */
    searchCourseCode(query) {
      this.courseCodeLoading = true
      listCourseLib({
        pageNum: 1,
        pageSize: 50,
        courseCode: query || undefined,
        // 学院管理员只能搜索本学院的课程
        deptId: this.isCollegeAdmin ? (this.$store.state.user.deptId || null) : null
      }).then(response => {
        this.courseOptions = response.rows
        this.courseLoading = false
      }).catch(() => {
        this.courseLoading = false
      })
    },
    /** 课程远程搜索（同时支持课程号和课程名称搜索） */
    searchCourse(query) {
      this.courseLoading = true
      listCourseLib({
        pageNum: 1,
        pageSize: 50,
        // 同时传课程号和课程名称参数，后端会做OR匹配
        courseCode: query || undefined,
        courseName: query || undefined,
        // 学院管理员只能搜索本学院的课程
        deptId: this.isCollegeAdmin ? (this.$store.state.user.deptId || null) : null
      }).then(response => {
        this.courseOptions = response.rows
        this.courseLoading = false
      }).catch(() => {
        this.courseLoading = false
      })
    },
    /** 课程选择变化时，自动填充完整课程信息 */
    onCourseChange(courseCode) {
      if (courseCode) {
        // 从课程选项中找到对应课程
        const course = this.courseOptions.find(c => c.courseCode === courseCode)
        if (course) {
          this.selectedLibCourse = course
          // 填入完整的课程库信息（学期和总课时由用户自行设置）
          this.$set(this.form, 'courseCode', course.courseCode)
          this.$set(this.form, 'courseName', course.courseName)
          this.$set(this.form, 'courseType', course.courseType)
          this.$set(this.form, 'credit', course.credit)
          this.$set(this.form, 'intro', course.intro)
        }
      } else {
        this.selectedLibCourse = null
      }
    },
    /** 课程下拉框展开时，若无选项则自动加载全部 */
    onCourseVisible(visible) {
      if (visible && this.courseOptions.length === 0) {
        this.searchCourse('')
      }
    },
    handleUpdate(row) {
      if (!this.canManageCourse) {
        return
      }
      this.reset()
      const id = row.courseId || this.ids[0]
      getCourse(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改课程管理'
      })
    },
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
    handleDelete(row) {
      if (!this.canManageCourse) {
        return
      }
      const ids = row.courseId || this.ids
      this.$modal.confirm('是否确认删除课程管理编号为"' + ids + '"的数据项？').then(() => {
        return delCourse(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      if (!this.canEducationAction('course', 'export')) {
        return
      }
      this.download('education/course/export', {
        ...this.queryParams
      }, 'course_' + new Date().getTime() + '.xlsx')
    },
    /** 切换学期时，自动填充选课时间（开学前15天～后15天） */
    onTermChange(termId) {
      this.form.selectStartTime = null
      this.form.selectEndTime = null
      if (!termId) return
      this.fillCourseSelectTimes(termId)
    },
    /** 根据学期ID从学期选项中取开学日期，自动填充选课时间 */
    fillCourseSelectTimes(termId) {
      const termOptions = this.educationOptions.termOptions || []
      const termItem = termOptions.find(t => String(t.value) === String(termId))
      if (!termItem || !termItem.startDate) return
      const startDateStr = termItem.startDate
      const startDate = new Date(startDateStr)
      const pad = n => String(n).padStart(2, '0')
      const fmt = d => d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate())
      const before = new Date(startDate)
      before.setDate(before.getDate() - 15)
      const after = new Date(startDate)
      after.setDate(after.getDate() + 15)
      this.form.selectStartTime = fmt(before) + ' 00:00:00'
      this.form.selectEndTime = fmt(after) + ' 23:59:59'
    },
    /** 初始化班级卡片数组 */
    initClassCards() {
      const count = this.form.classCount || 1
      this.classCards = []
      for (let i = 0; i < count; i++) {
        this.classCards.push({
          classNo: String(i + 1).padStart(2, '0'),
          teacherUserId: null,
          capacity: 30,
          teacherOptions: [],
          teacherLoading: false
        })
      }
      // 预加载每个卡片的教师选项
      this.classCards.forEach(card => {
        this.searchTeacherForCard('', card)
      })
    },
    /** 班级数量变化时，重新生成班级卡片 */
    onClassCountChange(count) {
      this.initClassCards()
    },
    /** 班级卡片中搜索教师（远程搜索，已按学院过滤） */
    searchTeacherForCard(query, card) {
      card.teacherLoading = true
      searchTeacher(query || '').then(response => {
        card.teacherOptions = response.data || []
        card.teacherLoading = false
      }).catch(() => {
        card.teacherLoading = false
      })
    },
    /** 提交表单：单个新增或批量新增（循环调用授课安排） */
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return

        // 校验：多个班时每个班必须有教师
        if (this.form.classCount > 1) {
          const invalidCard = this.classCards.find(card => !card.teacherUserId)
          if (invalidCard) {
            this.$modal.msgError('请为所有课堂选择授课教师')
            return
          }
        }

        if (this.form.courseId != null) {
          // 修改模式
          updateCourse(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          // 新增模式
          if (this.form.classCount > 1) {
            // 批量新增：两步走
            // 第一步：循环调用 addCourse，获得每个班的 courseId
            const coursePromises = this.classCards.map(card => {
              const formData = { ...this.form }
              formData.classNo = card.classNo
              formData.capacity = card.capacity
              return addCourse(formData).then(res => {
                return { card, courseId: res.data }
              })
            })

            Promise.all(coursePromises).then(results => {
              // 第二步：循环调用 addCourseTeacher，为每个班建授课关联
              const teacherPromises = results.map(({ card, courseId }) => {
                return addCourseTeacher({
                  courseId: courseId,
                  teacherUserId: card.teacherUserId,
                  teacherRole: '1',
                  isOwner: '1',
                  orderNum: 1,
                  status: '0'
                })
              })
              return Promise.all(teacherPromises)
            }).then(() => {
              this.$modal.msgSuccess('批量新增成功')
              this.open = false
              this.getList()
            }).catch(err => {
              this.$modal.msgError(err.msg || '批量新增失败')
            })
          } else {
            // 单个新增：同样两步走
            const formData = { ...this.form }
            const card = this.classCards[0] || {}
            if (card.capacity) {
              formData.capacity = card.capacity
            }
            addCourse(formData).then(res => {
              const courseId = res.data
              if (card.teacherUserId) {
                return addCourseTeacher({
                  courseId: courseId,
                  teacherUserId: card.teacherUserId,
                  teacherRole: '1',
                  isOwner: '1',
                  orderNum: 1,
                  status: '0'
                }).then(() => {
                  this.$modal.msgSuccess('新增成功')
                  this.open = false
                  this.getList()
                })
              } else {
                this.$modal.msgSuccess('新增成功')
                this.open = false
                this.getList()
              }
            }).catch(err => {
              this.$modal.msgError(err.msg || '新增失败')
            })
          }
        }
      })
    }
  }
}
</script>
<style scoped lang="scss">
/* 课程封面上传框尺寸从185x185缩小到160x160 */
.course-cover-upload ::v-deep .el-upload--picture-card {
  width: 80px;
  height: 80px;
  line-height: 80px;
}
.course-cover-upload ::v-deep .el-upload-list__item {
  width: 80px;
  height: 80px;
}
/* 班级卡片样式 */
.class-card {
  margin-bottom: 16px;
  .card-header {
    font-weight: bold;
    color: #303133;
  }
}
.mt16 {
  margin-top: 16px;
}
</style>
