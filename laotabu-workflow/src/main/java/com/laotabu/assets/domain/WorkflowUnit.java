package com.laotabu.assets.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计量单位表
 *
 */
@TableName("workflow_unit")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowUnit {
    private static final long serialVersionUID = 1L;

    /**
     * id字段
     */
    @TableId(type = IdType.AUTO)
    int id;

    /**
     * 名称
     */
    @TableField("name")
    String name;

}
