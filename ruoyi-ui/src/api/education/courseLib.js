import request from '@/utils/request'

// 查询课程库列表
export function listCourse(query) {
  return request({
    url: '/education/courseLib/list',
    method: 'get',
    params: query
  })
}

// 查询课程库详细
export function getCourse(courseId) {
  return request({
    url: '/education/courseLib/' + courseId,
    method: 'get'
  })
}

// 新增课程库
export function addCourse(data) {
  return request({
    url: '/education/courseLib',
    method: 'post',
    data: data
  })
}

// 修改课程库
export function updateCourse(data) {
  return request({
    url: '/education/courseLib',
    method: 'put',
    data: data
  })
}

// 删除课程库
export function delCourse(courseId) {
  return request({
    url: '/education/courseLib/' + courseId,
    method: 'delete'
  })
}

// 导出课程库
export function exportCourse(query) {
  return request({
    url: '/education/courseLib/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

// 下载导入模板
export function importTemplate() {
  return request({
    url: '/education/courseLib/importTemplate',
    method: 'get',
    responseType: 'blob'
  })
}
