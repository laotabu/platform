package com.laotabu.assets.service.impl;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.laotabu.assets.domain.WorkflowFixedAssetsDetalis;
import com.laotabu.assets.mapper.WorkflowFixedAssetsDetalisMapper;
import com.laotabu.assets.service.IWorkflowFixedAssetsDetalisService;
import com.laotabu.common.constant.PostCode;
import com.laotabu.common.constant.WorkflowKeyConstant;
import com.laotabu.common.core.domain.entity.SysDept;
import com.laotabu.common.core.domain.entity.SysUser;
import com.laotabu.common.enums.AuditStatus;
import com.laotabu.common.utils.DateUtils;
import com.laotabu.common.utils.SecurityUtils;
import com.laotabu.common.utils.StringUtils;
import com.laotabu.common.utils.uuid.UUID;
import com.laotabu.system.service.ISysDeptService;
import com.laotabu.system.service.ISysUserService;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.bpmn.model.*;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import com.laotabu.assets.mapper.WorkflowFixedAssetsTitleMapper;
import com.laotabu.assets.domain.WorkflowFixedAssetsTitle;
import com.laotabu.assets.service.IWorkflowFixedAssetsTitleService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 固资申请头Service业务层处理
 * 
 * @author lsd
 * @date 2023-11-15
 */
@Service
public class WorkflowFixedAssetsTitleServiceImpl extends ServiceImpl<WorkflowFixedAssetsTitleMapper, WorkflowFixedAssetsTitle>  implements IWorkflowFixedAssetsTitleService
{
    @Autowired
    private WorkflowFixedAssetsTitleMapper workflowFixedAssetsTitleMapper;
    @Autowired
    private WorkflowFixedAssetsDetalisMapper workflowFixedAssetsDetalisMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    /**
     * 预算类型
     */
    public static final String IN_BUDGET = "0"; //预算内
//    public static final String OUT_BUDGET = "1";

    @Autowired
    @Qualifier(value = "threadPoolTaskExecutor")
    ThreadPoolTaskExecutor executor;

//    @Autowired
//    private IWorkflowFixedAssetsDetalisService workflowFixedAssetsDetalisService;
    @Autowired
    private ProcessRuntime processRuntime;
    /**
     * 查询固资申请头
     * 
     * @param id 固资申请头主键
     * @return 固资申请头
     */
    @Override
    public WorkflowFixedAssetsTitle selectWorkflowFixedAssetsTitleById(Long id)
    {
//        return workflowFixedAssetsTitleMapper.selectWorkflowFixedAssetsTitleById(id);
        return workflowFixedAssetsTitleMapper.selectById(id);
    }

    /**
     * 查询固资申请头列表
     * 
     * @param workflowFixedAssetsTitle 固资申请头
     * @return 固资申请头
     */
    @Override
    public List<WorkflowFixedAssetsTitle> selectWorkflowFixedAssetsTitleList(WorkflowFixedAssetsTitle workflowFixedAssetsTitle)
    {
//        return workflowFixedAssetsTitleMapper.selectWorkflowFixedAssetsTitleList(workflowFixedAssetsTitle);
        // 只查自己

        List<WorkflowFixedAssetsTitle> workflowFixedAssetsTitles = workflowFixedAssetsTitleMapper.selectList(
                new LambdaQueryWrapper<WorkflowFixedAssetsTitle>().eq(WorkflowFixedAssetsTitle::getApplicantId, SecurityUtils.getUsername()));
//        // 设置状态名称
//        Optional.ofNullable(workflowFixedAssetsTitles).orElse(new LinkedList<>()).stream().forEach(e-> {
//            if (e.getStatus().intValue() == AuditStatus.CREATED.getValue()){
//                e.setStatusStr(AuditStatus.CREATED.getName());
//            }else if(e.getStatus().intValue() == AuditStatus.REVIEWING.getValue()){
//                e.setStatusStr(AuditStatus.REVIEWING.getName());
//            }else if(e.getStatus().intValue() == AuditStatus.CLOSED.getValue()){
//                e.setStatusStr(AuditStatus.CLOSED.getName());
//            }else if(e.getStatus().intValue() == AuditStatus.PASSED.getValue()){
//                e.setStatusStr(AuditStatus.PASSED.getName());
//            }else if(e.getStatus().intValue() == AuditStatus.REJECTED_PENDING_REVIEW.getValue()){
//                e.setStatusStr(AuditStatus.REJECTED_PENDING_REVIEW.getName());
//            }
//        });
        return workflowFixedAssetsTitles;
    }

    /**
     * 新增固资申请头
     * 
     * @param workflowFixedAssetsTitle 固资申请头
     * @return 结果
     */
    @Override
    public int insertWorkflowFixedAssetsTitle(WorkflowFixedAssetsTitle workflowFixedAssetsTitle)
    {
        return workflowFixedAssetsTitleMapper.insertWorkflowFixedAssetsTitle(workflowFixedAssetsTitle);
    }

    /**
     * 修改固资申请头
     * 
     * @param workflowFixedAssetsTitle 固资申请头
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateWorkflowFixedAssetsTitle(WorkflowFixedAssetsTitle workflowFixedAssetsTitle)
    {
        List<WorkflowFixedAssetsDetalis> listOfAssets = workflowFixedAssetsTitle.getListOfAssets();
        if (listOfAssets.size() > 0) {
            workflowFixedAssetsDetalisMapper.insertOrUpdateBatch(listOfAssets);
            String newListOfAssetsStr = String.join(",", listOfAssets.stream().map(e -> Long.toString(e.getId())).collect(Collectors.toList()));
            String oldListOfAssetsStr = workflowFixedAssetsTitle.getListOfAssetsStr();
            workflowFixedAssetsTitle.setListOfAssetsStr(newListOfAssetsStr);
            // 异步清除失效的清单项
            CompletableFuture.runAsync(() -> deleteInvalidDetailIds(newListOfAssetsStr, oldListOfAssetsStr), executor)
                    .exceptionally(e -> {
                        System.out.println("异常信息为：" + e.getMessage());
                        throw new RuntimeException(e.getMessage());
                    });
        }
        return workflowFixedAssetsTitleMapper.updateById(workflowFixedAssetsTitle);
    }

    private void deleteInvalidDetailIds(String newListOfAssetsStr, String oldListOfAssetsStr) {
        // 异步清除失效的清单项
        Set<String> newListOfAssets = Arrays.stream(newListOfAssetsStr.split(",")).collect(Collectors.toSet());
        Set<String> oldListOfAssets = Arrays.stream(oldListOfAssetsStr.split(",")).collect(Collectors.toSet());
        oldListOfAssets.removeAll(newListOfAssets);
        List<Long> ids = oldListOfAssets.stream().map(e->Long.parseLong(e)).collect(Collectors.toList());
        if (ids.size() > 0){
            workflowFixedAssetsDetalisMapper.deleteBatchIds(ids);
        }
    }

    /**
     * 批量删除固资申请头
     * 
     * @param ids 需要删除的固资申请头主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteWorkflowFixedAssetsTitleByIds(Long[] ids) throws ExecutionException, InterruptedException {
        // 设置变量
        List<Long> detailsIds = new ArrayList<>();
        List<String> businessKeys = new ArrayList<>();

        List<WorkflowFixedAssetsTitle> workflowFixedAssetsTitles = workflowFixedAssetsTitleMapper
                .selectList(new LambdaQueryWrapper<WorkflowFixedAssetsTitle>().in(WorkflowFixedAssetsTitle::getId, ids));
        workflowFixedAssetsTitles = Optional
                .ofNullable(workflowFixedAssetsTitles)
                .orElse(new ArrayList<>())
                .stream()
                .filter(e->e.getStatus()==AuditStatus.CREATED.getValue())
                .collect(Collectors.toList());
        workflowFixedAssetsTitles
                .stream()
                .forEach(e->{
                    String[] split = e.getListOfAssetsStr().split(",");
                    for (String s: split) {
                        detailsIds.add(Long.parseLong(s));
                    }
                    businessKeys.add(e.getSerialNumber());
                });

        // 终止流程
        CompletableFuture<Void> terminateTask = CompletableFuture.runAsync(() -> terminateTask(businessKeys), executor)
                .exceptionally(e -> {
                    System.out.println("异常信息为：" + e.getMessage());
                    throw new RuntimeException(e.getMessage());
                });
        // 删除清单
        CompletableFuture<Integer> deletDetailsTask = CompletableFuture.supplyAsync(() -> workflowFixedAssetsDetalisMapper.deleteBatchIds(detailsIds), executor)
                .exceptionally(e -> {
                    System.out.println("异常信息为：" + e.getMessage());
                    throw new RuntimeException(e.getMessage());
                });
        // 删除申请头
        CompletableFuture<Integer> deletTitleTask =CompletableFuture.supplyAsync(() -> workflowFixedAssetsTitleMapper.deleteWorkflowFixedAssetsTitleByIds(ids), executor)
                .exceptionally(e -> {
                    System.out.println("异常信息为：" + e.getMessage());
                    throw new RuntimeException(e.getMessage());
                });
        CompletableFuture.allOf(terminateTask, deletTitleTask, deletDetailsTask).get();
        return 1;
    }


    private void terminateTask(List<String> businessKeys) {
        ListUtils.emptyIfNull(businessKeys)
                .stream()
                .forEach(key -> {
                    taskService.createTaskQuery().processInstanceBusinessKey(key).list()
                            .forEach(task -> terminateProcessByTask(task.getId(), task));
                });
    }
    private void terminateProcessByTask(String taskId, Task task) {
        if (Objects.isNull(task)){
            log.warn("流程任务实例并不存在, taskId:" + taskId);
            return;
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        EndEvent endEvent = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class).get(0);
        FlowNode currentNode  = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());
        List<SequenceFlow> orignOutgoingFlows  = currentNode.getOutgoingFlows();
        // 将当前节点的出向分支替换为流向最后一个
        List<SequenceFlow> incomingFlows = endEvent.getIncomingFlows();
        currentNode.setOutgoingFlows(incomingFlows);
        taskService.complete(taskId);
        // 流转完再流回来
        currentNode.setOutgoingFlows(orignOutgoingFlows);
    }
    /**
     * 删除固资申请头信息
     * 
     * @param id 固资申请头主键
     * @return 结果
     */
    @Override
    public int deleteWorkflowFixedAssetsTitleById(Long id)
    {
        return workflowFixedAssetsTitleMapper.deleteWorkflowFixedAssetsTitleById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertWorkflowFixedAssets(WorkflowFixedAssetsTitle workflowFixedAssetsTitle) {
        List<WorkflowFixedAssetsDetalis> listOfAssets = workflowFixedAssetsTitle.getListOfAssets();
        workflowFixedAssetsDetalisMapper.insertBatch(listOfAssets);
        List<String> collect = listOfAssets.stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
        workflowFixedAssetsTitle.setListOfAssetsStr(String.join(",", collect));
        // business_id
        workflowFixedAssetsTitle.setSerialNumber(UUID.randomUUID().toString());
        workflowFixedAssetsTitle.setCreateBy(SecurityUtils.getUsername());
        String[] split = workflowFixedAssetsTitle.getApplicant().split(",");
        workflowFixedAssetsTitle.setApplicantName(split[0]);
        workflowFixedAssetsTitle.setApplicantId(split[1]);
        // 设置部门名称
        SysDept sysDept = sysDeptService.selectDeptById(workflowFixedAssetsTitle.getDepartment());
        workflowFixedAssetsTitle.setDepartmentName(Optional.ofNullable(sysDept).orElse(new SysDept()).getDeptName());
        /**
         * 开启一个流程实例，此时会返回一个流程实例对象
         */
        // 获取流程实例变量
        Map<String, Object> variables = getWorkflowFixedAssetsVariables(workflowFixedAssetsTitle);
        // 主要操作了act_ru_execution 这张表
        // 开启流程实例
        ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                .start()
                // 跟流程定义做关联
                .withProcessDefinitionKey(WorkflowKeyConstant.FIXED_ASSET_REQUEST_KEY)
                .withName(workflowFixedAssetsTitle.getCreateBy() + "的" + workflowFixedAssetsTitle.getTitle())
                // 跟业务做关联
                .withBusinessKey(workflowFixedAssetsTitle.getSerialNumber())
                .withVariables(variables)
                .build());
        // 设置流程实例Id
        workflowFixedAssetsTitle.setInstanceId(processInstance.getId());
        // 初始化流程状态
        workflowFixedAssetsTitle.setStatus(AuditStatus.CREATED.getValue());
        workflowFixedAssetsTitle.setCreateTime(DateUtils.getNowDate());
        int count = this.baseMapper.insert(workflowFixedAssetsTitle);
        try {
            // todo 异步通知
            CompletableFuture
                    .runAsync(() -> System.out.println("发送给消息中间件，通知用户"), executor)
                    .exceptionally(e -> {
                        System.out.println("异常信息为：" + e.getMessage());
                        throw new RuntimeException(e.getMessage());
                    }).join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    private Map<String, Object> getWorkflowFixedAssetsVariables(WorkflowFixedAssetsTitle workflowFixedAssetsTitle) {
        Map<String, Object> map = new HashMap<>();
        // 1. 获取固资审核员列表
        String sca = StringUtils.join(sysUserService.selectUserNameByPostCode(PostCode.SOLID_CAPITAL_AUDITOR), ",");
        // 2. 获取IT部主管
        String director = StringUtils.join(sysUserService.selectUserNameByPostCodeAndDeptName(PostCode.DIRECTOR, "IT部"), ",");
        // 3. 获取申请人总监
        String major = StringUtils.join(sysUserService.selectUserNameByPostCodeAndDeptId(PostCode.MAJOR, SecurityUtils.getDeptId()), ",");
        // 4. 获取预算类型
        boolean isBudgetary = IN_BUDGET.equals(workflowFixedAssetsTitle.getBudgetType()) ? true : false;
        // 5. 获取采购员
        String buyer = StringUtils.join(sysUserService.selectUserNameByPostCode(PostCode.BUYER), ",");
        // 6. 获取系统运维组人员
        String itus =  StringUtils.join(sysUserService.selectUserNameByPostCodeAndDeptName(PostCode.USER, "系统运维组"), ",");
        // 7. 获取财务人员
        String fn = StringUtils.join(sysUserService.selectUserNameByPostCode(PostCode.FINANCIAL), ",");
        // 8. 获取IT部总监
        String it_major = StringUtils.join(sysUserService.selectUserNameByPostCodeAndDeptName(PostCode.MAJOR, "IT部"), ",");
        // 9. 获取IT部经理
        String it_manager = StringUtils.join(sysUserService.selectUserNameByPostCodeAndDeptName(PostCode.PROJECT_MANAGER, "IT部"), ",");
        map.put("it_auditor", sca);
        map.put("director", director);
        map.put("major", major);
        map.put("it_manager", it_manager);
        map.put("it_major", it_major);
        map.put("buyer", buyer);
        map.put("isBudgetary", isBudgetary);
        map.put("it_user", itus);
        map.put("financialPersonnel", fn);
        return map;
    }
}
