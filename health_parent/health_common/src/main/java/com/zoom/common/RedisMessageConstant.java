package com.zoom.common;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/14 22:29
 * 手机验证码常量类
 */
public class RedisMessageConstant {
    public static final String SENDTYPE_ORDER = "001";//用于缓存体检预约时发送的验证码
    public static final String SENDTYPE_LOGIN = "002";//用于缓存手机号快速登录时发送的验证码
    public static final String SENDTYPE_GETPWD = "003";//用于缓存找回密码时发送的验证码
}
