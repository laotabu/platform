package com.laotabu.assets.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.laotabu.assets.domain.WorkflowUnit;
import com.laotabu.common.enums.BusinessType;
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
import com.laotabu.common.annotation.Log;
import com.laotabu.common.core.controller.BaseController;
import com.laotabu.common.core.domain.AjaxResult;
import com.laotabu.assets.domain.WorkflowFixedAssetsDetalis;
import com.laotabu.assets.service.IWorkflowFixedAssetsDetalisService;
import com.laotabu.common.utils.poi.ExcelUtil;
import com.laotabu.common.core.page.TableDataInfo;

/**
 * 固资申请细项Controller
 * 
 * @author lsd
 * @date 2023-11-15
 */
@RestController
@RequestMapping("/workflow/assetsDetalis")
public class WorkflowFixedAssetsDetalisController extends BaseController
{
    @Autowired
    private IWorkflowFixedAssetsDetalisService workflowFixedAssetsDetalisService;

    /**
     * 查询固资申请细项列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:assetsDetalis:list')")
    @GetMapping("/list")
    public TableDataInfo list(WorkflowFixedAssetsDetalis workflowFixedAssetsDetalis)
    {
        startPage();
        List<WorkflowFixedAssetsDetalis> list = workflowFixedAssetsDetalisService.selectWorkflowFixedAssetsDetalisList(workflowFixedAssetsDetalis);
        return getDataTable(list);
    }

    /**
     * 导出固资申请细项列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:assetsDetalis:export')")
    @Log(title = "固资申请细项", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WorkflowFixedAssetsDetalis workflowFixedAssetsDetalis)
    {
        List<WorkflowFixedAssetsDetalis> list = workflowFixedAssetsDetalisService.selectWorkflowFixedAssetsDetalisList(workflowFixedAssetsDetalis);
        ExcelUtil<WorkflowFixedAssetsDetalis> util = new ExcelUtil<WorkflowFixedAssetsDetalis>(WorkflowFixedAssetsDetalis.class);
        util.exportExcel(response, list, "固资申请细项数据");
    }

    /**
     * 获取固资申请细项详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:assetsDetalis:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(workflowFixedAssetsDetalisService.selectWorkflowFixedAssetsDetalisById(id));
    }

    /**
     * 新增固资申请细项
     */
    @PreAuthorize("@ss.hasPermi('workflow:assetsDetalis:add')")
    @Log(title = "固资申请细项", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WorkflowFixedAssetsDetalis workflowFixedAssetsDetalis)
    {
        return toAjax(workflowFixedAssetsDetalisService.insertWorkflowFixedAssetsDetalis(workflowFixedAssetsDetalis));
    }

    /**
     * 修改固资申请细项
     */
    @PreAuthorize("@ss.hasPermi('workflow:assetsDetalis:edit')")
    @Log(title = "固资申请细项", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WorkflowFixedAssetsDetalis workflowFixedAssetsDetalis)
    {
        return toAjax(workflowFixedAssetsDetalisService.updateWorkflowFixedAssetsDetalis(workflowFixedAssetsDetalis));
    }

    /**
     * 删除固资申请细项
     */
    @PreAuthorize("@ss.hasPermi('workflow:assetsDetalis:remove')")
    @Log(title = "固资申请细项", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {

        return toAjax(workflowFixedAssetsDetalisService.deleteWorkflowFixedAssetsDetalisByIds(ids));
    }

    /**
     * 获取单位表数据
     */
    @GetMapping("/getUnits")
    public AjaxResult getUnits()
    {
        List<WorkflowUnit> result = new ArrayList<>();
        result = workflowFixedAssetsDetalisService.getUnits();
        return success(result);
    }
    /**
     * 获取固资申请细项详细信息列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:assetsDetalis:query')")
    @GetMapping(value = "/list/{ids}")
    public AjaxResult getAssetsDetalisInfo(@PathVariable Long[] ids)
    {
        return success(workflowFixedAssetsDetalisService.getAssetsDetalisInfoByIds(ids));
    }
}
