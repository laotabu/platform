package com.laotabu.leave.controller;

import java.util.List;


import com.laotabu.common.annotation.Log;
import com.laotabu.common.core.controller.BaseController;
import com.laotabu.common.core.domain.AjaxResult;
import com.laotabu.common.core.page.TableDataInfo;
import com.laotabu.common.enums.BusinessType;
import com.laotabu.common.utils.SecurityUtils;
import com.laotabu.common.utils.poi.ExcelUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.laotabu.leave.domain.WorkflowLeave;
import com.laotabu.leave.service.IWorkflowLeaveService;

/**
 * 请假Controller
 *
 * @author danny
 * @date 2020-10-28
 */
@RestController
@RequestMapping("/workflow/leave")
public class WorkflowLeaveController extends BaseController {
    @Autowired
    private IWorkflowLeaveService workflowLeaveService;

    /**
     * 查询请假列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:leave:list')")
    @GetMapping("/list")
    public TableDataInfo list(WorkflowLeave workflowLeave) {
        startPage();
        workflowLeave.setCreateBy(SecurityUtils.getUsername());
        List<WorkflowLeave> list = workflowLeaveService.selectWorkflowLeaveAndTaskNameList(workflowLeave);
        return getDataTable(list);
    }
    /**
     * 查询请假列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:leave:list')")
    @GetMapping("/listAll")
    public TableDataInfo listAll(WorkflowLeave workflowLeave) {
        startPage();
        List<WorkflowLeave> list = workflowLeaveService.selectWorkflowLeaveList(workflowLeave);
        return getDataTable(list);
    }

    /**
     * 导出请假列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:leave:export')")
    @Log(title = "请假", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WorkflowLeave workflowLeave) {
        List<WorkflowLeave> list = workflowLeaveService.selectWorkflowLeaveList(workflowLeave);
        ExcelUtil<WorkflowLeave> util = new ExcelUtil<WorkflowLeave>(WorkflowLeave.class);
        return util.exportExcel(list, "leave");
    }

    /**
     * 获取请假详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:leave:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(workflowLeaveService.selectWorkflowLeaveById(id));
    }   /**
     * 获取请假详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:leave:query')")
    @GetMapping(value = "ByInstanceId/{instanceId}")
    public AjaxResult getInfoByInstanceId(@PathVariable("instanceId") String instanceId) {
        return AjaxResult.success(workflowLeaveService.selectWorkflowLeaveByInstanceId(instanceId));
    }

    /**
     * 新增请假
     */
    @PreAuthorize("@ss.hasPermi('workflow:leave:add')")
    @Log(title = "请假", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WorkflowLeave workflowLeave) {
        return toAjax(workflowLeaveService.insertWorkflowLeave(workflowLeave));
    }

    /**
     * 修改请假
     */
    @PreAuthorize("@ss.hasPermi('workflow:leave:edit')")
    @Log(title = "请假", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WorkflowLeave workflowLeave) {
        return toAjax(workflowLeaveService.insertWorkflowLeave(workflowLeave));
    }

    /**
     * 删除请假
     */
    @PreAuthorize("@ss.hasPermi('workflow:leave:remove')")
    @Log(title = "请假", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(workflowLeaveService.deleteWorkflowLeaveByIds(ids));
    }
}
