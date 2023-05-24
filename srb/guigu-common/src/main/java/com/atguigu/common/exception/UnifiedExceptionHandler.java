package com.atguigu.common.exception;

import com.atguigu.common.result.R;
import com.atguigu.common.result.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLSyntaxErrorException;

/*
* 统一异常处理器
* */

@Slf4j
@RestControllerAdvice
@Component
public class UnifiedExceptionHandler {

    /*
    * 统一异常处理
    * */
    @ExceptionHandler(value = Exception.class) //当controller抛出Exception则捕获
    public R handleException(Exception e) {
        log.info(e.getMessage(),e);
        return R.error();
    }

    /*
    * 特殊异常处理
    * */
    @ExceptionHandler(value = SQLSyntaxErrorException.class) //SQLSyntaxErrorException特点异常捕获
    public R handleBadSqlGrammarException(SQLSyntaxErrorException e) {
        log.info(e.getMessage(),e);
        return R.setResult(ResponseEnum.BAD_SQL_GRAMMAR_ERROR);
    }

    /*
    * 自定义异常的方式处理异常
    * */
    @ExceptionHandler(value = BusinessException.class) //SQLSyntaxErrorException特点异常捕获
    public R handleException(BusinessException e) {
        log.info(e.getMessage(),e);
        return R.error().message(e.getMessage()).code(e.getCode());
    }


    /*
    * 前端工程异常处理(controller上一层异常)
    * */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,   //请求方法不对(Get/Post/Delete/Put)
            HttpMediaTypeNotSupportedException.class,
            MissingPathVariableException.class,     //@PathVariable
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,  //前端返回参数不合法(后台接受json格式,前台传非json)
            HttpMessageNotWritableException.class,
            MethodArgumentNotValidException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public R handleServletException(Exception e) {
        log.error(e.getMessage(),e);
        //SERVLET_ERROR(-102, "servlet请求异常"),
        return R.error().message(ResponseEnum.SERVLET_ERROR.getMessage()).code(ResponseEnum.SERVLET_ERROR.getCode());
    }
}
