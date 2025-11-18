package org.dows.email.builder;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import org.dows.email.config.MailConfig;
import org.eclipse.angus.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * 初始收件人身份登录session信息
 */
public class EmailSessionBuilder {
    private static final Logger log = LoggerFactory.getLogger(EmailSessionBuilder.class);

    private Properties props;

    public EmailSessionBuilder(MailConfig config) {
        this.setProperties(config);
    }

    private void setProperties(MailConfig config) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", config.getMailSmtpAuth());
        props.put("mail.smtp.ssl.enable", config.getMailSmtpSslEnable());
        // outlook 邮箱是 starttls 认证
        props.put("mail.smtp.starttls.enable", Boolean.TRUE);
        // 简化证书认证，解决发送邮件时出现 ssl 错误
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
            log.warn("socketFactory 构建失败：", e);
        }

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.timeout", config.getMailSmtpTimeout());
        props.put("mail.smtp.host", config.getSmtpHost());
        props.put("mail.smtp.port", config.getSmtpPort());

        props.setProperty("username", config.getUsername());
        props.setProperty("password", config.getPassword());

        props.put("mail.debug", config.getMailDebug().toString());

        this.props = props;
    }

    public Session parseSession() {
        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(getProps("username"), getProps("password"));
            }
        });
    }

    public String getProps(String key) {
        return props.getProperty(key);
    }
}
