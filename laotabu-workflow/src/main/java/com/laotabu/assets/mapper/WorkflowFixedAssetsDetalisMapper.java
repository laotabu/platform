package com.laotabu.assets.mapper;

import java.util.List;
import java.util.Map;

import com.laotabu.assets.domain.WorkflowFixedAssetsDetalis;
import com.laotabu.assets.domain.WorkflowUnit;
import com.laotabu.common.core.mapper.BaseMapperPlus;

/**
 * 固资申请细项Mapper接口
 * 
 * @author lsd
 * @date 2023-11-15
 */
public interface WorkflowFixedAssetsDetalisMapper extends BaseMapperPlus<WorkflowFixedAssetsDetalisMapper, WorkflowFixedAssetsDetalis, WorkflowFixedAssetsDetalis>
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
     * 删除固资申请细项
     * 
     * @param id 固资申请细项主键
     * @return 结果
     */
    public int deleteWorkflowFixedAssetsDetalisById(Long id);

    /**
     * 批量删除固资申请细项
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWorkflowFixedAssetsDetalisByIds(Long[] ids);


    /**
     * 获取单位表信息
     *
     * @return
     */
    public List<WorkflowUnit> getUnits();

}
