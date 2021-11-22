package com.uzdz.bpm.base.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Method;

@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException {
    protected int errCode;
    protected String errMessage;
    protected Object data;

    public BizException() {
        this(BaseErrorCodeEnum.SYSTEM_DEFAULT_ERROR.getCode(), BaseErrorCodeEnum.SYSTEM_DEFAULT_ERROR.getMsg());
    }

    public BizException(String errMessage) {
        this(BaseErrorCodeEnum.SYSTEM_DEFAULT_ERROR.getCode(), errMessage, null);
    }

    public BizException(int errCode, String errMessage) {
        this(errCode, errMessage, null);
    }

    public BizException(int errCode, String errMessage, Object data) {
        this(errCode, errMessage, data, null);
    }

    public BizException(int errCode, String errMessage, Object data, Throwable cause) {
        super(errMessage, cause);
        this.errCode = errCode;
        this.errMessage = errMessage;
        this.data = data;
    }

    public BizException(int errCode, String errMessage, Throwable cause) {
        super(errMessage, cause);
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public static BizException with(Enum obj) {
        return with(obj, null);
    }

    public static BizException with(Enum obj, Throwable cause) {
        Class<?> aClass = obj.getClass();

        try {
            if (aClass.isEnum()) {
                // 如果是枚举类则代表枚举异常
                Method codeMethod = aClass.getDeclaredMethod("getCode");
                Method msgMethod = aClass.getDeclaredMethod("getMsg");

                return new BizException((int)codeMethod.invoke(obj), (String)msgMethod.invoke(obj), cause);
            }
        } catch (Exception e) {
            // recover 抛出系统异常
            obj = null;
        }

        return new BizException(BaseErrorCodeEnum.SYSTEM_DEFAULT_ERROR.getCode(), BaseErrorCodeEnum.SYSTEM_DEFAULT_ERROR.getMsg(), obj, cause);
    }
}