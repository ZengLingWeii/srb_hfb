package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.bo.TransFlowBO;
import com.atguigu.srb.core.pojo.entity.TransFlow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 交易流水表 服务类
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */
public interface TransFlowService extends IService<TransFlow> {

    //将充值交易数据插入到流水数据库中
    void saveTransFlow(TransFlowBO transFlowBO);

    //接口等幂性原则
    boolean isSaveTransFlow(String agentBillNo);

    List<TransFlow> selectByUserId(Long userId);
}
