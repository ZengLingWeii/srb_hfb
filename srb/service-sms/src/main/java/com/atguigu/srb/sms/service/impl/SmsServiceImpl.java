package com.atguigu.srb.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.common.exception.Assert;
import com.atguigu.common.exception.BusinessException;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.sms.service.SmsService;
import com.atguigu.srb.sms.util.SmsProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {
    @Override
    public void send(String mobile, String templateCode, Map<String, Object> param) {

        //创建远程连接客户端对象
        DefaultProfile profile = DefaultProfile.getProfile(
                SmsProperties.REGION_Id,//sms服务器城市
                SmsProperties.KEY_ID,//你的id名称
                SmsProperties.KEY_SECRET);//你的服务密码
        IAcsClient client = new DefaultAcsClient(profile);

        //创建远程连接的请求参数
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", SmsProperties.REGION_Id);
        request.putQueryParameter("PhoneNumbers", mobile);//通讯地址
        request.putQueryParameter("SignName", SmsProperties.SIGN_NAME);
        request.putQueryParameter("TemplateCode", templateCode);//短信模板

        //将参数param转为json格式,并将param参数放入请求TemplateParam中
        Gson gson = new Gson();
        String json = gson.toJson(param);
        request.putQueryParameter("TemplateParam", json);

        try {
            //使用客户端对象携带请求对象发送请求并得到响应结果
            CommonResponse response = client.getCommonResponse(request);
            boolean success = response.getHttpResponse().isSuccess();
            //ALIYUN_RESPONSE_FAIL(-501, "阿里云响应失败"),
            Assert.isTrue(success, ResponseEnum.ALIYUN_RESPONSE_FAIL);

            //响应成功的情况下得到响应数据
            String data = response.getData();
            HashMap<String, String> resultMap = gson.fromJson(data, HashMap.class);
            String code = resultMap.get("Code");
            String message = resultMap.get("Message");
            log.info("阿里云短信发送响应结果：");
            log.info("code：" + code);
            log.info("message：" + message);

            //ALIYUN_SMS_LIMIT_CONTROL_ERROR(-502, "短信发送过于频繁"),//业务限流
            Assert.notEquals("isv.BUSINESS_LIMIT_CONTROL", code, ResponseEnum.ALIYUN_SMS_LIMIT_CONTROL_ERROR);
            //ALIYUN_SMS_ERROR(-503, "短信发送失败"),//其他失败
            Assert.equals("OK", code, ResponseEnum.ALIYUN_SMS_ERROR);
        } catch (ServerException e) {
            log.error("阿里云短信发送SDK调用失败：");
            log.error("ErrorCode=" + e.getErrCode());
            log.error("ErrorMessage=" + e.getErrMsg());
            throw new BusinessException(ResponseEnum.ALIYUN_SMS_ERROR , e);
        } catch (ClientException e) {
            log.error("阿里云短信发送SDK调用失败：");
            log.error("ErrorCode=" + e.getErrCode());
            log.error("ErrorMessage=" + e.getErrMsg());
            throw new BusinessException(ResponseEnum.ALIYUN_SMS_ERROR , e);
        }
    }
}
