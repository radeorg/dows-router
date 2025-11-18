package org.dows.email.core;


import org.dows.email.constant.EmailContentTypeEnum;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

/**
 * 基础邮件模板
 */
public class DefaultMiniEmail extends BaseMiniEmail {
    private static final Logger log = LoggerFactory.getLogger(DefaultMiniEmail.class);

    public DefaultMiniEmail(MimeMessage msg, String fromName) {
        super(msg, fromName);
    }

    @Override
    public String send(String to, String content) {
        return this.send(to, null, EmailContentTypeEnum.TEXT, content);
    }

    @Override
    public List<String> send(String[] tos, String content) {
        return this.send(tos, null, EmailContentTypeEnum.TEXT, content);
    }

    @Override
    public String send(String to, String subject, EmailContentTypeEnum contentType, String content) {
        return super.send(to, subject, contentType, content);
    }

    @Override
    public List<String> send(String[] tos, String subject, EmailContentTypeEnum contentType, String content) {
        return super.send(tos, subject, contentType, content);
    }

    @Override
    public MiniEmail addAttachment(File file, String fileName) {
        try {
            super.setDataHandler(new DataHandler(new FileDataSource(file)), fileName);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.warn("添加附件失败，将跳过此附件", e);
        }

        return this;
    }

    @Override
    public MiniEmail addAttachment(URL url, String urlName) {
        try {
            super.setDataHandler(new DataHandler(url), urlName);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.warn("添加附件失败，将跳过此附件", e);
        }

        return this;
    }

    @Override
    public MiniEmail addCarbonCopy(String[] carbonCopies) {
        try {
            super.addRecipients(carbonCopies, Message.RecipientType.CC);
        } catch (MessagingException e) {
            log.warn("添加抄送邮箱失败，将跳过此步骤", e);
        }

        return this;
    }

    @Override
    public MiniEmail addBlindCarbonCopy(String[] blindCarbonCopies) {
        try {
            super.addRecipients(blindCarbonCopies, Message.RecipientType.BCC);
        } catch (MessagingException e) {
            log.warn("添加密抄邮箱失败，将跳过此步骤", e);
        }

        return this;
    }

}
