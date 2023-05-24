package com.atguigu.common.exception;

import com.atguigu.common.result.ResponseEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.plaf.nimbus.NimbusStyle;

/*
* 自定义异常类
* */
@Data
@NoArgsConstructor
public class BusinessException extends RuntimeException{

    //状态码
    private Integer code;
    //状态信息
    private String message;

    //带消息的异常对象构造器
    public BusinessException(String message){
        this.message = message;
    }

    //带状态码和消息的异常对象构造器
    public BusinessException(String message,Integer code){
        this.message = message;
        this.code = code;
    }

    //带状态码、消息、异常信息的构造器
    public BusinessException(String message,Integer code,Throwable e){
        super(e);
        this.message = message;
        this.code = code;
    }

    //根据枚举抛异常对象
    public BusinessException(ResponseEnum responseEnum){
        this.code = responseEnum.getCode();
        this.message  =responseEnum.getMessage();
    }

    //根据枚举和异常来抛异常
    public BusinessException(ResponseEnum responseEnum,Throwable e){
        super(e);
        this.code = responseEnum.getCode();
        this.message  =responseEnum.getMessage();
    }

}
