package com.atguigu.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "投标信息")
public class InvestVO {

    //标的id
    private Long lendId;

    //投标金额
    private String investAmount;

    //投资人id
    private Long investUserId;

    //用户姓名
    private String investName;
}