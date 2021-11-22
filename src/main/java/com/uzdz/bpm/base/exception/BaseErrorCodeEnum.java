package com.uzdz.bpm.base.exception;

import lombok.Getter;

public enum BaseErrorCodeEnum {

    /**
     * 系统异常
     */
    SYSTEM_DEFAULT_ERROR(-1, "对不起，系统发生了异常"),
    SYSTEM_BLOCK(-2, "系统繁忙，请稍后重试"),
    PASSWORD_ERROR(10001, "用户名密码错误"),
    NEED_LOGIN(10002, "请前往登陆"),
    INSUFFICIENT_PERMISSIONS(10003, "暂无访问权限"),
    ;

    BaseErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Getter
    private int code;

    @Getter
    private String msg;
}