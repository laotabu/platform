package com.laotabu.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: lsd
 * @Date: 2023/10/19 - 15:08
 * @Desc:
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("jq_countryGroup")
public class CountryGroup {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("countryGroup")
    @NotNull
    @NotEmpty
    private String countryGroup;
}
