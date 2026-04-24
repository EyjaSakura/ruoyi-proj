import { getOptionData } from '@/api/education/common'

function defaultEducationOptions() {
  return {
    deptOptions: [],
    termOptions: [],
    courseOptions: [],
    classroomCourseOptions: [],
    teacherUserOptions: [],
    studentUserOptions: [],
    userOptions: [],
    assignmentOptions: [],
    chapterOptions: [],
    classroomChapterOptions: [],
    noticeOptions: [],
    submissionOptions: []
  }
}

function findOptionLabel(options, value) {
  for (const item of options) {
    if (String(item.value !== undefined ? item.value : item.id) === String(value)) {
      return item.label
    }
    if (item.children && item.children.length) {
      const childLabel = findOptionLabel(item.children, value)
      if (childLabel) {
        return childLabel
      }
    }
  }
  return ''
}

export default {
  data() {
    return {
      educationOptions: defaultEducationOptions()
    }
  },
  methods: {
    loadEducationOptions() {
      return getOptionData().then(response => {
        const optionData = response.data && typeof response.data === 'object' ? response.data : response
        this.educationOptions = Object.assign(defaultEducationOptions(), optionData || {})
      })
    },
    getEducationOptionLabel(optionKey, value) {
      if (value === null || value === undefined || value === '') {
        return ''
      }
      return findOptionLabel(this.educationOptions[optionKey] || [], value) || value
    }
  }
}
