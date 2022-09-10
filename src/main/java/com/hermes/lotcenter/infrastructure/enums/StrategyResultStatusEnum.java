package com.hermes.lotcenter.infrastructure.enums;

import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/9.
 * <p>
 * 策略结果状态枚举： @1:正常@2:异常@3:跳过
 */
public enum StrategyResultStatusEnum {

    SUCCESS(1, "正常"),
    FAIL(2, "异常"),
    SKIP(3, "跳过");

    private Integer code;

    private String desc;

    StrategyResultStatusEnum(Integer code, String desc) {
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

    public static StrategyResultStatusEnum fromCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        StrategyResultStatusEnum[] values = values();
        for (StrategyResultStatusEnum item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

}
