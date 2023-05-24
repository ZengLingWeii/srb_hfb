package com.atguigu.srb.sms.client;

import com.atguigu.srb.sms.client.fallback.CoreUserInfoClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-core",fallback = CoreUserInfoClientFallback.class)//链接远程服务调用的服务端
public interface CoreUserInfoClient {

    @GetMapping("/api/core/userInfo/checkMobile/{mobile}")
    boolean checkMobile(@PathVariable String mobile);
}
