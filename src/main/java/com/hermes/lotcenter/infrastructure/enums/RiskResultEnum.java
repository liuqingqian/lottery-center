package com.hermes.lotcenter.infrastructure.enums;

import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/11.
 * <p>
 * 状态枚举 @1:正常@2:风险
 */
public enum RiskResultEnum {
    NORMAL(1, "正常"),
    RISK(2, "风险");

    private Integer code;

    private String desc;

    RiskResultEnum(Integer code, String desc) {
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

    public static RiskResultEnum fromCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        RiskResultEnum[] values = values();
        for (RiskResultEnum item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
