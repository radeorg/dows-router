package org.dows.router.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.router.email.entity.Mail;
import org.dows.router.email.factory.MailFactory;
import org.dows.router.email.strategy.MailWriter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailClient {

    private final Map<String, MailWriter> mailSenders;

    /**
     * 执行发送邮件
     */
    private void dispatch(Mail mail) {

    }


   public void send(Mail mail){

       String key = MailFactory.get(mail.getContentType());
       this.mailSenders.get(key).send( mail);
   }

    /**
     * 接收邮件
     *
     * @param mail ：邮件信息
     */
    void receive(Mail mail){

        String key = MailFactory.get(mail.getContentType());
        this.mailSenders.get(key).send( mail);
    }
}
