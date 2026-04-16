import request from '@/utils/request'

export function listChapter(query) {
  return request({
    url: '/education/chapter/list',
    method: 'get',
    params: query
  })
}

export function getChapter(chapterId) {
  return request({
    url: '/education/chapter/' + chapterId,
    method: 'get'
  })
}

export function addChapter(data) {
  return request({
    url: '/education/chapter',
    method: 'post',
    data: data
  })
}

export function updateChapter(data) {
  return request({
    url: '/education/chapter',
    method: 'put',
    data: data
  })
}

export function delChapter(chapterId) {
  return request({
    url: '/education/chapter/' + chapterId,
    method: 'delete'
  })
}
