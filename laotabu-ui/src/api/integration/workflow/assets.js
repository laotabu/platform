import request from '@/utils/request'

// 查询固资申请头列表
export function listAssets(query) {
  return request({
    url: '/workflow/assets/list',
    method: 'get',
    params: query
  })
}

// 查询固资申请头详细
export function getAssets(id) {
  return request({
    url: '/workflow/assets/' + id,
    method: 'get'
  })
}

// 新增固资申请头
export function addAssets(data) {
  return request({
    url: '/workflow/assets',
    method: 'post',
    data: data
  })
}

// 修改固资申请头
export function updateAssets(data) {
  return request({
    url: '/workflow/assets',
    method: 'put',
    data: data
  })
}

// 删除固资申请头
export function delAssets(id) {
  return request({
    url: '/workflow/assets/' + id,
    method: 'delete'
  })
}

export function uploadAssetsFiles(file){
  return request({
    url: '/oos/workflow/upAssetsFile',
    method: 'post',
    data: file
  })
}

export function getAssetsInfoByBusinessKey(businessKey){
  return request({
    url: '/workflow/assets/bs/' + businessKey,
    method: 'get'
  })
}
export function getApplicantOptions(){
  return request({
    url: '/integration/international/arrivalArea/getCountryOptions/' + customerCode,
    // headers: {
    //   isToken: false
    // },
    method: 'get',
    timeout: 20000
  })
}

