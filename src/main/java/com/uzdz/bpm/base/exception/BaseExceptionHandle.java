package com.uzdz.bpm.base.exception;

import com.uzdz.bpm.base.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Order(9999)
@ControllerAdvice
public class BaseExceptionHandle {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handle(Exception ex) {
        if (ex instanceof BizException) {
            return Response.nonExpect(((BizException) ex).getErrCode(), ex.getMessage());
        }

        log.error("服务发生异常：", ex);

        return Response.nonExpect(BaseErrorCodeEnum.SYSTEM_DEFAULT_ERROR.getCode()).message(BaseErrorCodeEnum.SYSTEM_DEFAULT_ERROR.getMsg());
    }
}