package com.laotabu.assets.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.laotabu.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.laotabu.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 固资申请头对象 workflow_fixed_assets_title
 * 
 * @author lsd
 * @date 2023-11-15
 */
@TableName("workflow_fixed_assets_title")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowFixedAssetsTitle {
    private static final long serialVersionUID = 1L;

    /**
     * id字段
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 流水号
     */
    @TableField("serialNumber")
    private String serialNumber;

    /**
     * 申请人[id + name]
     */
    @NotBlank(message = "申请人不能为空")
    @TableField(exist = false)
    private String applicant;

    /**
     * 申请人id[这个才需要写入数据库]
     */
    @TableField("applicantId")
    private String applicantId;

    /**
     * 申请人name[这个才需要写入数据库]
     */
    @Excel(name = "申请人")
    @TableField("applicantName")
    private String applicantName;

    /**
     * 申请日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "申请日期", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("applicationDate")
    private Date applicationDate;

    /**
     * 使用部门
     */
    @Excel(name = "使用部门")
//    @NotBlank(message = "使用部门不能为空")
    @TableField("department")
    private Long department;

    /**
     * 项目名称
     */
    @Excel(name = "项目名称")
    @TableField("projectName")
    private String projectName;

    /**
     * 项目编号
     */
    @Excel(name = "项目编号")
    @TableField("projectCode")
    private String projectCode;

    /**
     * 项目类型: 0-政府项目,1-研发项目,2-其它
     */
    @Excel(name = "项目类型")
    @TableField("projectType")
    private String projectType;

    /**
     * 资金类别:0-自有资金,1-募投资金,2-政府资金
     */
    @Excel(name = "资金类别")
    @TableField("fundCategory")
    private String fundCategory;

    /**
     * 需求时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "需求时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("demandTime")
    private Date demandTime;

    /**
     * 申请原因提纲:0-新入职,1-新增加,2-报废后新增,3-借用,4-其他
     */
    @Excel(name = "申请原因")
    @TableField("outlineOfReasons")
    private String outlineOfReasons;

    /**
     * 申请理由详细内容
     */
    @Excel(name = "申请理由详细内容")
    @TableField("detailsOfReason")
    private String detailsOfReason;

    /**
     * 是否启动环境&安全因素识别处理
     */
    @Excel(name = "是否启动环境&安全因素识别处理")
    @TableField("enableIdentificationProcessing")
    private Integer enableIdentificationProcessing;

    /**
     * 资产清单列表（存放了清单细项的id）
     */
    @Excel(name = "资产清单列表", readConverterExp = "存放了清单细项的id，格式是**/**/**")
    @TableField("listOfAssets")
    private String listOfAssetsStr;


    @TableField(exist = false)
    private List<WorkflowFixedAssetsDetalis> listOfAssets;

    /**
     * 申请人附件说明
     */
    @Excel(name = "申请人附件说明")
    @TableField("attachments")
    private String attachments;

    /**
     * IT部配置意见:0-IT或物业仓库,1-工厂仓库,2-申请采购
     */
    @Excel(name = "IT部配置意见")
    @TableField("configurationAdvice")
    private String configurationAdvice;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField("instanceId")
    private String instanceId;

    // 固资申请单标题
    @TableField(exist = false)
    private String title;

    @Excel(name = "状态")
    private Integer status;
//    /**
//     * 状态中文描述
//     */
//    @TableField(exist = false)
//    private String statusStr;

    /**
     * 0-预算内，1-预算外
     */
    @TableField("budget_type")
    private String budgetType;
    /**
     * 部门名称
     */
    @TableField("departmentName")
    private String departmentName;
}
