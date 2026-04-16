import request from '@/utils/request'

export function listResource(query) {
  return request({
    url: '/education/resource/list',
    method: 'get',
    params: query
  })
}

export function getResource(resourceId) {
  return request({
    url: '/education/resource/' + resourceId,
    method: 'get'
  })
}

export function addResource(data) {
  return request({
    url: '/education/resource',
    method: 'post',
    data: data
  })
}

export function updateResource(data) {
  return request({
    url: '/education/resource',
    method: 'put',
    data: data
  })
}

export function delResource(resourceId) {
  return request({
    url: '/education/resource/' + resourceId,
    method: 'delete'
  })
}
