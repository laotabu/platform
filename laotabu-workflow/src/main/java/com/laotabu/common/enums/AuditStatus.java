package com.laotabu.common.enums;

/**
 * @Author: lsd
 * @Date: 2023/11/20 - 16:06
 * @Desc: 审核状态枚举
 */
public enum AuditStatus {
    /**
     * 创建状态
     */
    CREATED("创建状态", 0),

    /**
     * 审核中
     */
    REVIEWING("审核中", 1),
    /**
     * 驳回待审
     */
    REJECTED_PENDING_REVIEW("驳回待审", 2),
    /**
     * 流程关闭
     */
    CLOSED("流程关闭", 3),
    /**
     * 通过
     */
    PASSED("通过", 4),
    ;

    private AuditStatus(String name, Integer value){
        this.name = name;
        this.value = value;
    }
    private String name;
    private Integer value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
