package com.laotabu.activiti.domain.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.laotabu.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 *
 * 
 * @author lsd
 * @date 2023-11-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerWorkflowFixedAssets {
    private static final long serialVersionUID = 1L;

    /**
     * id字段
     */
    private Long id;

    /**
     * 流水号
     */
    private String serialNumber;

    /**
     * 申请人
     */
    @Excel(name = "申请人")
    @NotBlank(message = "申请人不能为空")
    private String applicant;

    /**
     * 申请日期
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date applicationDate;

    /**
     * 使用部门
     */
    private Long department;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 项目类型: 0-政府项目,1-研发项目,2-其它
     */
    private String projectType;

    /**
     * 资金类别:0-自有资金,1-募投资金,2-政府资金
     */
    private String fundCategory;

    /**
     * 需求时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date demandTime;

    /**
     * 申请原因提纲:0-新入职,1-新增加,2-报废后新增,3-借用,4-其他
     */
    private String outlineOfReasons;

    /**
     * 申请理由详细内容
     */
    private String detailsOfReason;

    /**
     * 是否启动环境&安全因素识别处理
     */
    private Integer enableIdentificationProcessing;

    /**
     * 资产清单列表（存放了清单细项的id）
     */
    private String listOfAssetsStr;


    private List<CustomerWorkflowFixedAssetsDetalis> listOfAssets;

    /**
     * 申请人附件说明
     */
    private String attachments;

    /**
     * IT部配置意见:0-IT或物业仓库,1-工厂仓库,2-申请采购
     */
    private String configurationAdvice;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String instanceId;

    private Integer status;

    /**
     * 0-预算内，1-预算外
     */
    private String budgetType;
    /**
     * 部门名称
     */
    private String departmentName;

    private CustomerWorkflowFixedAssetsAuditData customerWorkflowFixedAssetsAuditData;

}
