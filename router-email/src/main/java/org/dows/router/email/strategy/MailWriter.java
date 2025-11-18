package org.dows.router.email.strategy;

import org.dows.router.email.entity.Mail;

/**
 * 发送邮件策略接口
 */
public interface MailWriter {

    /**
     * 发送邮件
     *
     * @param mail ：邮件信息
     */
    void send(Mail mail);



}
