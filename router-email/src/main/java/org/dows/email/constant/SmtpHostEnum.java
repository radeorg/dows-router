package org.dows.email.constant;

/**
 * 邮箱协议host枚举
 */
public enum SmtpHostEnum implements BaseSmtpHost {
    /**
     * QQ邮箱。需开启smtp，使用授权码作为密码
     */
    SMTP_QQ("smtp.qq.com", 465),
    /**
     * QQ企业邮箱。默认开启了smtp，默认使用邮箱登录密码作为密码。
     */
    SMTP_ENTERPRISE_QQ("smtp.exmail.qq.com", 465),
    /**
     * 网易邮箱。需开启smtp，使用授权码作为密码
     */
    SMTP_163("smtp.163.com", 465),
    /**
     * 新浪邮箱
     */
    SMTP_SINA("smtp.sina.com.cn", 465),
    /**
     * 中国移动邮箱。需开启smtp，使用授权码作为密码
     */
    SMTP_139("smtp.139.com", 465),
    /**
     * 微软邮箱。需开启smtp，使用邮箱登录密码作为密码。mailSmtpSslEnable 需设置 false
     */
    SMTP_OUTLOOK("smtp.office365.com", 587),
    /**
     * 阿里云企业邮箱。需开启smtp，默认使用邮箱登录密码作为密码。
     */
    SMTP_ALIBABA("smtp.mxhichina.com", 465),
    ;

    private final String smtpHost;

    private final Integer smtpPort;

    SmtpHostEnum(String smtpHost, Integer smtpPort) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
    }

    @Override
    public String getSmtpHost() {
        return smtpHost;
    }

    @Override
    public Integer getSmtpPort() {
        return smtpPort;
    }

}
