package org.dows.router.email.entity;

import lombok.RequiredArgsConstructor;
import org.dows.router.email.enums.ContentType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 邮件发送类
 */
@RequiredArgsConstructor
@Component
public class MailBuilder {

    /** 邮件实体 */
    private final Mail mail = new Mail();


    public MailBuilder from(String from) {
        mail.setFrom(from);
        return this;
    }
    /**
     * 设置邮件标题
     *
     * @param title：邮件标题
     * @return
     */
    public MailBuilder title(String title) {
        mail.setTitle(title);
        return this;
    }

    /**
     * 设置邮件内容
     *
     * @param content：内容
     * @return
     */
    public MailBuilder content(String content) {
        mail.setContent(content);
        return this;
    }

    /**
     * 设置邮件格式
     *
     * @param typeEnum：邮件格式
     * @return
     */
    public MailBuilder contentType(ContentType typeEnum) {
        mail.setContentType(typeEnum.value());
        return this;
    }

    /**
     * 设置请求目标邮件地址
     *
     * @param to：请求目标邮件地址
     * @return
     */
    public MailBuilder to(String to) {
        mail.setTo(to);
        return this;
    }

    /**
     * 设置模板名称
     *
     * @param templateName
     * @return
     */
    public MailBuilder templateName(String templateName) {
        mail.setTemplateName(templateName);
        return this;
    }

    /**
     * 模板发送的变量
     *
     * @param maps：maps
     * @return
     */
    public MailBuilder maps(Map<String, Object> maps) {
        mail.setMaps(maps);
        return this;
    }

}
