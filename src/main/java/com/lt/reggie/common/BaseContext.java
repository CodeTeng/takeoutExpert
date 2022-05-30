package com.lt.reggie.common;

/**
 * @description: 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 * @author: 狂小腾
 * @date: 2022/5/27 16:05
 */
public class BaseContext {

    /**
     * 客户端发送的每次http请求，对应的在服务端都会分配一个新的线程来处理，
     * 在处理过程中涉及到的有些方法都属于相同的一个线程
     * eg:LoginCheckFilter的doFilter方法
     *    EmployeeController的update方法
     *    MyMetaObjectHandler的updateFill方法
     * 可以打印线程号验证
     */
    private static ThreadLocal<Long> threadLocal =new ThreadLocal<>();

    /**
     * 设置值
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取值
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }

}
