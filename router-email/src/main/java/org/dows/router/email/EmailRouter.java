package org.dows.router.email;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.router.core.Routable;
import org.dows.router.core.RouterFactory;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailRouter implements Routable {


    @Override
    public void dispatch(String op, Object beforeEntity, Object afterEntity) {



        //RouterFactory.getRouter();
        /**
         * 邮箱地址
         * lait.zhang@gmail.com
         * 授权码/口令
         * 请输入邮箱授权码（非登录密码）
         * 注意：此处需要输入的是邮箱服务商提供的SMTP/IMAP授权码，而非邮箱登录密码。如QQ邮箱、163邮箱等需要在邮箱设置中开启SMTP/IMAP服务并获取授权码。
         *
         * 协议类型 IMAP,SMTP
         * 服务器地址
         * imap.gmail.com
         * 端口号
         */
        //
    }
}
