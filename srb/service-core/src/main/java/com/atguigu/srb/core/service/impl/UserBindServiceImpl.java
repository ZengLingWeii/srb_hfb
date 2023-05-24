package com.atguigu.srb.core.service.impl;

import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.core.enums.UserBindEnum;
import com.atguigu.srb.core.hfb.FormHelper;
import com.atguigu.srb.core.hfb.HfbConst;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.entity.UserBind;
import com.atguigu.srb.core.mapper.UserBindMapper;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.vo.UserBindVO;
import com.atguigu.srb.core.service.UserBindService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务实现类
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */
@Service
public class UserBindServiceImpl extends ServiceImpl<UserBindMapper, UserBind> implements UserBindService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public String commitBindUser(UserBindVO userBindVO, Long userId) {

        //查询身份证号(id_card)是否已经绑定
        QueryWrapper<UserBind> userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper
                .eq("id_card",userBindVO.getIdCard())
                .ne("user_id",userId);
        UserBind userBind = baseMapper.selectOne(userBindQueryWrapper);
        //USER_BIND_IDCARD_EXIST_ERROR(-301,"身份证号码已绑定")
        Assert.isNull(userBind, ResponseEnum.USER_BIND_IDCARD_EXIST_ERROR);

        //查询用户绑定信息
        userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper.eq("user_id",userId);
        userBind = baseMapper.selectOne(userBindQueryWrapper);

        //判断是否有绑定记录
        if(userBind == null){
            //如果未创建绑定记录，则创建一条记录
            userBind = new UserBind();
            BeanUtils.copyProperties(userBindVO,userBind);
            userBind.setUserId(userId);
            userBind.setStatus(UserBindEnum.NO_BIND.getStatus());
            baseMapper.insert(userBind);
        }else{
            //曾经跳转到托管平台，但是未完成操作，此时将用户最新填写的数据同步到userBind对象
            BeanUtils.copyProperties(userBindVO,userBind);
            baseMapper.updateById(userBind);
        }

        //将汇付宝对应的字段封装起来
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);//尚融宝唯一标识id:999888
        paramMap.put("agentUserId", userId);//用户id
        paramMap.put("idCard",userBindVO.getIdCard());//用户身份证
        paramMap.put("personalName", userBindVO.getName());//用户名称
        paramMap.put("bankType", userBindVO.getBankType());//用户银行卡类型
        paramMap.put("bankNo", userBindVO.getBankNo());//用户银行卡号
        paramMap.put("mobile", userBindVO.getMobile());//用户电话
        paramMap.put("returnUrl", HfbConst.USERBIND_RETURN_URL);//汇付宝成功绑定后跳转的尚融宝用户地址
        paramMap.put("notifyUrl", HfbConst.USERBIND_NOTIFY_URL);//汇付宝成功绑定的异步回调
        paramMap.put("timestamp", RequestHelper.getTimestamp());//绑定时间
        paramMap.put("sign", RequestHelper.getSign(paramMap));//签名加密

        //构建充值自动提交表单
        //HfbConst.USERBIND_URL = "http://localhost:9999/userBind/BindAgreeUserV2"
        String formStr = FormHelper.buildForm(HfbConst.USERBIND_URL, paramMap);
        return formStr;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notify(Map<String, Object> paramMap) {

        String bindCode = (String)paramMap.get("bindCode");
        //会员id
        String agentUserId = (String)paramMap.get("agentUserId");

        //根据user_id查询user_bind记录
        QueryWrapper<UserBind> userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper.eq("user_id",agentUserId);

        //更新用户绑定表
        UserBind userBind = baseMapper.selectOne(userBindQueryWrapper);
        userBind.setBindCode(bindCode);
        userBind.setStatus(UserBindEnum.BIND_OK.getStatus());
        baseMapper.updateById(userBind);

        //更新用户表
        UserInfo userInfo = userInfoMapper.selectById(agentUserId);
        userInfo.setBindCode(bindCode);
        userInfo.setName(userBind.getName());
        userInfo.setIdCard(userBind.getIdCard());
        userInfo.setBindStatus(UserBindEnum.BIND_OK.getStatus());
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public String getBindCodeByUserId(Long userId) {
        QueryWrapper<UserBind> userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper.eq("user_id", userId);
        UserBind userBind = baseMapper.selectOne(userBindQueryWrapper);
        String bindCode = userBind.getBindCode();
        return bindCode;
    }
}
