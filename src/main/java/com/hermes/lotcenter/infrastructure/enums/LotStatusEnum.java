package com.hermes.lotcenter.infrastructure.enums;

import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/1.
 * <p>
 * 彩票记录状态 @1:已开奖@2:未开奖
 */
public enum LotStatusEnum {

    HAS_LOT(1, "已开奖"),
    NOT_LOT(2, "未开奖");

    private Integer code;

    private String desc;

    LotStatusEnum(Integer code, String desc) {
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

    public static LotStatusEnum fromCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        LotStatusEnum[] values = values();
        for (LotStatusEnum item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
