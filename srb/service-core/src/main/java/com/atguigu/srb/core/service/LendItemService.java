package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.Lend;
import com.atguigu.srb.core.pojo.entity.LendItem;
import com.atguigu.srb.core.pojo.vo.InvestVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 服务类
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */
public interface LendItemService extends IService<LendItem> {

    List<LendItem> selectByLendId(Long lendId, Integer status);

    String commitInvest(InvestVO investVO);

    void notify(Map<String, Object> paramMap);

    List<LendItem> selectByLendId(Long lendId);
}
