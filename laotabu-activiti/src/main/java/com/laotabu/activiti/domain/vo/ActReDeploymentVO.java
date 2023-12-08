package com.laotabu.activiti.domain.vo;

import java.util.Date;

/**
 * laotabu
 * 创建日期:2023/11/7
 * 版本   开发者     日期
 * 1.0    lsd    2023/11/7
 */
public class ActReDeploymentVO {

    private String id;
    private Date deployTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(Date deployTime) {
        this.deployTime = deployTime;
    }
}
