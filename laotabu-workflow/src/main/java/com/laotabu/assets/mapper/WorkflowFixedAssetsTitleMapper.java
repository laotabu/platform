package com.laotabu.assets.mapper;

import java.util.List;
import com.laotabu.assets.domain.WorkflowFixedAssetsTitle;
import com.laotabu.common.core.mapper.BaseMapperPlus;

/**
 * 固资申请头Mapper接口
 * 
 * @author lsd
 * @date 2023-11-15
 */
public interface WorkflowFixedAssetsTitleMapper extends BaseMapperPlus<WorkflowFixedAssetsTitleMapper, WorkflowFixedAssetsTitle, WorkflowFixedAssetsTitle> {
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
     * 删除固资申请头
     * 
     * @param id 固资申请头主键
     * @return 结果
     */
    public int deleteWorkflowFixedAssetsTitleById(Long id);

    /**
     * 批量删除固资申请头
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWorkflowFixedAssetsTitleByIds(Long[] ids);
}
