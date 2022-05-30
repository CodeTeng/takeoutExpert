package com.lt.reggie.common;

/**
 * @description: 自定义业务异常
 * @author: 狂小腾
 * @date: 2022/5/27 16:49
 */
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}
