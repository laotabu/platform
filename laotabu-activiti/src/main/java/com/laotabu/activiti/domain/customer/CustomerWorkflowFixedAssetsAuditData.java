package com.laotabu.activiti.domain.customer;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: lsd
 * @Date: 2023/12/8 - 11:28
 * @Desc:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("workflow_fixed_assets_audit_data")
public class CustomerWorkflowFixedAssetsAuditData {
    @TableField("id")
    Long id;
    @TableField("taskId")
    String  taskId;
    @TableField("taskName")
    String taskName;
    @TableField("approveId")
    Long approveId;
    @TableField("approveName")
    String approveName;
    @TableField("status")
    char status;
    @TableField("remark")
    String remark;
    @TableField("business_key")
    String business_key;
    @TableField("create_time")
    Date create_time;

}
