package com.laotabu.activiti.service;

import com.laotabu.activiti.domain.dto.ActivitiHighLineDTO;

public interface IActivitiHistoryService {
    public ActivitiHighLineDTO gethighLine(String instanceId);
}
