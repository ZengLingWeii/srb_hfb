package com.atguigu.srb.core.service.impl;

import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.core.enums.BorrowInfoStatusEnum;
import com.atguigu.srb.core.enums.UserBindEnum;
import com.atguigu.srb.core.mapper.BorrowerMapper;
import com.atguigu.srb.core.mapper.IntegralGradeMapper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.entity.BorrowInfo;
import com.atguigu.srb.core.mapper.BorrowInfoMapper;
import com.atguigu.srb.core.pojo.entity.Borrower;
import com.atguigu.srb.core.pojo.entity.IntegralGrade;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.atguigu.srb.core.pojo.vo.BorrowerDetailVO;
import com.atguigu.srb.core.service.BorrowInfoService;
import com.atguigu.srb.core.service.BorrowerService;
import com.atguigu.srb.core.service.DictService;
import com.atguigu.srb.core.service.LendService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务实现类
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */
@Service
public class BorrowInfoServiceImpl extends ServiceImpl<BorrowInfoMapper, BorrowInfo> implements BorrowInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private IntegralGradeMapper integralGradeMapper;

    @Resource
    private DictService dictService;

    @Resource
    private BorrowerService borrowerService;

    @Resource
    private BorrowerMapper borrowerMapper;

    @Resource
    private LendService lendService;

    @Override
    public BigDecimal getBorrowAmount(Long userId) {

        //获取用户积分
        UserInfo userInfo = userInfoMapper.selectById(userId);
        Assert.notNull(userInfo, ResponseEnum.LOGIN_MOBILE_ERROR);
        Integer integral = userInfo.getIntegral();

        //根据积分查询借款额度
        QueryWrapper<IntegralGrade> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("integral_start",integral);//大于等于
        queryWrapper.ge("integral_end",integral);//小于等于
        IntegralGrade integralGrade = integralGradeMapper.selectOne(queryWrapper);
        if(integral == null){
            return new BigDecimal("0");
        }
        return integralGrade.getBorrowAmount();
    }

    @Override
    public void saveBorrowInfo(BorrowInfo borrowInfo, Long userId) {

        //获取userInfo的用户数据
        UserInfo userInfo = userInfoMapper.selectById(userId);

        //判断用户绑定状态
        Assert.isTrue(
                userInfo.getBindStatus().intValue() == UserBindEnum.BIND_OK.getStatus().intValue(),
                ResponseEnum.USER_NO_AMOUNT_ERROR
        );

        //借款额度是否足够
        BigDecimal borrowAmount = this.getBorrowAmount(userId);
        Assert.isTrue(
                borrowInfo.getAmount().doubleValue() <= borrowAmount.doubleValue(),
                ResponseEnum.USER_AMOUNT_LESS_ERROR
        );

        //存储数据
        borrowInfo.setUserId(userId);
        //百分比转成小数
        borrowInfo.setBorrowYearRate(borrowInfo.getBorrowYearRate().divide(new BigDecimal(100)));
        borrowInfo.setStatus(BorrowInfoStatusEnum.CHECK_RUN.getStatus());
        baseMapper.insert(borrowInfo);
    }

    @Override
    public Integer getStatusByUserId(Long userId) {
        QueryWrapper<BorrowInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("status").eq("user_id", userId);
        List<Object> objects = baseMapper.selectObjs(queryWrapper);

        //如果借款人尚未提交
        if(objects.size() == 0){
            return BorrowInfoStatusEnum.NO_AUTH.getStatus();
        }

        Integer status = (Integer)objects.get(0);
        return status;
    }

    /*
    * 展示借款人申请借款
    * */
    @Override
    public List<BorrowInfo> selectList() {

        //SQL语句：SELECT bi.*,b.name,b.mobile FROM borrow_info AS bi
        //        LEFT JOIN borrower AS b ON bi.user_id = b.user_id
        //        WHERE bi.is_deleted = 0
        //返回为borrowInfo的所有参数(除了param参数没有)
        List<BorrowInfo> borrowInfoList = baseMapper.selectBorrowInfoList();
        borrowInfoList.forEach(borrowInfo -> {
            //还款方式
            String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", borrowInfo.getReturnMethod());
            //借款使用
            String moneyUse = dictService.getNameByParentDictCodeAndValue("moneyUse", borrowInfo.getMoneyUse());
            //借款状态
            String status = BorrowInfoStatusEnum.getMsgByStatus(borrowInfo.getStatus());

            //上述的参数为BorrowInfo的其他参数
            borrowInfo.getParam().put("returnMethod", returnMethod);
            borrowInfo.getParam().put("moneyUse", moneyUse);
            borrowInfo.getParam().put("status", status);
        });
        return borrowInfoList;
    }

    /*
    * 查看借款人信息
    * */
    @Override
    public Map<String, Object> getBorrowInfoDetail(Long id) {

        //查询借款对象
        BorrowInfo borrowInfo = baseMapper.selectById(id);

        //组装数据
        String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", borrowInfo.getReturnMethod());
        String moneyUse = dictService.getNameByParentDictCodeAndValue("moneyUse",borrowInfo.getMoneyUse());
        String status = BorrowInfoStatusEnum.getMsgByStatus(borrowInfo.getStatus());
        borrowInfo.getParam().put("returnMethod",returnMethod);
        borrowInfo.getParam().put("moneyUse",moneyUse);
        borrowInfo.getParam().put("status",status);

        //根据user_id获取借款人对象
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper.eq("user_id",borrowInfo.getUserId());
        Borrower borrower = borrowerMapper.selectOne(borrowerQueryWrapper);

        //组装借款人对象
        BorrowerDetailVO borrowerDetailVO= borrowerService.getBorrowerDetailVOById(borrower.getId());

        //组装数据
        HashMap<String, Object> result = new HashMap<>();
        result.put("borrowInfo",borrowInfo);
        result.put("borrower",borrowerDetailVO);

        return result;
    }

    /*
    * 审批借款人
    * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approval(BorrowInfoApprovalVO borrowInfoApprovalVO) {

        //修改借款信息状态
        Long borrowInfoId = borrowInfoApprovalVO.getId();
        BorrowInfo borrowInfo = baseMapper.selectById(borrowInfoId);
        borrowInfo.setStatus(borrowInfoApprovalVO.getStatus());
        baseMapper.updateById(borrowInfo);

        //审核通过则创建标的
        if(borrowInfoApprovalVO.getStatus().intValue() == BorrowInfoStatusEnum.CHECK_OK.getStatus().intValue()){
            //创建标的
            lendService.createLend(borrowInfoApprovalVO, borrowInfo);
        }
    }

}
