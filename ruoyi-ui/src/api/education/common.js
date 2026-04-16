import request from '@/utils/request'

// 查询教学管理院系树
export function deptTreeSelect() {
  return request({
    url: '/education/common/deptTree',
    method: 'get'
  })
}

// 查询教学管理通用选项
export function getOptionData() {
  return request({
    url: '/education/common/options',
    method: 'get'
  })
}
