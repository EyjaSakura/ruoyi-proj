import request from '@/utils/request'

export function listSubmissionAttachment(query) {
  return request({
    url: '/education/submissionAttachment/list',
    method: 'get',
    params: query
  })
}

export function getSubmissionAttachment(submissionAttachmentId) {
  return request({
    url: '/education/submissionAttachment/' + submissionAttachmentId,
    method: 'get'
  })
}

export function addSubmissionAttachment(data) {
  return request({
    url: '/education/submissionAttachment',
    method: 'post',
    data: data
  })
}

export function updateSubmissionAttachment(data) {
  return request({
    url: '/education/submissionAttachment',
    method: 'put',
    data: data
  })
}

export function delSubmissionAttachment(submissionAttachmentId) {
  return request({
    url: '/education/submissionAttachment/' + submissionAttachmentId,
    method: 'delete'
  })
}
