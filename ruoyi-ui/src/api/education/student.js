import request from '@/utils/request'

export function listStudent(query) {
  return request({
    url: '/education/student/list',
    method: 'get',
    params: query
  })
}

export function getStudent(studentId) {
  return request({
    url: '/education/student/' + studentId,
    method: 'get'
  })
}

export function addStudent(data) {
  return request({
    url: '/education/student',
    method: 'post',
    data: data
  })
}

export function updateStudent(data) {
  return request({
    url: '/education/student',
    method: 'put',
    data: data
  })
}

export function delStudent(studentId) {
  return request({
    url: '/education/student/' + studentId,
    method: 'delete'
  })
}
