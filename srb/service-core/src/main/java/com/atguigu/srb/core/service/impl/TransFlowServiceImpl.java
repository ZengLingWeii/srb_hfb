package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.bo.TransFlowBO;
import com.atguigu.srb.core.pojo.entity.TransFlow;
import com.atguigu.srb.core.mapper.TransFlowMapper;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.service.TransFlowService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 交易流水表 服务实现类
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */
@Service
public class TransFlowServiceImpl extends ServiceImpl<TransFlowMapper, TransFlow> implements TransFlowService {

    @Resource
    private UserInfoMapper userInfoMapper;

    /*
    * 充值流水、将充值记录插入到流水数据库中
    * */
    @Override
    public void saveTransFlow(TransFlowBO transFlowBO) {

        //获取用户基本信息user_info
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("bind_code",transFlowBO.getBindCode());
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);

        //存储交易流水数据
        TransFlow transFlow = new TransFlow();
        transFlow.setUserId(userInfo.getId());
        transFlow.setUserName(userInfo.getName());
        transFlow.setTransNo(transFlowBO.getAgentBillNo());
        transFlow.setTransType(transFlowBO.getTransTypeEnum().getTransType());
        transFlow.setTransTypeName(transFlowBO.getTransTypeEnum().getTransTypeName());
        transFlow.setTransAmount(transFlowBO.getAmount());
        transFlow.setMemo(transFlowBO.getMemo());
        baseMapper.insert(transFlow);
    }

    /*
    * 判断流水如果存在，则从业务方法中直接退出
    * */
    @Override
    public boolean isSaveTransFlow(String agentBillNo) {

        QueryWrapper<TransFlow> transFlowQueryWrapper = new QueryWrapper<>();
        transFlowQueryWrapper.eq("trans_no",agentBillNo);
        Integer count = baseMapper.selectCount(transFlowQueryWrapper);

        if(count>0){
            return true;
        }
        return false;
    }

    @Override
    public List<TransFlow> selectByUserId(Long userId) {
        QueryWrapper<TransFlow> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_id", userId)
                .orderByDesc("id");
        return baseMapper.selectList(queryWrapper);
    }
}
