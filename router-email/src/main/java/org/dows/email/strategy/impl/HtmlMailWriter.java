package org.dows.email.strategy.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.email.entity.Mail;
import org.dows.email.strategy.MailWriter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


/**
 * HTML格式发送邮件
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class HtmlMailWriter implements MailWriter {

    private final JavaMailSender javaMailSender;

    @Override
    public void send(Mail mail) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mail.getFrom());
            helper.setTo(mail.getTo());
            helper.setSubject(mail.getTitle());
            helper.setText(mail.getContent(), true);
            javaMailSender.send(message);
            log.info("html邮件已经发送{}。", mail.getTo());
        } catch (MessagingException e) {
            log.error("发送html邮件时发生异常！", e);
        }
    }
}
