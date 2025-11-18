package org.dows.router.email.strategy.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dows.router.email.entity.Mail;
import org.dows.router.email.strategy.MailWriter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * 简单文本发送邮件
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SimpleMailWriter implements MailWriter {
    private static final Logger LOG = LogManager.getLogger("bizLog");

    private final JavaMailSender javaMailSender;

    @Override
    public void send(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        try {
            message.setFrom(mail.getFrom());
            message.setTo(mail.getTo());
            message.setSubject(mail.getTitle());
            message.setText(mail.getContent());
            javaMailSender.send(message);
            LOG.info("纯文本的邮件已经发送给【{}】。", mail.getTo());
        } catch (Exception e) {
            LOG.error("纯文本邮件发送时发生异常！", e);
        }
    }

}
