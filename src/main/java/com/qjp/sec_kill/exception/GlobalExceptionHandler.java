package com.qjp.sec_kill.exception;

import com.qjp.sec_kill.result.CodeMsg;
import com.qjp.sec_kill.result.Result;
import org.springframework.http.HttpRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * description: GlobalExceptionHandler
 * date: 2020/5/18 23:53
 * author: 雨夜微凉
 * version: 1.0
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e){
        e.printStackTrace();
        //全局异常（用户不存在，密码不对等）
        if(e instanceof GlobalException){
            GlobalException ex = (GlobalException)e;
            return Result.error(ex.getCm());
        }
        //参数绑定异常（例如表单传来的数据和vo的数据没有匹配上）
        else if(e instanceof BindException) {
            BindException ex = (BindException)e;
            List<ObjectError> errors = ex.getAllErrors();
            //为了简单起见只取第一个，取多少取决于自己的业务需求
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }

    }
}
