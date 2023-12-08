package com.laotabu.activiti.domain.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author lsd
 * @date 2023-11-15
 */
@TableName("workflow_fixed_assets_detalis")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerWorkflowFixedAssetsDetalis
{

//    private static final long serialVersionUID = 1L;

    /** 细项id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 资产名称  */
    @TableField("assetName")
    private String assetName;

    /** 资产类别 */
    @TableField("assetType")
    private Long assetType;

    /** 品牌 */
    @TableField("brand")
    private String brand;

    /** 规格型号 */
    @TableField("specificationAndModel")
    private String specificationAndModel;

    /** 单位 */
    @TableField("unit")
    private Long unit;

    /** 数量 */
    @TableField("quantity")
    private Long quantity;

    /** 预估金额 */
    @TableField("estimatedAmount")
    private Long estimatedAmount;

    /** 资产编号 */
    @TableField("assetNumber")
    private String assetNumber;

    /** 原值 */
    @TableField("originalPrice")
    private Long originalPrice;

    /** 报销单号 */
    @TableField("reimbursementNumber")
    private String reimbursementNumber;

    /** 存放地点[部门] */
    @TableField("placeOfStorage")
    private Long placeOfStorage;

    /** 保管人名称 */
    @TableField("preserverName")
    private String preserverName;


}
