package com.lt.reggie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 通用返回结果，服务端响应的数据最终都会封装成此对象
 * @author: 狂小腾
 * @date: 2022/5/26 15:08
 */
@Data
public class Result<T> {

    /**
     * 返回编码 1---成功 0和其他数字---失败
     */
    private Integer code;

    /**
     * 返回错误信息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 动态数据
     */
    private Map map = new HashMap();

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<>();
        result.code = 1;
        result.data = object;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = 0;
        return result;
    }

    public Result<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
