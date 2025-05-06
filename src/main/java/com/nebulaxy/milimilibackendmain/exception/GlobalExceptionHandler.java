package com.nebulaxy.milimilibackendmain.exception;

import com.nebulaxy.milimilibackendmain.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 全局异常捕获器
 */
@ControllerAdvice("com.nebulaxy.milimilibackendmain.controller")
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody//返回json串
    public Result error(Exception e) {
        log.error("系统异常",e);
        return Result.error("系统异常");
    }

    @ExceptionHandler(CustomerException.class)
    @ResponseBody
    public Result customerError(CustomerException e) {
        return Result.error(e.getCode(),e.getMessage());
    }
}
