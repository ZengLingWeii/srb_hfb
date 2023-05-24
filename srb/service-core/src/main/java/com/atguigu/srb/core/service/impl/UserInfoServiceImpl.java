package com.atguigu.srb.core.service.impl;

import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.common.util.MD5;
import com.atguigu.srb.base.util.JwtUtils;
import com.atguigu.srb.core.mapper.UserAccountMapper;
import com.atguigu.srb.core.mapper.UserLoginRecordMapper;
import com.atguigu.srb.core.pojo.entity.UserAccount;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.entity.UserLoginRecord;
import com.atguigu.srb.core.pojo.query.UserInfoQuery;
import com.atguigu.srb.core.pojo.vo.LoginVO;
import com.atguigu.srb.core.pojo.vo.RegisterVO;
import com.atguigu.srb.core.pojo.vo.UserIndexVO;
import com.atguigu.srb.core.pojo.vo.UserInfoVO;
import com.atguigu.srb.core.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserAccountMapper userAccountMapper;

    @Resource
    private UserLoginRecordMapper userLoginRecordMapper;

    @Override
    @Transactional(rollbackFor = {Exception.class})//开启事务回滚
    public void register(RegisterVO registerVO) {

        //判断用户是否已被注册
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",registerVO.getMobile());
        Integer count = baseMapper.selectCount(queryWrapper);
        //MOBILE_EXIST_ERROR(-207,"手机号已被注册")
        Assert.isTrue(count==0, ResponseEnum.MOBILE_EXIST_ERROR);

        //插入用户信息记录: user_info
        UserInfo userInfo = new UserInfo();
        userInfo.setUserType(registerVO.getUserType());
        userInfo.setNickName(registerVO.getMobile());
        userInfo.setName(registerVO.getMobile());
        userInfo.setMobile(registerVO.getMobile());
        userInfo.setPassword(MD5.encrypt(registerVO.getPassword()));//密码加密存储
        userInfo.setStatus(UserInfo.STATUS_NORMAL);
        //设置一张静态资源服务器上的头像
        userInfo.setHeadImg("https://zlw-srb-file.oss-cn-hangzhou.aliyuncs.com/avater/aliyun.png");
        baseMapper.insert(userInfo);

        //插入用户账户记录: user_account
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userInfo.getId());
        userAccountMapper.insert(userAccount);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})//开启事务回滚
    public UserInfoVO login(LoginVO loginVO, String ip) {

        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        Integer userType = loginVO.getUserType();

        //获取会员
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        queryWrapper.eq("user_type",userType);
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);

        //用户不存在
        //LOGIN_MOBILE_ERROR(-208,"用户不存在")
        Assert.notNull(userInfo,ResponseEnum.LOGIN_MOBILE_ERROR);

        //校验密码
        //LOGIN_PASSWORD_ERROR(-209,"密码不正确")
        Assert.equals(MD5.encrypt(password),userInfo.getPassword(),ResponseEnum.LOGIN_PASSWORD_ERROR);

        //用户是否被禁用
        //LOGIN_DISABLED_ERROR(-210,"用户已被禁用")
        Assert.equals(userInfo.getStatus(),UserInfo.STATUS_NORMAL,ResponseEnum.LOGIN_LOKED_ERROR);

        //记录登入日志
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(userInfo.getId());
        userLoginRecord.setIp(ip);
        userLoginRecordMapper.insert(userLoginRecord);

        //生成token
        String token = JwtUtils.createToken(userInfo.getId(),userInfo.getName());
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setToken(token);
        userInfoVO.setName(userInfo.getName());
        userInfoVO.setNickName(userInfo.getNickName());
        userInfoVO.setHeadImg(userInfo.getHeadImg());
        userInfoVO.setMobile(userInfo.getMobile());
        userInfoVO.setUserType(userType);

        return userInfoVO;
    }

    @Override
    public IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery) {

        String mobile = userInfoQuery.getMobile();
        Integer status = userInfoQuery.getStatus();
        Integer userType = userInfoQuery.getUserType();

        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();

        if(userInfoQuery == null){
            return baseMapper.selectPage(pageParam,null);
        }

        userInfoQueryWrapper
                .eq(StringUtils.isNotBlank(mobile),"mobile",mobile)
                .eq(status != null,"status",userInfoQuery.getStatus())
                .eq(userType != null,"user_type",userInfoQueryWrapper);

        return baseMapper.selectPage(pageParam, userInfoQueryWrapper);
    }

    @Override
    public void lock(Long id, Integer status) {

        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setStatus(status);
        baseMapper.updateById(userInfo);
    }

    @Override
    public boolean checkMobile(String mobile) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count>0;
    }

    @Override
    public UserIndexVO getIndexUserInfo(Long userId) {

        //用户信息
        UserInfo userInfo = baseMapper.selectById(userId);

        //账户信息
        QueryWrapper<UserAccount> userAccountQueryWrapper = new QueryWrapper<>();
        userAccountQueryWrapper.eq("user_id", userId);
        UserAccount userAccount = userAccountMapper.selectOne(userAccountQueryWrapper);

        //登录信息
        QueryWrapper<UserLoginRecord> userLoginRecordQueryWrapper = new QueryWrapper<>();
        userLoginRecordQueryWrapper
                .eq("user_id", userId)
                .orderByDesc("id")
                .last("limit 1");
        UserLoginRecord userLoginRecord = userLoginRecordMapper.selectOne(userLoginRecordQueryWrapper);

        //组装结果数据
        UserIndexVO userIndexVO = new UserIndexVO();
        userIndexVO.setUserId(userInfo.getId());
        userIndexVO.setUserType(userInfo.getUserType());
        userIndexVO.setName(userInfo.getName());
        userIndexVO.setNickName(userInfo.getNickName());
        userIndexVO.setHeadImg(userInfo.getHeadImg());
        userIndexVO.setBindStatus(userInfo.getBindStatus());
        userIndexVO.setAmount(userAccount.getAmount());
        userIndexVO.setFreezeAmount(userAccount.getFreezeAmount());
        userIndexVO.setLastLoginTime(userLoginRecord.getCreateTime());

        return userIndexVO;
    }

    /*
    * 消息队列发送短信(通过绑定码找电话号码)
    * */
    @Override
    public String getMobileByBindCode(String bindCode) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("bind_code", bindCode);
        UserInfo userInfo = baseMapper.selectOne(userInfoQueryWrapper);
        return userInfo.getMobile();
    }
}
