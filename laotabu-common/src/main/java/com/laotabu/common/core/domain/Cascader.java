package com.laotabu.common.core.domain;

import com.laotabu.common.core.domain.entity.SysDept;
import com.laotabu.common.core.domain.entity.SysMenu;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: lsd
 * @Date: 2023/11/20 - 13:59
 * @Desc:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cascader {
    private static final long serialVersionUID = 1L;

    /** 值 */
    private Long value;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Cascader> children;



    public Cascader(SysDept dept)
    {
        this.value = dept.getDeptId();
        this.label = dept.getDeptName();
        this.children = dept.getChildren().stream().map(Cascader::new).collect(Collectors.toList());
    }

    public Cascader(SysMenu menu)
    {
        this.value = menu.getMenuId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(Cascader::new).collect(Collectors.toList());
    }


}
