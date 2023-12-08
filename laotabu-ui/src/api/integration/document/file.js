import request from '@/utils/request'

// 查询文件信息列表
export function listFile(query) {
  return request({
    url: '/integration/file/list',
    method: 'get',
    params: query
  })
}

// 查询文件信息详细
export function getFile(id) {
  return request({
    url: '/integration/file/' + id,
    method: 'get'
  })
}

// 新增文件信息
export function addFile(data) {
  return request({
    url: '/integration/file',
    method: 'post',
    data: data
  })
}

// 修改文件信息
export function updateFile(data) {
  return request({
    url: '/integration/file',
    method: 'put',
    data: data
  })
}

// 删除文件信息
export function delFile(id) {
  return request({
    url: '/integration/file/' + id,
    method: 'delete'
  })
}
