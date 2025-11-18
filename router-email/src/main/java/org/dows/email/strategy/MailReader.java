package org.dows.email.strategy;

import org.dows.email.entity.Mail;

public interface MailReader {

    /**
     * 接收邮件
     *
     * @param mail ：邮件信息
     */
    void receive(Mail mail);
}
