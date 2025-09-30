package org.dows.router.core;

import lombok.Getter;

public enum SessionTypeEnum {

    EMAIL(1, "emailRouter"),
    SMS(2, "smsRouter"),
    LLM(3, "llmRouter");
    @Getter
    private int value;
    @Getter
    private String beanCode;


    SessionTypeEnum(int value, String beanCode) {
        this.value = value;
        this.beanCode = beanCode;
    }

    public static SessionTypeEnum getByType(int sessionType) {
        for (SessionTypeEnum value : values()) {
            if (value.getValue() == sessionType) {
                return value;
            }
        }
        throw new RuntimeException("没有对应的枚举值");
    }

}
