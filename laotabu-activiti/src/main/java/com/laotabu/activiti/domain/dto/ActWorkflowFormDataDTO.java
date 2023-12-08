package com.laotabu.activiti.domain.dto;


import com.laotabu.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 动态单对象 act_workflow_formdata
 * 
 * @author danny
 * @date 2020-11-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActWorkflowFormDataDTO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表单id */
    private String controlId;
    private String controlType;

    /** 表单名称 */
    private String controlLable;
    private String controlIsParam;

    /** 表单值 */
    private String controlValue;
    private String controlDefault;


}
