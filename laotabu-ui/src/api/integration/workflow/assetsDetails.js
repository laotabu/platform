import request from '@/utils/request'

export function getUnits() {
  return request({
    url: '/workflow/assetsDetalis/getUnits',
    method: 'get'
  })
}
export function getAssetsDetailsListByIds(id){
  return request({
    url: '/workflow/assetsDetalis/list/' + id,
    method: 'get'
  })
}
