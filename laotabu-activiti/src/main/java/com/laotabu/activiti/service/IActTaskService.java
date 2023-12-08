package com.laotabu.activiti.service;

import com.laotabu.activiti.domain.customer.CustomerWorkflowFixedAssets;
import com.github.pagehelper.Page;
import com.laotabu.activiti.domain.dto.ActWorkflowFormDataDTO;
import com.laotabu.activiti.domain.dto.ActTaskDTO;
import com.laotabu.common.core.page.PageDomain;

import java.text.ParseException;
import java.util.List;

public interface IActTaskService {
    public Page<ActTaskDTO> selectProcessDefinitionList(PageDomain pageDomain);
    public List<String>formDataShow(String taskID);
    public int dynamicformDataSave(String taskID, List<ActWorkflowFormDataDTO> awfs) throws ParseException;

    public int assetsApprove(String taskID, CustomerWorkflowFixedAssets customerWorkflowFixedAssets);
}
