import request from '@/utils/request'

export function listProgress(query) {
  return request({
    url: '/education/progress/list',
    method: 'get',
    params: query
  })
}

export function getProgress(progressId) {
  return request({
    url: '/education/progress/' + progressId,
    method: 'get'
  })
}

export function addProgress(data) {
  return request({
    url: '/education/progress',
    method: 'post',
    data: data
  })
}

export function updateProgress(data) {
  return request({
    url: '/education/progress',
    method: 'put',
    data: data
  })
}

export function delProgress(progressId) {
  return request({
    url: '/education/progress/' + progressId,
    method: 'delete'
  })
}
