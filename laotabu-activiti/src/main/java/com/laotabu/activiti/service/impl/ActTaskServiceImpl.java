

package com.laotabu.activiti.service.impl;

import com.laotabu.activiti.domain.customer.CustomerWorkflowFixedAssets;
import com.laotabu.common.core.page.PageDomain;
import com.github.pagehelper.Page;
import com.laotabu.activiti.domain.ActWorkflowFormData;
import com.laotabu.activiti.domain.dto.ActWorkflowFormDataDTO;
import com.laotabu.activiti.domain.dto.ActTaskDTO;
import com.laotabu.activiti.service.IActTaskService;
import com.laotabu.activiti.service.IActWorkflowFormDataService;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActTaskServiceImpl implements IActTaskService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private IActWorkflowFormDataService actWorkflowFormDataService;


    @Override
    public Page<ActTaskDTO> selectProcessDefinitionList(PageDomain pageDomain) {
        Page<ActTaskDTO> list = new Page<ActTaskDTO>();
        org.activiti.api.runtime.shared.query.Page<Task> pageTasks = taskRuntime.tasks(
                Pageable.of((pageDomain.getPageNum() - 1) * pageDomain.getPageSize(), pageDomain.getPageSize()));
        List<Task> tasks = pageTasks.getContent();
        int totalItems = pageTasks.getTotalItems();
        list.setTotal(totalItems);
        if (totalItems != 0) {
            Set<String> processInstanceIdIds = tasks.parallelStream().map(t -> t.getProcessInstanceId()).collect(Collectors.toSet());
            List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceIdIds).list();
            List<ActTaskDTO> actTaskDTOS = tasks.stream()
                    .map(t -> new ActTaskDTO(t, processInstanceList.parallelStream().filter(pi -> t.getProcessInstanceId().equals(pi.getId())).findAny().get()))
                    .collect(Collectors.toList());
            list.addAll(actTaskDTOS);

        }
        list.stream().forEach(System.out::println);
        return list;
    }

    @Override
    public List<String> formDataShow(String taskID) {
        Task task = taskRuntime.task(taskID);
/*  ------------------------------------------------------------------------------
            FormProperty_0ueitp2--__!!类型--__!!名称--__!!是否参数--__!!默认值
            例子：
            FormProperty_0lovri0--__!!string--__!!姓名--__!!f--__!!同意!!__--驳回
            FormProperty_1iu6onu--__!!int--__!!年龄--__!!s

            默认值：无、字符常量、FormProperty_开头定义过的控件ID
            是否参数：f为不是参数，s是字符，t是时间(不需要int，因为这里int等价于string)
            注：类型是可以获取到的，但是为了统一配置原则，都配置到
            */
        //注意!!!!!!!!:表单Key必须要任务编号一模一样，因为参数需要任务key，但是无法获取，只能获取表单key“task.getFormKey()”当做任务key
        UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId())
                .getFlowElement(task.getFormKey());
        if (userTask == null) {
            return null;
        }
        List<FormProperty> formProperties = userTask.getFormProperties();
        List<String> collect = formProperties.stream().map(fp -> fp.getId()).collect(Collectors.toList());
        return collect;
    }

    @Override
    public int dynamicformDataSave(String taskID, List<ActWorkflowFormDataDTO> awfs) throws ParseException {
        // 根据taskId获取登录用户的任务
        // Get task by id if the authenticated user: - is the assignee
        // or - is in a group with is assigned to the task or - has admin role
        Task task = taskRuntime.task(taskID);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        Boolean hasVariables = false;//没有任何参数
        HashMap<String, Object> variables = new HashMap<String, Object>();
        //前端传来的字符串，拆分成每个控件
        List<ActWorkflowFormData> acwfds = new ArrayList<>();
        for (ActWorkflowFormDataDTO awf : awfs) {
            ActWorkflowFormData actWorkflowFormData = new ActWorkflowFormData(processInstance.getBusinessKey(),awf, task);
            acwfds.add(actWorkflowFormData);
            //构建参数集合
            if(!"f".equals(awf.getControlIsParam())) {
                    variables.put(awf.getControlId(), awf.getControlValue());
                    hasVariables = true;
            }
        }//for结束
        if (task.getAssignee() == null) {
            // 如果当前任务没有分配候选人，就登录用户认领【一般就是轮到自己的时候才会执行，其它环节都有审批人】
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
        }
        if (hasVariables) {
            //带参数完成任务
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskID)
                    .withVariables(variables)
                    .build());
        } else {
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskID)
                    .build());
        }


        //写入数据库
        return actWorkflowFormDataService.insertActWorkflowFormDatas(acwfds);
    }

    @Override
    public int assetsApprove(String taskID, CustomerWorkflowFixedAssets customerWorkflowFixedAssets) {
        Task task = taskRuntime.task(taskID);
//        System.out.println(customerWorkflowFixedAssets);
        System.out.println(customerWorkflowFixedAssets.getCustomerWorkflowFixedAssetsAuditData());
//        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
//        Boolean hasVariables = false;//没有任何参数
//        HashMap<String, Object> variables = new HashMap<String, Object>();
//        //前端传来的字符串，拆分成每个控件
//        List<ActWorkflowFormData> acwfds = new ArrayList<>();
//        for (ActWorkflowFormDataDTO awf : awfs) {
//            ActWorkflowFormData actWorkflowFormData = new ActWorkflowFormData(processInstance.getBusinessKey(),awf, task);
//            acwfds.add(actWorkflowFormData);
//            //构建参数集合
//            if(!"f".equals(awf.getControlIsParam())) {
//                variables.put(awf.getControlId(), awf.getControlValue());
//                hasVariables = true;
//            }
//        }
//        if (task.getAssignee() == null) {
//            // 任务认领
//            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
//        }
//        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskID)
//                .build());
//        if (hasVariables) {
//
//            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskID)
//                    .withVariables(variables)
//                    .build());
//        } else {

//        }
            return 1;
        //写入数据库
//        return actWorkflowFormDataService.insertActWorkflowFormDatas(acwfds);
    }
}
