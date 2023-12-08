package com.laotabu.activiti.controller;


import com.laotabu.activiti.domain.customer.CustomerWorkflowFixedAssets;
import com.laotabu.activiti.service.IActTaskService;
import com.github.pagehelper.Page;
import com.laotabu.activiti.domain.dto.ActTaskDTO;
import com.laotabu.activiti.domain.dto.ActWorkflowFormDataDTO;
import com.laotabu.common.core.controller.BaseController;
import com.laotabu.common.core.domain.AjaxResult;
import com.laotabu.common.core.page.PageDomain;
import com.laotabu.common.core.page.TableDataInfo;
import com.laotabu.common.core.page.TableSupport;
import org.activiti.api.task.runtime.TaskRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


@RestController
@RequestMapping("/task")
public class TaskController extends BaseController {
    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private IActTaskService actTaskService;



    //获取我的代办任务
    @GetMapping(value = "/list")
    public TableDataInfo getTasks() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<ActTaskDTO> hashMaps = actTaskService.selectProcessDefinitionList(pageDomain);
         return getDataTable(hashMaps);


    }


    //渲染表单
    @GetMapping(value = "/formDataShow/{taskID}")
    public AjaxResult formDataShow(@PathVariable("taskID") String taskID) {
        return AjaxResult.success(actTaskService.formDataShow(taskID));
    }

    //保存动态表单
    @PostMapping(value = "/dynamicformDataSave/{taskID}")
    public AjaxResult dynamicformDataSave(@PathVariable("taskID") String taskID,
                                   @RequestBody   List<ActWorkflowFormDataDTO> formData ) throws ParseException {
        return toAjax(actTaskService.dynamicformDataSave(taskID, formData));
    }

    // 定制表单[资产] -- 不推荐模仿
    @PostMapping(value = "/customizedformDataSave/assets/approve/{taskID}")
    public AjaxResult assetsApprove(@PathVariable("taskID") String taskID,
                                             @RequestBody CustomerWorkflowFixedAssets customerWorkflowFixedAssets) throws ParseException {
        return toAjax(actTaskService.assetsApprove(taskID, customerWorkflowFixedAssets));
    }
    @PostMapping(value = "/customizedformDataSave/assets/reject/{taskID}")
    public AjaxResult assetsReject(@PathVariable("taskID") String taskID,
                                             @RequestBody CustomerWorkflowFixedAssets customerWorkflowFixedAssets) throws ParseException {
        System.out.println(taskID);
        System.out.println(customerWorkflowFixedAssets);
        return AjaxResult.success();
    }
}
