import request from '@/utils/request'

export function listEnrollment(query) {
  return request({
    url: '/education/enrollment/list',
    method: 'get',
    params: query
  })
}

export function getEnrollment(enrollmentId) {
  return request({
    url: '/education/enrollment/' + enrollmentId,
    method: 'get'
  })
}

export function addEnrollment(data) {
  return request({
    url: '/education/enrollment',
    method: 'post',
    data: data
  })
}

export function updateEnrollment(data) {
  return request({
    url: '/education/enrollment',
    method: 'put',
    data: data
  })
}

export function delEnrollment(enrollmentId) {
  return request({
    url: '/education/enrollment/' + enrollmentId,
    method: 'delete'
  })
}
