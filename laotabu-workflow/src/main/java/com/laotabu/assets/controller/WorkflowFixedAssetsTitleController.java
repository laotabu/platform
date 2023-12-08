package com.laotabu.assets.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.laotabu.common.enums.BusinessType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
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
import com.laotabu.assets.domain.WorkflowFixedAssetsTitle;
import com.laotabu.assets.service.IWorkflowFixedAssetsTitleService;
import com.laotabu.common.utils.poi.ExcelUtil;
import com.laotabu.common.core.page.TableDataInfo;

/**
 * 固资申请头Controller
 * 
 * @author lsd
 * @date 2023-11-15
 */
@RestController
@RequestMapping("/workflow/assets")
public class WorkflowFixedAssetsTitleController extends BaseController
{
    @Autowired
    private IWorkflowFixedAssetsTitleService workflowFixedAssetsTitleService;

    /**
     * 查询固资申请头列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:assets:list')")
    @GetMapping("/list")
    public TableDataInfo list(WorkflowFixedAssetsTitle workflowFixedAssetsTitle)
    {
        startPage();
        List<WorkflowFixedAssetsTitle> list = workflowFixedAssetsTitleService.selectWorkflowFixedAssetsTitleList(workflowFixedAssetsTitle);
        return getDataTable(list);
    }

    /**
     * 导出固资申请头列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:assets:export')")
    @Log(title = "固资申请头", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WorkflowFixedAssetsTitle workflowFixedAssetsTitle)
    {
        List<WorkflowFixedAssetsTitle> list = workflowFixedAssetsTitleService.selectWorkflowFixedAssetsTitleList(workflowFixedAssetsTitle);
        ExcelUtil<WorkflowFixedAssetsTitle> util = new ExcelUtil<WorkflowFixedAssetsTitle>(WorkflowFixedAssetsTitle.class);
        util.exportExcel(response, list, "固资申请头数据");
    }

    /**
     * 获取固资申请头详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:assets:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(workflowFixedAssetsTitleService.selectWorkflowFixedAssetsTitleById(id));
    }

    /**
     * 根据businessKey获取固资申请单内容
     */
    @PreAuthorize("@ss.hasPermi('workflow:assets:query')")
    @GetMapping(value = "/bs/{businessKey}")
    public AjaxResult getInfoByBusinessKey(@PathVariable("businessKey") String businessKey)
    {
        System.out.println(businessKey);
        return success(workflowFixedAssetsTitleService.getOne(
                new LambdaQueryWrapper<WorkflowFixedAssetsTitle>().eq(WorkflowFixedAssetsTitle::getSerialNumber, businessKey)));
    }

    /**
     * 新增固资申请头
     */
    @PreAuthorize("@ss.hasPermi('workflow:assets:add')")
    @Log(title = "固资申请头", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody WorkflowFixedAssetsTitle workflowFixedAssetsTitle, BindingResult bindingResult) throws ExecutionException, InterruptedException {
        if (bindingResult.hasErrors()){
            return AjaxResult.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return toAjax(workflowFixedAssetsTitleService.insertWorkflowFixedAssets(workflowFixedAssetsTitle));
    }

    /**
     * 修改固资申请头
     */
    @PreAuthorize("@ss.hasPermi('workflow:assets:edit')")
    @Log(title = "固资申请头", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WorkflowFixedAssetsTitle workflowFixedAssetsTitle)
    {
        return toAjax(workflowFixedAssetsTitleService.updateWorkflowFixedAssetsTitle(workflowFixedAssetsTitle));
    }

    /**
     * 删除固资申请头
     */
    @PreAuthorize("@ss.hasPermi('workflow:assets:remove')")
    @Log(title = "固资申请头", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) throws ExecutionException, InterruptedException {
        return toAjax(workflowFixedAssetsTitleService.deleteWorkflowFixedAssetsTitleByIds(ids));
    }
}
