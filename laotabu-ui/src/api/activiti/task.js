import request from '@/utils/request'

// 查询task列表
export function listTask(query) {
  return request({
    url: '/task/list',
    method: 'get',
    params: query
  })
}

// 查询表单
export function formDataShow(taskID) {
  return request({
    url: '/task/formDataShow/'+taskID,
    method: 'get',
  })
}

// 查询表单
export function dynamicformDataSave(taskID,data) {
  return request({
    url: '/task/formDataSave/'+taskID,
    method: 'post',
    data:data
  })
}
// 定制表单【assets】
export function customizedformDataSave(type,res,taskID,data) {
  return request({
    url: '/task/customizedformDataSave/'+type + '/' +res+'/'+taskID,
    method: 'post',
    data:data
  })
}


