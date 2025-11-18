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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

/**
 * 自定义模板格式发送邮件
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TemplateMailWriter implements MailWriter {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void send(Mail mail) {
        final Context ctx = new Context(new Locale(""));
        if (null != mail.getMaps() && !mail.getMaps().isEmpty()) {
            for (Map.Entry<String, Object> entry : mail.getMaps().entrySet()) {
                ctx.setVariable(entry.getKey(), entry.getValue());
            }
        }
        final String htmlContent = templateEngine.process(mail.getTemplateName(), ctx);
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        try {
            message.setFrom(mail.getFrom());
            message.setTo(mail.getTo());
            message.setSubject(mail.getTitle());
            message.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
            log.info("模板邮件已经发送{}。", mail.getTo());
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("发送模板邮件【{}】时发生异常！", mail.getTemplateName());
        }
    }
}
