import request from '@/utils/request'

export function listMessage(query) {
  return request({
    url: '/education/message/list',
    method: 'get',
    params: query
  })
}

export function getMessage(messageId) {
  return request({
    url: '/education/message/' + messageId,
    method: 'get'
  })
}

export function addMessage(data) {
  return request({
    url: '/education/message',
    method: 'post',
    data: data
  })
}

export function updateMessage(data) {
  return request({
    url: '/education/message',
    method: 'put',
    data: data
  })
}

export function delMessage(messageId) {
  return request({
    url: '/education/message/' + messageId,
    method: 'delete'
  })
}
