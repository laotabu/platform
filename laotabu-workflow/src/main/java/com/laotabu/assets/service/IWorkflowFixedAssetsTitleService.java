package com.laotabu.assets.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.baomidou.mybatisplus.extension.service.IService;
import com.laotabu.assets.domain.WorkflowFixedAssetsTitle;

/**
 * 固资申请头Service接口
 * 
 * @author lsd
 * @date 2023-11-15
 */
public interface IWorkflowFixedAssetsTitleService extends IService<WorkflowFixedAssetsTitle>
{
    /**
     * 查询固资申请头
     * 
     * @param id 固资申请头主键
     * @return 固资申请头
     */
    public WorkflowFixedAssetsTitle selectWorkflowFixedAssetsTitleById(Long id);

    /**
     * 查询固资申请头列表
     * 
     * @param workflowFixedAssetsTitle 固资申请头
     * @return 固资申请头集合
     */
    public List<WorkflowFixedAssetsTitle> selectWorkflowFixedAssetsTitleList(WorkflowFixedAssetsTitle workflowFixedAssetsTitle);

    /**
     * 新增固资申请头
     * 
     * @param workflowFixedAssetsTitle 固资申请头
     * @return 结果
     */
    public int insertWorkflowFixedAssetsTitle(WorkflowFixedAssetsTitle workflowFixedAssetsTitle);

    /**
     * 修改固资申请头
     * 
     * @param workflowFixedAssetsTitle 固资申请头
     * @return 结果
     */
    public int updateWorkflowFixedAssetsTitle(WorkflowFixedAssetsTitle workflowFixedAssetsTitle);

    /**
     * 批量删除固资申请头
     * 
     * @param ids 需要删除的固资申请头主键集合
     * @return 结果
     */
    public int deleteWorkflowFixedAssetsTitleByIds(Long[] ids) throws ExecutionException, InterruptedException;

    /**
     * 删除固资申请头信息
     * 
     * @param id 固资申请头主键
     * @return 结果
     */
    public int deleteWorkflowFixedAssetsTitleById(Long id);

    /**
     * 插入固资申请【表头+表单细项】
     * @param workflowFixedAssetsTitle
     * @return
     */
    int insertWorkflowFixedAssets(WorkflowFixedAssetsTitle workflowFixedAssetsTitle) throws ExecutionException, InterruptedException;
}
