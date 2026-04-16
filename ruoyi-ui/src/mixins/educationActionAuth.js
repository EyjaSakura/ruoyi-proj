import { mapGetters } from 'vuex'

export default {
  computed: {
    ...mapGetters(['roles'])
  },
  methods: {
    hasEducationRole(roleKey) {
      return (this.roles || []).includes(roleKey)
    },
    canEducationAction(module, action) {
      if (this.hasEducationRole('admin')) {
        return true
      }
      if (this.hasEducationRole('master')) {
        return module !== 'statistics' || action === 'query'
      }
      if (this.hasEducationRole('teacher')) {
        return this.canTeacherAction(module, action)
      }
      if (this.hasEducationRole('student')) {
        return this.canStudentAction(module, action)
      }
      return false
    },
    canTeacherAction(module, action) {
      if (module === 'statistics') {
        return action === 'query'
      }
      if (module === 'teacher') {
        return ['query', 'edit'].includes(action)
      }
      if (module === 'course') {
        return ['query', 'export', 'add'].includes(action)
      }
      if (['student', 'courseTeacher', 'enrollment', 'progress', 'term'].includes(module)) {
        return ['query', 'export'].includes(action)
      }
      if (module === 'submission') {
        return ['query', 'export', 'edit'].includes(action)
      }
      if (module === 'submissionAttachment') {
        return ['query', 'export'].includes(action)
      }
      if (['chapter', 'resource', 'assignment', 'assignmentAttachment', 'notice', 'message'].includes(module)) {
        return ['query', 'export', 'add', 'edit', 'remove'].includes(action)
      }
      return false
    },
    canStudentAction(module, action) {
      if (module === 'student') {
        return ['query', 'edit'].includes(action)
      }
      if (module === 'course') {
        return action === 'query'
      }
      if (['enrollment', 'submission', 'submissionAttachment'].includes(module)) {
        return ['query', 'add', 'edit', 'remove'].includes(action)
      }
      if (module === 'message') {
        return ['query', 'edit'].includes(action)
      }
      if (['resource', 'assignment', 'assignmentAttachment', 'notice', 'progress'].includes(module)) {
        return action === 'query'
      }
      return false
    }
  }
}
