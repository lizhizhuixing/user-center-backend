package com.yupi.usercenter.exception;

import com.yupi.usercenter.commom.BaseResponse;
import com.yupi.usercenter.commom.ErrorCode;
import com.yupi.usercenter.commom.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestController
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends RuntimeException{
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("BusinessException:{}",e);
        return ResultUtils.error(e.getCode(),e.getMessage()," ");

    }
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(BusinessException e){
        log.error("RuntimeException:{}",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage()," ");
    }
}
