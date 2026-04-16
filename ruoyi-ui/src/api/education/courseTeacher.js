import request from '@/utils/request'

export function listCourseTeacher(query) {
  return request({
    url: '/education/courseTeacher/list',
    method: 'get',
    params: query
  })
}

export function getCourseTeacher(courseTeacherId) {
  return request({
    url: '/education/courseTeacher/' + courseTeacherId,
    method: 'get'
  })
}

export function addCourseTeacher(data) {
  return request({
    url: '/education/courseTeacher',
    method: 'post',
    data: data
  })
}

export function updateCourseTeacher(data) {
  return request({
    url: '/education/courseTeacher',
    method: 'put',
    data: data
  })
}

export function delCourseTeacher(courseTeacherId) {
  return request({
    url: '/education/courseTeacher/' + courseTeacherId,
    method: 'delete'
  })
}

// 搜索课程（用于下拉框实时搜索）
export function searchCourse(keyword) {
  return request({
    url: '/education/course/search',
    method: 'get',
    params: { keyword }
  })
}

// 搜索教师用户（用于下拉框实时搜索）
export function searchTeacher(keyword) {
  return request({
    url: '/education/common/searchTeacher',
    method: 'get',
    params: { keyword }
  })
}
