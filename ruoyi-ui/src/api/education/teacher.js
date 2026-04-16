import request from '@/utils/request'

export function listTeacher(query) {
  return request({
    url: '/education/teacher/list',
    method: 'get',
    params: query
  })
}

export function getTeacher(teacherId) {
  return request({
    url: '/education/teacher/' + teacherId,
    method: 'get'
  })
}

export function addTeacher(data) {
  return request({
    url: '/education/teacher',
    method: 'post',
    data: data
  })
}

export function updateTeacher(data) {
  return request({
    url: '/education/teacher',
    method: 'put',
    data: data
  })
}

export function delTeacher(teacherId) {
  return request({
    url: '/education/teacher/' + teacherId,
    method: 'delete'
  })
}
