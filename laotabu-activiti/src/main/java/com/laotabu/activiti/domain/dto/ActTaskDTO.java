package com.laotabu.activiti.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.laotabu.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.activiti.api.task.model.Task;

import org.activiti.engine.runtime.ProcessInstance;

import java.util.Date;
public class ActTaskDTO
        extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String status;

    private String assignee;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;
    private String instanceName;
    private String definitionKey;
    private String businessKey;

    public ActTaskDTO() {
    }

    public ActTaskDTO(Task task, ProcessInstance processInstance) {
        this.id = task.getId();
        this.name = task.getName();
        this.status = task.getStatus().toString();
        this.createdDate = task.getCreatedDate();
        this.instanceName = processInstance.getName();
        this.definitionKey=processInstance.getProcessDefinitionKey();
        this.businessKey=processInstance.getBusinessKey();
        this.assignee = task.getAssignee();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getDefinitionKey() {
        return definitionKey;
    }

    public void setDefinitionKey(String definitionKey) {
        this.definitionKey = definitionKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @Override
    public String toString() {
        return "ActTaskDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", assignee='" + assignee + '\'' +
                ", createdDate=" + createdDate +
                ", instanceName='" + instanceName + '\'' +
                ", definitionKey='" + definitionKey + '\'' +
                ", businessKey='" + businessKey + '\'' +
                '}';
    }
}
