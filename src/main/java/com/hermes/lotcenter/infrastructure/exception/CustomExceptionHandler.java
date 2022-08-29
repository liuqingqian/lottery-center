package com.hermes.lotcenter.infrastructure.exception;

import com.beicai.common.exception.DBException;
import com.beicai.common.exception.RpcException;
import com.beicai.common.restful.ErrorCode;
import com.beicai.common.restful.Response;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import java.time.format.DateTimeParseException;

@ControllerAdvice
@ResponseBody
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(RpcException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response handleRpcException(RpcException e) {
        log.error("RPC异常", e);
        return Response.error(ErrorCode.RPC_EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response handleDateTimeParseException(DateTimeParseException e) {
        log.error("时间格式错误", e);
        return Response.error(ErrorCode.PARAM_INVALID, "时间格式错误!");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleNotFoundException(NoHandlerFoundException e) {
        log.error("NoHandlerFound异常", e);
        return Response.error(ErrorCode.PARAM_INVALID, e.getMessage());
    }

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handlerFeignException(FeignException feignException) {
        log.error("exception:{}", feignException);
        return Response.error(ErrorCode.RPC_EXCEPTION, "调用外部服务异常");
    }

    @ExceptionHandler(ServletException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response handleServletException(ServletException e) {
        log.error("请求数据异常", e);
        return Response.error(ErrorCode.PARAM_INVALID, "请求数据异常");
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response handleValidationBindException(BindException e) {
        log.error("请求参数异常", e);
        return Response.error(ErrorCode.PARAM_INVALID, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(DBException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleDBException(DBException e) {
        log.error("数据库异常", e);
        return Response.error(ErrorCode.UNKNOWN_EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleException(Exception e) {
        log.error("后台服务异常", e);
        return Response.error(ErrorCode.UNKNOWN_EXCEPTION);
    }
}
