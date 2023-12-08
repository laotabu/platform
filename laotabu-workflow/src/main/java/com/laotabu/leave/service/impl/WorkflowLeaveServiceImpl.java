package com.laotabu.leave.service.impl;

import java.util.List;

import java.util.stream.Collectors;


import com.laotabu.common.constant.WorkflowKeyConstant;
import com.laotabu.common.core.domain.entity.SysDept;
import com.laotabu.common.utils.DateUtils;
import com.laotabu.common.utils.SecurityUtils;
import com.laotabu.common.utils.StringUtils;
import com.laotabu.common.utils.uuid.UUID;
import com.laotabu.leave.domain.WorkflowLeave;
import com.laotabu.leave.mapper.WorkflowLeaveMapper;
import com.laotabu.leave.service.IWorkflowLeaveService;
import com.laotabu.system.service.ISysDeptService;
import com.laotabu.system.service.ISysUserService;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 请假Service业务层处理
 *
 * @author danny
 * @date 2020-10-28
 */
@Service
public class WorkflowLeaveServiceImpl implements IWorkflowLeaveService {

    @Autowired
    private WorkflowLeaveMapper workflowLeaveMapper;
    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ISysDeptService sysDeptService;

    /**
     * 查询请假
     *
     * @param id 请假ID
     * @return 请假
     */
    @Override
    public WorkflowLeave selectWorkflowLeaveById(String id) {
        return workflowLeaveMapper.selectWorkflowLeaveById(id);
    }

    /**
     * 查询请假列表
     *
     * @param workflowLeave 请假
     * @return 请假
     */
    @Override
    public List<WorkflowLeave> selectWorkflowLeaveList(WorkflowLeave workflowLeave) {
        return workflowLeaveMapper.selectWorkflowLeaveListByWorkflowLeaveAndDeptId(workflowLeave, SecurityUtils.getLoginUser().getUser().getDeptId());
    }
    /**
     * 查询请假列表带任务状态
     *
     * @param workflowLeave 请假
     * @return 请假
     */
    @Override
    public List<WorkflowLeave> selectWorkflowLeaveAndTaskNameList(WorkflowLeave workflowLeave) {
        List<WorkflowLeave> workflowLeaves = workflowLeaveMapper.selectWorkflowLeaveList(workflowLeave);
        List<String> collect = workflowLeaves.parallelStream().map(wl -> wl.getInstanceId()).collect(Collectors.toList());
        if(collect!=null&&!collect.isEmpty()) {
            List<Task> tasks = taskService.createTaskQuery().processInstanceIdIn(collect).list();
            workflowLeaves.forEach(
                    wl->{
                        Task task = tasks.parallelStream().filter(t -> t.getProcessInstanceId().equals(wl.getInstanceId())).findAny().orElse(null);
                        if (task != null) {
                            wl.setTaskName(task.getName());
                        }
                    }
            );
        }
        return workflowLeaves;
    }

    /**
     * 新增请假
     *
     * @param workflowLeave 请假
     * @return 结果
     */
    @Override
    public int insertWorkflowLeave(WorkflowLeave workflowLeave) {
        String id = UUID.randomUUID().toString();
        // 此id就是 business_id
        workflowLeave.setId(id);
        workflowLeave.setCreateTime(DateUtils.getNowDate());
        // 获取登录用户所在部门的管理员角色
        Long deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        String join = StringUtils.join(sysUserService.selectUserNameByPostCodeAndDeptId("se", deptId), ",");


        /**
         * 开启一个流程实例，此时会返回一个流程实例对象
         */
        // 主要操作了act_ru_execution 这张表
        ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                .start()
                // 跟流程定义做关联
                .withProcessDefinitionKey(WorkflowKeyConstant.LEAVE_KEY)
                .withName(workflowLeave.getTitle())
                // 跟业务做关联
                .withBusinessKey(id)
                //
                .withVariable("deptLeader",join)
                .build());

        workflowLeave.setInstanceId(processInstance.getId());
        workflowLeave.setState("0");
        workflowLeave.setCreateName(SecurityUtils.getUsername());
        workflowLeave.setCreateBy(SecurityUtils.getUsername());
        workflowLeave.setCreateTime(DateUtils.getNowDate());

        return workflowLeaveMapper.insertWorkflowLeave(workflowLeave);
    }

    /**
     * 修改请假
     *
     * @param workflowLeave 请假
     * @return 结果
     */
    @Override
    public int updateWorkflowLeave(WorkflowLeave workflowLeave) {
        workflowLeave.setUpdateTime(DateUtils.getNowDate());
        return workflowLeaveMapper.updateWorkflowLeave(workflowLeave);
    }

    /**
     * 批量删除请假
     *
     * @param ids 需要删除的请假ID
     * @return 结果
     */
    @Override
    public int deleteWorkflowLeaveByIds(String[] ids) {
        return workflowLeaveMapper.deleteWorkflowLeaveByIds(ids);
    }

    /**
     * 删除请假信息
     *
     * @param id 请假ID
     * @return 结果
     */
    @Override
    public int deleteWorkflowLeaveById(String id) {
        return workflowLeaveMapper.deleteWorkflowLeaveById(id);
    }

    @Override
    public WorkflowLeave selectWorkflowLeaveByInstanceId(String instanceId) {

        return workflowLeaveMapper.selectWorkflowLeaveByInstanceId(instanceId);
    }
}
