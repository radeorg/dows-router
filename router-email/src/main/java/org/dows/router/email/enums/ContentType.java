package org.dows.router.email.enums;

/**
 * 邮件内容的类型
 */
public enum ContentType {

    //text格式
    TEXT("text"),
    //html格式
    HTML("html"),
    //模板
    TEMPLATE("template")
    ;

    private String value;

    ContentType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
