package com.uzdz.bpm.base;

import com.alibaba.fastjson.JSON;
import com.uzdz.bpm.base.exception.BaseErrorCodeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Getter
@Setter
@Slf4j
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 6686419048438176898L;

    private Integer error;

    private String message;

    private T body;

    public Response<T> error(int error) {
        this.error = error;
        return this;
    }

    public Response<T> body(T body) {
        this.body = body;
        return this;
    }

    private Response() {
    }

    private Response(int status, String message, T body) {
        this.error = status;
        this.message = message;
        this.body = body;
    }

    public static Response ofEnum(BaseErrorCodeEnum v) {
        if (v == null) {
            return ofEnum(BaseErrorCodeEnum.SYSTEM_BLOCK);
        }

        return new Response().error(v.getCode()).message(v.getMsg());
    }

    /**
     * 如果出现错误在返回时候进行判断，避免大量的异常堆栈
     *
     * @param body
     * @param <T>
     * @return
     */
    public static <T> Response getInstance(T body) {
        return new Response<T>(0, null, body);
    }

    public static Response ok() {
        return new Response().error(0);
    }

    public static Response nonExpect(int error) {
        return new Response().error(error);
    }

    public static Response nonExpect(int error, String message) {
        return new Response().error(error).message(message);
    }

    @Override
    public String toString() {
        if (null == this.body) {
            this.body((T) new Object());
        }
        return JSON.toJSONString(this);
    }

    public Response message(String message) {
        this.message = message;
        return this;
    }

    public boolean hasError() {
        if (error!=null && error!=0){
            return true;
        }
        return false;
    }
}
