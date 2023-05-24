package com.atguigu.common.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class R {

    private Integer code;
    private String message;
    private Map<String,Object> data = new HashMap<>();

    /*
    * 构造器私有
    * */
    private R(){}

    /*
    * 返回成功
    * */
    public static R ok(){
        R r = new R();
        r.setCode(ResponseEnum.SUCCESS.getCode());
        r.setMessage(ResponseEnum.SUCCESS.getMessage());
        return r;
    }

    /*
    * 返回失败
    * */
    public static R error(){
        R r = new R();
        r.setCode(ResponseEnum.ERROR.getCode());
        r.setMessage(ResponseEnum.ERROR.getMessage());
        return r;
    }

    /*
    * 返回其它结果
    * */
    public static R setResult(ResponseEnum responseEnum){
        R r = new R();
        r.setCode((responseEnum.getCode()));
        r.setMessage(responseEnum.getMessage());
        return r;
    }

    /*
    * 设置其它属性
    * */
    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R data(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    public R data(Map<String,Object> map){
        this.setData(map);
        return this;
    }
}
