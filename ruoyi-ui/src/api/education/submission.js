import request from '@/utils/request'

export function listSubmission(query) {
  return request({
    url: '/education/submission/list',
    method: 'get',
    params: query
  })
}

export function getSubmission(submissionId) {
  return request({
    url: '/education/submission/' + submissionId,
    method: 'get'
  })
}

export function addSubmission(data) {
  return request({
    url: '/education/submission',
    method: 'post',
    data: data
  })
}

export function updateSubmission(data) {
  return request({
    url: '/education/submission',
    method: 'put',
    data: data
  })
}

export function delSubmission(submissionId) {
  return request({
    url: '/education/submission/' + submissionId,
    method: 'delete'
  })
}
