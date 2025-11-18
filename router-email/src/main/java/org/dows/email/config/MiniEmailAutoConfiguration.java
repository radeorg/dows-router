package org.dows.email.config;

import org.dows.email.core.MiniEmail;
import org.dows.email.core.MiniEmailFactory;
import org.dows.email.core.MiniEmailFactoryBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 建立 MiniEmail
 */
@Configuration
@EnableConfigurationProperties(value = {MiniEmailProperties.class})
public class MiniEmailAutoConfiguration {

    @ConditionalOnMissingBean(MiniEmail.class)
    @Bean
    public MiniEmail miniEmail(MiniEmailProperties properties) {
        MailConfig mailConfig = new MailConfig().setUsername(properties.getUsername())
                .setPassword(properties.getPassword())
                .setSenderNickname(properties.getSenderNickname())
                .setSmtpHost(properties.getSmtpHost()).setSmtpPort(properties.getSmtpPort())
                .setMailDebug(properties.getMailDebug()).setCustomMiniEmail(properties.getCustomMiniEmail())
                .setMailSmtpAuth(properties.getMailSmtpAuth()).setMailSmtpSslEnable(properties.getMailSmtpSslEnable())
                .setMailSmtpTimeout(properties.getMailSmtpTimeout());

        // 构建工厂类
        MiniEmailFactory factory = new MiniEmailFactoryBuilder().build(mailConfig);
        // 初始化
        MiniEmail init = factory.init();
        return init;
    }

}
