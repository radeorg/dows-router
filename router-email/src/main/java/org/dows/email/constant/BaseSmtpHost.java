package org.dows.email.constant;

/**
 * 邮箱SMTP服务配置接口
 * 实现该接口，可自定义实现更多邮箱SMTP配置
 */
public interface BaseSmtpHost {
    /**
     * 获取邮件服务器 host
     *
     * @return
     */
    String getSmtpHost();

    /**
     * 获取邮件服务器 端口
     *
     * @return
     */
    Integer getSmtpPort();

}
