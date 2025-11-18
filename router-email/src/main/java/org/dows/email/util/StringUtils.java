package org.dows.email.util;


/**
 * 字符串工具类
 */
public class StringUtils {

    private StringUtils() {
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }
}
