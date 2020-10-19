package com.zoom.exception;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/10 22:19
 */
public class ConnectionOverflowException extends Exception{
    public ConnectionOverflowException(String message) {
        super(message);
    }

    public ConnectionOverflowException() {
    }
}
