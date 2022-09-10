package com.hermes.lotcenter.infrastructure.enums;

import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/9.
 * <p>
 * 投注任务状态@1:未开始@2:进行中@3:已结束@4:已作废
 */
public enum BetTaskStatusEnum {

    NOT_START(1, "未开始"),
    DOING(2, "进行中"),
    ENDED(3, "已结束"),
    DELETED(4, "已作废");

    private Integer code;

    private String desc;

    BetTaskStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean equals(Integer code) {
        return this.getCode().equals(code);
    }

    public boolean notEquals(Integer code) {
        return !this.equals(code);
    }

    public static BetTaskStatusEnum fromCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        BetTaskStatusEnum[] values = values();
        for (BetTaskStatusEnum item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
