import request from '@/utils/request'

export function listAssignment(query) {
  return request({
    url: '/education/assignment/list',
    method: 'get',
    params: query
  })
}

export function getAssignment(assignmentId) {
  return request({
    url: '/education/assignment/' + assignmentId,
    method: 'get'
  })
}

export function addAssignment(data) {
  return request({
    url: '/education/assignment',
    method: 'post',
    data: data
  })
}

export function updateAssignment(data) {
  return request({
    url: '/education/assignment',
    method: 'put',
    data: data
  })
}

export function delAssignment(assignmentId) {
  return request({
    url: '/education/assignment/' + assignmentId,
    method: 'delete'
  })
}
