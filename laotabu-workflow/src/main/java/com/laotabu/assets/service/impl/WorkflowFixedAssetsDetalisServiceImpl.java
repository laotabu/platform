package com.laotabu.assets.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.laotabu.assets.domain.WorkflowUnit;
import com.laotabu.common.core.domain.entity.SysDept;
import com.laotabu.common.utils.StringUtils;
import com.laotabu.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laotabu.assets.mapper.WorkflowFixedAssetsDetalisMapper;
import com.laotabu.assets.domain.WorkflowFixedAssetsDetalis;
import com.laotabu.assets.service.IWorkflowFixedAssetsDetalisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
/**
 * 固资申请细项Service业务层处理
 * 
 * @author lsd
 * @date 2023-11-15
 */
@Service
public class WorkflowFixedAssetsDetalisServiceImpl extends ServiceImpl<WorkflowFixedAssetsDetalisMapper, WorkflowFixedAssetsDetalis> implements IWorkflowFixedAssetsDetalisService
{
    @Autowired
    private WorkflowFixedAssetsDetalisMapper workflowFixedAssetsDetalisMapper;
    @Autowired
    private ISysDeptService sysDeptService;
    /**
     * 查询固资申请细项
     * 
     * @param id 固资申请细项主键
     * @return 固资申请细项
     */
    @Override
    public WorkflowFixedAssetsDetalis selectWorkflowFixedAssetsDetalisById(Long id)
    {
        return workflowFixedAssetsDetalisMapper.selectWorkflowFixedAssetsDetalisById(id);
    }

    /**
     * 查询固资申请细项列表
     * 
     * @param workflowFixedAssetsDetalis 固资申请细项
     * @return 固资申请细项
     */
    @Override
    public List<WorkflowFixedAssetsDetalis> selectWorkflowFixedAssetsDetalisList(WorkflowFixedAssetsDetalis workflowFixedAssetsDetalis)
    {
        return workflowFixedAssetsDetalisMapper.selectWorkflowFixedAssetsDetalisList(workflowFixedAssetsDetalis);
    }

    /**
     * 新增固资申请细项
     * 
     * @param workflowFixedAssetsDetalis 固资申请细项
     * @return 结果
     */
    @Override
    public int insertWorkflowFixedAssetsDetalis(WorkflowFixedAssetsDetalis workflowFixedAssetsDetalis)
    {
        return workflowFixedAssetsDetalisMapper.insertWorkflowFixedAssetsDetalis(workflowFixedAssetsDetalis);
    }

    /**
     * 修改固资申请细项
     * 
     * @param workflowFixedAssetsDetalis 固资申请细项
     * @return 结果
     */
    @Override
    public int updateWorkflowFixedAssetsDetalis(WorkflowFixedAssetsDetalis workflowFixedAssetsDetalis)
    {
        return workflowFixedAssetsDetalisMapper.updateWorkflowFixedAssetsDetalis(workflowFixedAssetsDetalis);
    }

    /**
     * 批量删除固资申请细项
     * 
     * @param ids 需要删除的固资申请细项主键
     * @return 结果
     */
    @Override
    public int deleteWorkflowFixedAssetsDetalisByIds(Long[] ids)
    {
        return workflowFixedAssetsDetalisMapper.deleteWorkflowFixedAssetsDetalisByIds(ids);
    }

    /**
     * 删除固资申请细项信息
     * 
     * @param id 固资申请细项主键
     * @return 结果
     */
    @Override
    public int deleteWorkflowFixedAssetsDetalisById(Long id)
    {
        return workflowFixedAssetsDetalisMapper.deleteWorkflowFixedAssetsDetalisById(id);
    }

    /**
     * 获取单位表信息
     *
     * @return
     */
    @Override
    public List<WorkflowUnit> getUnits() {
        return workflowFixedAssetsDetalisMapper.getUnits();
    }

    @Override
    public List<WorkflowFixedAssetsDetalis> getAssetsDetalisInfoByIds(Long[] ids) {
        List<WorkflowFixedAssetsDetalis> workflowFixedAssetsDetalis = workflowFixedAssetsDetalisMapper.selectList(new LambdaQueryWrapper<WorkflowFixedAssetsDetalis>().in(WorkflowFixedAssetsDetalis::getId, ids));
        Map<Long, Object> map = Optional.ofNullable(workflowFixedAssetsDetalis).orElse(new LinkedList<>())
                .stream()
                .collect(Collectors.toMap(WorkflowFixedAssetsDetalis::getPlaceOfStorage,
                        WorkflowFixedAssetsDetalis::getId,
                        (v1,v2)-> v1));
        List<SysDept> deps = sysDeptService.selectDeptListByIds(map.keySet());
        if (deps.size() > 0) {
            deps.stream().forEach(e -> {
                map.put(e.getDeptId(), e.getDeptName());
            });
            Optional.ofNullable(workflowFixedAssetsDetalis).orElse(new LinkedList<>()).stream().forEach(e -> {
                e.setPlaceOfStorageName(map.get(e.getPlaceOfStorage()).toString());
            });
        }
        return workflowFixedAssetsDetalis;
    }
}
