package com.atguigu.srb.core.service.impl;

import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.core.enums.LendStatusEnum;
import com.atguigu.srb.core.enums.TransTypeEnum;
import com.atguigu.srb.core.hfb.FormHelper;
import com.atguigu.srb.core.hfb.HfbConst;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.mapper.LendMapper;
import com.atguigu.srb.core.mapper.UserAccountMapper;
import com.atguigu.srb.core.pojo.bo.TransFlowBO;
import com.atguigu.srb.core.pojo.entity.Lend;
import com.atguigu.srb.core.pojo.entity.LendItem;
import com.atguigu.srb.core.mapper.LendItemMapper;
import com.atguigu.srb.core.pojo.vo.InvestVO;
import com.atguigu.srb.core.service.*;
import com.atguigu.srb.core.util.LendNoUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 服务实现类
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */
@Service
@Slf4j
public class LendItemServiceImpl extends ServiceImpl<LendItemMapper, LendItem> implements LendItemService {

    @Resource
    LendMapper lendMapper;

    @Resource
    UserAccountService userAccountService;

    @Resource
    UserAccountMapper userAccountMapper;

    @Resource
    LendService lendService;

    @Resource
    UserBindService userBindService;

    @Resource
    TransFlowService transFlowService;

    /*
    * 获取投资列表信息
    * */
    @Override
    public List<LendItem> selectByLendId(Long lendId, Integer status) {
        QueryWrapper<LendItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lend_id",lendId);
        queryWrapper.eq("status",status);
        List<LendItem> lendItemList = baseMapper.selectList(queryWrapper);
        return lendItemList;
    }

    /*
    * 投标(1.投资前对用户进行校验(①标必须在募资中②买的金额不能超过标③账户的余额必须大于投标金额)、
    *     2.在投标平台生成投资信息(①投资记录②预期收益③实际收益)、
    *     3.组装参数提交到汇付宝(①投资人协议号②借款人协议号③组装参数Map<String, Object> paramMap))
    * */
    @Override
    public String commitInvest(InvestVO investVO) {

        //----------------------输入校验----------------------------
        //获取标的
        Long lendId = investVO.getLendId();
        Lend lend = lendMapper.selectById(lendId);
        //标的状态必须为募资中
        Assert.isTrue(lend.getStatus().intValue() == LendStatusEnum.INVEST_RUN.getStatus().intValue(),
        ResponseEnum.LEND_INVEST_ERROR);
        //标的不能超卖：(已投金额 + 本次投资金额 )>=标的金额（超卖）
        BigDecimal sum = lend.getInvestAmount().add(new BigDecimal(investVO.getInvestAmount()));
        Assert.isTrue(sum.doubleValue() <= lend.getAmount().doubleValue(),
        ResponseEnum.LEND_FULL_SCALE_ERROR);
        //账户可用余额充足：当前用户的余额 >= 当前用户的投资金额（可以投资）
        Long investUserId = investVO.getInvestUserId();
        BigDecimal amount = userAccountService.getAccount(investUserId);//获取当前用户的账户余额
        Assert.isTrue(amount.doubleValue() >= Double.parseDouble(investVO.getInvestAmount()),
        ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR);

        //---------------------在商户平台中生成投资信息-----------------
        LendItem lendItem = new LendItem();
        lendItem.setInvestUserId(investUserId);//投资人id
        lendItem.setInvestName(investVO.getInvestName());//投资人名字
        String lendItemNo = LendNoUtils.getLendItemNo();
        lendItem.setLendItemNo(lendItemNo); //投资条目编号（一个Lend对应一个或多个LendItem）
        lendItem.setLendId(investVO.getLendId());//对应的标的id
        lendItem.setInvestAmount(new BigDecimal(investVO.getInvestAmount())); //此笔投资金额
        lendItem.setLendYearRate(lend.getLendYearRate());//年化
        lendItem.setInvestTime(LocalDateTime.now()); //投资时间
        lendItem.setLendStartDate(lend.getLendStartDate()); //开始时间
        lendItem.setLendEndDate(lend.getLendEndDate()); //结束时间
        //预期收益
        BigDecimal expectAmount = lendService.getInterestCount(
                lendItem.getInvestAmount(),
                lendItem.getLendYearRate(),
                lend.getPeriod(),
                lend.getReturnMethod());
        lendItem.setExpectAmount(expectAmount);
        //实际收益
        lendItem.setRealAmount(new BigDecimal(0));
        lendItem.setStatus(0);//默认状态：刚刚创建
        baseMapper.insert(lendItem);

        //-------------组装投资相关的参数，提交到汇付宝资金托管平台----------
        //---------在托管平台同步用户的投资信息，修改用户的账户资金信息-------
        //获取投资人的绑定协议号
        String bindCode = userBindService.getBindCodeByUserId(investUserId);
        //获取借款人的绑定协议号
        String benefitBindCode = userBindService.getBindCodeByUserId(lend.getUserId());
        //封装提交至汇付宝的参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);//给商户分配的唯一标识:999888
        paramMap.put("voteBindCode", bindCode);//投资人协议号
        paramMap.put("benefitBindCode",benefitBindCode);//借款人协议号
        paramMap.put("agentProjectCode", lend.getLendNo());//项目标号
        paramMap.put("agentProjectName", lend.getTitle());//标的名称
        //在资金托管平台上的投资订单的唯一编号，要和lendItemNo保持一致。
        paramMap.put("agentBillNo", lendItemNo);//订单编号
        paramMap.put("voteAmt", investVO.getInvestAmount());//投资金额
        paramMap.put("votePrizeAmt", "0");
        paramMap.put("voteFeeAmt", "0");
        paramMap.put("projectAmt", lend.getAmount()); //标的总金额
        paramMap.put("note", "");
        paramMap.put("notifyUrl", HfbConst.INVEST_NOTIFY_URL); //异步回调
        paramMap.put("returnUrl", HfbConst.INVEST_RETURN_URL);//同步回调
        paramMap.put("timestamp", RequestHelper.getTimestamp());//时间
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);//签名

        //构建充值自动提交表单
        String formStr = FormHelper.buildForm(HfbConst.INVEST_URL, paramMap);
        return formStr;
    }

    @Override
    public void notify(Map<String, Object> paramMap) {

        log.info("投标成功");

        //获取投资编号
        String agentBillNo = (String)paramMap.get("agentBillNo");

        //判断交易流水号是否存在
        boolean result = transFlowService.isSaveTransFlow(agentBillNo);
        if(result){
            log.info("幂等性返回");
            return;
        }

        //获取用户的绑定协议号
        String bindCode = (String) paramMap.get("voteBindCode");
        String voteAmt = (String) paramMap.get("voteAmt");

        //修改商户系统中的用户金额、余额、冻结金额
        userAccountMapper.updateAccount(bindCode,new BigDecimal("-"+voteAmt),new BigDecimal(voteAmt));

        //修改投资记录的投资状态为已支付
        LendItem lendItem = this.getByLendItemNo(agentBillNo);
        lendItem.setStatus(1);//已支付
        baseMapper.updateById(lendItem);

        //修改标的信息：投资人数、已投金额
        Long lendId = lendItem.getLendId();
        Lend lend = lendMapper.selectById(lendId);
        lend.setInvestNum(lend.getInvestNum() + 1);
        lend.setInvestAmount(lend.getInvestAmount().add(lendItem.getInvestAmount()));
        lendMapper.updateById(lend);

        //新增交易流水
        TransFlowBO transFlowBO = new TransFlowBO(
                agentBillNo,
                bindCode,
                new BigDecimal(voteAmt),
                TransTypeEnum.INVEST_LOCK,
                "投标项目编号：" + lend.getLendNo() + ",项目名称：" + lend.getTitle()
        );
        transFlowService.saveTransFlow(transFlowBO);
    }

    /*
    * 投资人列表
    * */
    @Override
    public List<LendItem> selectByLendId(Long lendId) {

        QueryWrapper<LendItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lend_id",lendId);
        List<LendItem> lendItemList = baseMapper.selectList(queryWrapper);
        return lendItemList;
    }

    /*
    * 辅助方法: 为notify方法辅助、根据标LendItemNo返回LendItem
    * */
    private LendItem getByLendItemNo(String lendItemNo) {
        QueryWrapper<LendItem> queryWrapper = new QueryWrapper();
        queryWrapper.eq("lend_item_no", lendItemNo);
        return baseMapper.selectOne(queryWrapper);
    }
}
