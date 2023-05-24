package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.UserLoginRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务类
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */
public interface UserLoginRecordService extends IService<UserLoginRecord> {

    //查询用户历史登入记录
    List<UserLoginRecord> listTop50(Long userId);
}
