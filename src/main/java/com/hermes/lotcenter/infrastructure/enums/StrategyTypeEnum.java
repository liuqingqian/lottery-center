package com.hermes.lotcenter.infrastructure.enums;

import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/9.
 * <p>
 * 投注策略类型枚举 @1:顺投递减@2:逆投递减@3:倍投@4:顺龙@5:斩龙
 */
public enum StrategyTypeEnum {
    POSITIVE_SPITTLE(1, "顺投递减"),
    NEGATIVE_SPITTLE(2, "逆投递减"),
    TIMES(3, "倍投"),
    POSITIVE_DRAGON(4, "顺龙"),
    NEGATIVE_DRAGON(5, "斩龙");

    private Integer code;

    private String desc;

    StrategyTypeEnum(Integer code, String desc) {
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

    public static StrategyTypeEnum fromCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        StrategyTypeEnum[] values = values();
        for (StrategyTypeEnum item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
