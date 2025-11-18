package org.dows.email.entity;

import lombok.Data;
import org.dows.email.Content;

import java.util.Map;

/**
 *  邮件实体
 */
@Data
public class Mail implements Content {
    /** 发送人 */
    private String from;
    /** 邮件标题 */
    private String title;
    /** 邮件内容 */
    private String content;
    /** 内容格式(默认html) */
    private String contentType;
    /** 接收邮件地址 */
    private String to;

    /*************模板发送****************/
    /** 模板名称 */
    private String templateName;
    /** 模板变量替换 */
    private Map<String, Object> maps;
}
