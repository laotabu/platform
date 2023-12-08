package com.laotabu.assets.service;

import java.util.List;
import java.util.Map;

import com.laotabu.assets.domain.WorkflowFixedAssetsDetalis;
import com.baomidou.mybatisplus.extension.service.IService;
import com.laotabu.assets.domain.WorkflowUnit;

/**
 * 固资申请细项Service接口
 * 
 * @author lsd
 * @date 2023-11-15
 */
public interface IWorkflowFixedAssetsDetalisService extends IService<WorkflowFixedAssetsDetalis>
{
    /**
     * 查询固资申请细项
     * 
     * @param id 固资申请细项主键
     * @return 固资申请细项
     */
    public WorkflowFixedAssetsDetalis selectWorkflowFixedAssetsDetalisById(Long id);

    /**
     * 查询固资申请细项列表
     * 
     * @param workflowFixedAssetsDetalis 固资申请细项
     * @return 固资申请细项集合
     */
    public List<WorkflowFixedAssetsDetalis> selectWorkflowFixedAssetsDetalisList(WorkflowFixedAssetsDetalis workflowFixedAssetsDetalis);

    /**
     * 新增固资申请细项
     * 
     * @param workflowFixedAssetsDetalis 固资申请细项
     * @return 结果
     */
    public int insertWorkflowFixedAssetsDetalis(WorkflowFixedAssetsDetalis workflowFixedAssetsDetalis);

    /**
     * 修改固资申请细项
     * 
     * @param workflowFixedAssetsDetalis 固资申请细项
     * @return 结果
     */
    public int updateWorkflowFixedAssetsDetalis(WorkflowFixedAssetsDetalis workflowFixedAssetsDetalis);

    /**
     * 批量删除固资申请细项
     * 
     * @param ids 需要删除的固资申请细项主键集合
     * @return 结果
     */
    public int deleteWorkflowFixedAssetsDetalisByIds(Long[] ids);

    /**
     * 删除固资申请细项信息
     * 
     * @param id 固资申请细项主键
     * @return 结果
     */
    public int deleteWorkflowFixedAssetsDetalisById(Long id);

    /**
     * 获取单位表信息
     *
     * @return
     */
    List<WorkflowUnit> getUnits();

    List<WorkflowFixedAssetsDetalis> getAssetsDetalisInfoByIds(Long[] ids);

}
