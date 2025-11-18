package org.dows.email.exception;

/**
 * 参数检查异常
 */
public class ParameterException extends RuntimeException {
    public ParameterException(String message) {
        super(message);
    }
}
