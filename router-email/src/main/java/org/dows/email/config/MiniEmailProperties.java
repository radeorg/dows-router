package org.dows.email.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 属性配置类
 */
@Data
@ConfigurationProperties(prefix = "org.dows.email")
public class MiniEmailProperties {

    /**
     * 发件人邮箱
     */
    private String username;
    /**
     * 发件人邮箱密码（qq邮箱、163邮箱需要的是邮箱授权码，新浪邮箱直接是邮箱登录密码）
     */
    private String password;
    /**
     * 发件人定制别名。
     */
    private String senderNickname;

    /**
     * 邮箱Host
     */
    private String smtpHost;

    /**
     * 邮箱port
     */
    private Integer smtpPort;

    /**
     * 是否开启debug。默认false
     */
    private Boolean mailDebug = Boolean.FALSE;
    /**
     * 自定义 MiniEmail实现类路径。类必须继承 BaseMiniEmail
     */
    private String customMiniEmail;
    /**
     * 开启账号密码鉴权。默认true
     */
    private Boolean mailSmtpAuth = Boolean.TRUE;
    /**
     * 开启SSL。默认true
     */
    private Boolean mailSmtpSslEnable = Boolean.TRUE;
    /**
     * 超时时间。默认10s
     */
    private Long mailSmtpTimeout = 10000L;
}
