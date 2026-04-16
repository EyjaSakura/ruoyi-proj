<template>
  <div v-loading="loading" class="app-container">
    <el-row :gutter="16" class="mb16">
      <el-col v-for="card in cards" :key="card.key" :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">{{ card.title }}</div>
          <div class="stat-value">{{ summary[card.key] || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="mb16">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">整体完成情况</div>
          <div class="progress-item">
            <div class="progress-label">作业批改完成率</div>
            <el-progress :percentage="Number(summary.reviewRate || 0)" />
          </div>
          <div class="progress-item">
            <div class="progress-label">课程平均完成度</div>
            <el-progress :percentage="Number(summary.avgProgress || 0)" status="success" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">教师工作量</div>
          <el-table :data="overview.teacherWorkload" size="small">
            <el-table-column label="教师" prop="teacherName" />
            <el-table-column label="授课数" prop="courseCount" align="center" />
            <el-table-column label="作业数" prop="assignmentCount" align="center" />
            <el-table-column label="已发布" prop="publishedAssignmentCount" align="center" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">课程完成概览</div>
          <el-table :data="overview.courseStats" size="small">
            <el-table-column label="课程" prop="courseName" />
            <el-table-column label="学期" prop="termName" />
            <el-table-column label="选课人数" prop="selectedCount" align="center" />
            <el-table-column label="平均进度" prop="avgProgress" align="center">
              <template slot-scope="scope">
                <span>{{ scope.row.avgProgress }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="已完成" prop="finishedCount" align="center" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">最近截止作业</div>
          <el-table :data="overview.deadlineAssignments" size="small">
            <el-table-column label="课程" prop="courseName" />
            <el-table-column label="作业标题" prop="title" />
            <el-table-column label="截止时间" prop="deadlineTime" width="170">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.deadlineTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="待批改" prop="pendingReviewCount" align="center" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getStatisticsOverview } from '@/api/education/statistics'
import educationActionAuth from '@/mixins/educationActionAuth'

export default {
  name: 'EducationStatistics',
  mixins: [educationActionAuth],
  data() {
    return {
      loading: false,
      summary: {},
      overview: {
        courseStats: [],
        teacherWorkload: [],
        deadlineAssignments: []
      },
      cards: [
        { key: 'courseCount', title: '课程总数' },
        { key: 'teacherCount', title: '教师人数' },
        { key: 'studentCount', title: '学生人数' },
        { key: 'enrollmentCount', title: '有效选课' }
      ]
    }
  },
  created() {
    if (this.canEducationAction('statistics', 'query')) {
      this.getOverview()
    }
  },
  methods: {
    getOverview() {
      this.loading = true
      getStatisticsOverview().then(response => {
        const data = response.data || {}
        this.summary = data.summary || {}
        this.overview.courseStats = data.courseStats || []
        this.overview.teacherWorkload = data.teacherWorkload || []
        this.overview.deadlineAssignments = data.deadlineAssignments || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
.mb16 {
  margin-bottom: 16px;
}

.stat-card {
  min-height: 112px;
}

.stat-title {
  color: #606266;
  font-size: 14px;
}

.stat-value {
  margin-top: 18px;
  font-size: 30px;
  font-weight: 600;
  color: #303133;
}

.progress-item + .progress-item {
  margin-top: 24px;
}

.progress-label {
  margin-bottom: 8px;
  color: #606266;
}
</style>
