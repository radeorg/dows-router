package org.dows.router.email.strategy;

import org.dows.router.email.entity.Mail;

public interface MailReader {

    /**
     * 接收邮件
     *
     * @param mail ：邮件信息
     */
    void receive(Mail mail);
}
