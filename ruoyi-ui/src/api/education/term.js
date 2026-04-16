import request from '@/utils/request'

export function listTerm(query) {
  return request({
    url: '/education/term/list',
    method: 'get',
    params: query
  })
}

export function getTerm(termId) {
  return request({
    url: '/education/term/' + termId,
    method: 'get'
  })
}

export function addTerm(data) {
  return request({
    url: '/education/term',
    method: 'post',
    data: data
  })
}

export function updateTerm(data) {
  return request({
    url: '/education/term',
    method: 'put',
    data: data
  })
}

export function delTerm(termId) {
  return request({
    url: '/education/term/' + termId,
    method: 'delete'
  })
}
