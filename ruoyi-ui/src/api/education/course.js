import request from '@/utils/request'

export function listCourse(query) {
  return request({
    url: '/education/course/list',
    method: 'get',
    params: query
  })
}

export function getCourse(courseId) {
  return request({
    url: '/education/course/' + courseId,
    method: 'get'
  })
}

export function addCourse(data) {
  return request({
    url: '/education/course',
    method: 'post',
    data: data
  })
}

export function updateCourse(data) {
  return request({
    url: '/education/course',
    method: 'put',
    data: data
  })
}

export function delCourse(courseId) {
  return request({
    url: '/education/course/' + courseId,
    method: 'delete'
  })
}

/**
 * 新增授课安排（供批量新增课堂时循环调用）
 * @param data { courseId, teacherUserId }
 */
export function addCourseTeacher(data) {
  return request({
    url: '/education/courseTeacher',
    method: 'post',
    data: data
  })
}
