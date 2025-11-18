package org.dows.router.email.factory;

import org.dows.router.email.enums.ContentType;

import java.util.HashMap;
import java.util.Map;

/**
 * 邮件发送bean工厂类
 */
public class MailFactory {

    private MailFactory() {}

    private static final Map<String, String> maps = new HashMap<>();

    static {
        maps.put(ContentType.TEXT.value(), getStrategyClassName("simple"));
        maps.put(ContentType.HTML.value(), getStrategyClassName("html"));
        maps.put(ContentType.TEMPLATE.value(), getStrategyClassName("template"));
    }


    public static String get(String type) {
        return maps.get(type);
    }

    /**
     * 获取策略类名
     *
     * @param classNamePrefix：类名前缀
     * @return 策略类名
     */
    private static String getStrategyClassName(String classNamePrefix) {
        return classNamePrefix + "MailWriter";
    }

}
