import request from '@/utils/request'


// 查询用户代码列表
export function getFNumberIdOptions() {
  return request({
    url: '/integration/international/arrivalArea/getFNumberIdOptions',
    // headers: {
    //   isToken: false
    // },
    method: 'get',
    timeout: 20000
  })
}
// 获取国家区域列表
export function getCountryOptions(customerCode) {
  return request({
    url: '/integration/international/arrivalArea/getCountryOptions/' + customerCode,
    // headers: {
    //   isToken: false
    // },
    method: 'get',
    timeout: 20000
  })
}
// 提交表单数据
export function addArrivalAreaData(data) {
  return request({
    url: '/integration/international/arrivalArea/allocateArrivalArea' ,
    method: 'post',
    data: data
  })
}

// 查询运抵区域信息列表
export function listCountryArea(query) {
  return request({
    url: '/integration/international/arrivalArea/getCountryAreaList',
    method: 'get',
    params: query
  })
}
// 查询运抵区域信息详细
export function getCountryArea(countryNameEN) {
  return request({
    url: '/integration/international/arrivalArea/getCountryArea/' + countryNameEN,
    method: 'get'
  })
}
// 更新运抵区域分组id
export function updateCountryAreaGroupId(data){
  return request({
    url: '/integration/international/arrivalArea/updateCountryAreaGroupId',
    method: 'put',
    data: data
  })
}

// 新增运抵区域
export function addCountryArea(data){
  return request({
    url: '/integration/international/arrivalArea/addCountryArea',
    method: 'put',
    data: data
  })
}
// 删除运抵区域
export function deleteCountryArea(countryNameENs) {
  return request({
    url: '/integration/international/arrivalArea/removeCountryArea/' + countryNameENs,
    method: 'delete'
  })
}
