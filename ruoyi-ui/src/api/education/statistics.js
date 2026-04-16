import request from '@/utils/request'

export function getStatisticsOverview() {
  return request({
    url: '/education/statistics/overview',
    method: 'get'
  })
}
