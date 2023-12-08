package com.laotabu.assets.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.laotabu.common.annotation.Excel;
import com.laotabu.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 固资申请细项对象 workflow_fixed_assets_detalis
 * 
 * @author lsd
 * @date 2023-11-15
 */
@TableName("workflow_fixed_assets_detalis")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowFixedAssetsDetalis
{

//    private static final long serialVersionUID = 1L;

    /** 细项id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 资产名称  */
    @Excel(name = "资产名称 ")
    @TableField("assetName")
    private String assetName;

    /** 资产类别 */
    @Excel(name = "资产类别")
    @TableField("assetType")
    private Long assetType;

    /** 品牌 */
    @Excel(name = "品牌")
    @TableField("brand")
    private String brand;

    /** 规格型号 */
    @Excel(name = "规格型号")
    @TableField("specificationAndModel")
    private String specificationAndModel;

    /** 单位 */
    @Excel(name = "单位")
    @TableField("unit")
    private Long unit;

    /** 数量 */
    @Excel(name = "数量")
    @TableField("quantity")
    private Long quantity;

    /** 预估金额 */
    @Excel(name = "预估金额")
    @TableField("estimatedAmount")
    private Long estimatedAmount;

    /** 资产编号 */
    @Excel(name = "资产编号")
    @TableField("assetNumber")
    private String assetNumber;

    /** 原值 */
    @Excel(name = "原值")
    @TableField("originalPrice")
    private Long originalPrice;

    /** 报销单号 */
    @Excel(name = "报销单号")
    @TableField("reimbursementNumber")
    private String reimbursementNumber;

    /** 存放地点[部门] */
    @Excel(name = "存放地点[部门]")
    @TableField("placeOfStorage")
    private Long placeOfStorage;

    /** 保管人名称 */
    @Excel(name = "保管人名称")
    @TableField("preserverName")
    private String preserverName;

//    /** 保管人id */
//    @Excel(name = "保管人id")
//    @TableField("preserverId")
//    private Long preserverId;

    @TableField(exist = false)
    private String placeOfStorageName;


}
