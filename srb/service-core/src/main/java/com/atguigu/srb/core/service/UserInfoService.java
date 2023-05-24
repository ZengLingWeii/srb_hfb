package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.query.UserInfoQuery;
import com.atguigu.srb.core.pojo.vo.LoginVO;
import com.atguigu.srb.core.pojo.vo.RegisterVO;
import com.atguigu.srb.core.pojo.vo.UserIndexVO;
import com.atguigu.srb.core.pojo.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */
public interface UserInfoService extends IService<UserInfo> {

    //注册会员用户
    void register(RegisterVO registerVO);

    //会员登入
    UserInfoVO login(LoginVO loginVO, String ip);

    //会员分页查询
    IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery);

    //用户锁定
    void lock(Long id, Integer status);

    //判断手机号是否存在(OpenFeign)
    boolean checkMobile(String mobile);

    UserIndexVO getIndexUserInfo(Long userId);

    String getMobileByBindCode(String bindCode);
}
