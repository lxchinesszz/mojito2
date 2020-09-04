package com.hanframework.mojito.exception;

/**
 * @author liuxin
 * 2020-09-03 22:37
 */
public class SubServerHandlerException extends RuntimeException {


    public SubServerHandlerException() {
    }

    public SubServerHandlerException(String message) {
        super(message);
    }

    public SubServerHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubServerHandlerException(Throwable cause) {
        super(cause);
    }

    public SubServerHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}