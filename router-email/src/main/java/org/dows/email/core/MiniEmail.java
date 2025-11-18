package org.dows.email.core;

import org.dows.email.constant.EmailContentTypeEnum;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * 邮件操作接口
 */
public interface MiniEmail {

    /**
     * 发送纯文本邮件给单个邮箱
     *
     * @param to      收件邮箱
     * @param content 发送内容
     * @return 发送成功的邮箱
     * @since 1.0.0
     */
    String send(String to, String content);

    /**
     * 批量发送纯文本邮件给多个邮箱
     *
     * @param tos     收件邮箱集合
     * @param content 发送内容
     * @return 发送成功的邮箱集合
     * @since 1.0.0
     */
    List<String> send(String[] tos, String content);

    /**
     * 发送给单个邮箱
     *
     * @param to          收件邮箱
     * @param subject     邮件主题
     * @param contentType 邮件内容格式类型
     * @param content     发送内容
     * @return 发送成功的邮箱
     * @since 1.3.0
     */
    String send(String to, String subject, EmailContentTypeEnum contentType, String content);

    /**
     * 发送给多个邮箱
     *
     * @param tos         收件邮箱
     * @param subject     邮件主题
     * @param contentType 邮件内容格式类型
     * @param content     发送内容
     * @return 发送成功的邮箱集合
     * @since 1.3.0
     */
    List<String> send(String[] tos, String subject, EmailContentTypeEnum contentType, String content);

    /**
     * 添加附件文件（本地文件）
     *
     * @param file     附件文件
     * @param fileName 附件文件别名
     * @return 返回自身，支持链式赋值。
     * @since 1.0.0
     */
    MiniEmail addAttachment(File file, String fileName);

    /**
     * 添加附件文件（网络链接文件）
     *
     * @param url     附件链接
     * @param urlName 附件链接别名
     * @return 返回自身，支持链式赋值。
     * @since 1.0.0
     */
    MiniEmail addAttachment(URL url, String urlName);

    /**
     * 添加抄送邮箱
     *
     * @param carbonCopies 抄送邮箱
     * @return 返回自身，支持链式赋值。
     * @since 1.1.1
     */
    MiniEmail addCarbonCopy(String[] carbonCopies);

    /**
     * 添加密抄邮箱
     *
     * @param blindCarbonCopies 密抄邮箱
     * @return 返回自身，支持链式赋值。
     * @since 1.3.0
     */
    MiniEmail addBlindCarbonCopy(String[] blindCarbonCopies);
}
