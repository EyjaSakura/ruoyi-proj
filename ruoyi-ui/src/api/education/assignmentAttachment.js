import request from '@/utils/request'

export function listAssignmentAttachment(query) {
  return request({
    url: '/education/assignmentAttachment/list',
    method: 'get',
    params: query
  })
}

export function getAssignmentAttachment(assignmentAttachmentId) {
  return request({
    url: '/education/assignmentAttachment/' + assignmentAttachmentId,
    method: 'get'
  })
}

export function addAssignmentAttachment(data) {
  return request({
    url: '/education/assignmentAttachment',
    method: 'post',
    data: data
  })
}

export function updateAssignmentAttachment(data) {
  return request({
    url: '/education/assignmentAttachment',
    method: 'put',
    data: data
  })
}

export function delAssignmentAttachment(assignmentAttachmentId) {
  return request({
    url: '/education/assignmentAttachment/' + assignmentAttachmentId,
    method: 'delete'
  })
}
