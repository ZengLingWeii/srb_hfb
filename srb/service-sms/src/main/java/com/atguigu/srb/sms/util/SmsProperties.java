package com.atguigu.srb.sms.util;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
//注意prefix要写到最后一个 "." 符号之前
//调用setter为成员赋值
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsProperties implements InitializingBean {

    private String regionId;
    private String keyId;
    private String keySecret;
    private String templateCode;
    private String signName;

    public static String REGION_Id;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String TEMPLATE_CODE;
    public static String SIGN_NAME;

    //当私有成员被赋值后，此方法自动被调用，从而初始化常量
    @Override
    public void afterPropertiesSet() throws Exception {

        REGION_Id = regionId;
        KEY_ID = keyId;
        KEY_SECRET = keySecret;
        TEMPLATE_CODE = templateCode;
        SIGN_NAME = signName;

    }
}
