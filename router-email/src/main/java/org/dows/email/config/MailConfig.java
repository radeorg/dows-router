package org.dows.email.config;

/**
 * 邮件配置项 封装类
 */
public class MailConfig {
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

    public static MailConfig config(String username, String password) {
        return new MailConfig().setUsername(username).setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public MailConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MailConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public MailConfig setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
        return this;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public MailConfig setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
        return this;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public MailConfig setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
        return this;
    }

    public Boolean getMailDebug() {
        return mailDebug;
    }

    public MailConfig setMailDebug(Boolean mailDebug) {
        this.mailDebug = mailDebug;
        return this;
    }

    public String getCustomMiniEmail() {
        return customMiniEmail;
    }

    public MailConfig setCustomMiniEmail(String customMiniEmail) {
        this.customMiniEmail = customMiniEmail;
        return this;
    }

    public Boolean getMailSmtpAuth() {
        return mailSmtpAuth;
    }

    public MailConfig setMailSmtpAuth(Boolean mailSmtpAuth) {
        this.mailSmtpAuth = mailSmtpAuth;
        return this;
    }

    public Boolean getMailSmtpSslEnable() {
        return mailSmtpSslEnable;
    }

    public MailConfig setMailSmtpSslEnable(Boolean mailSmtpSslEnable) {
        this.mailSmtpSslEnable = mailSmtpSslEnable;
        return this;
    }

    public Long getMailSmtpTimeout() {
        return mailSmtpTimeout;
    }

    public MailConfig setMailSmtpTimeout(Long mailSmtpTimeout) {
        this.mailSmtpTimeout = mailSmtpTimeout;
        return this;
    }

}
