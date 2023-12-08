package com.laotabu.assets.listener;


import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.util.List;

/**
 * @Author: lsd
 * @Date: 2023/11/23 - 18:45
 * @Desc: activiti 监听器
 */
public class AssetsMiddleEventListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) {
        // todo 也可以把消息放到消息中间件，异步去处理
        System.out.println("流程已经到达");
        System.out.println(execution.getCurrentActivityId());
        System.out.println(execution.getProcessInstanceBusinessKey());
        System.out.println(execution.getCurrentFlowElement().getName());
        // 获取流程定义Id
        String processInstanceId = execution.getProcessInstanceId();
        System.out.println(processInstanceId);
        // 根据流程定义Id获取bpmnModel
//        BpmnModel bpmnModel = SpringUtils.getBean(RepositoryService.class).getBpmnModel(processInstanceId);
//        List<Process> processes = bpmnModel.getProcesses();

    }
}
