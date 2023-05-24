package com.atguigu.srb.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.exception.BusinessException;
import com.atguigu.srb.core.enums.LendStatusEnum;
import com.atguigu.srb.core.enums.ReturnMethodEnum;
import com.atguigu.srb.core.enums.TransTypeEnum;
import com.atguigu.srb.core.hfb.HfbConst;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.mapper.BorrowerMapper;
import com.atguigu.srb.core.mapper.UserAccountMapper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.bo.TransFlowBO;
import com.atguigu.srb.core.pojo.entity.*;
import com.atguigu.srb.core.mapper.LendMapper;
import com.atguigu.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.atguigu.srb.core.pojo.vo.BorrowerDetailVO;
import com.atguigu.srb.core.service.*;
import com.atguigu.srb.core.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 标的准备表 服务实现类
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */
@Service
@Slf4j
public class LendServiceImpl extends ServiceImpl<LendMapper, Lend> implements LendService {

    @Resource
    private DictService dictService;

    @Resource
    private BorrowerMapper borrowerMapper;

    @Resource
    private BorrowerService borrowerService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserAccountMapper userAccountMapper;

    @Resource
    private TransFlowService transFlowService;

    @Resource
    private LendItemService lendItemService;

    @Resource
    private LendReturnService lendReturnService;

    @Resource
    private LendItemReturnService lendItemReturnService;

    /*
    * 产生标的并在数据库中插入数据
    * */
    @Override
    public void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowInfo borrowInfo) {

        //生成标的
        Lend lend = new Lend();

        //设置标的信息
        lend.setUserId(borrowInfo.getUserId());
        lend.setBorrowInfoId(borrowInfo.getId());
        lend.setLendNo(LendNoUtils.getLendNo());//生成编号
        lend.setTitle(borrowInfoApprovalVO.getTitle());
        lend.setAmount(borrowInfo.getAmount());
        lend.setPeriod(borrowInfo.getPeriod());
        lend.setLendYearRate(borrowInfoApprovalVO.getLendYearRate().divide(new BigDecimal(100)));//从审批对象中获取
        lend.setServiceRate(borrowInfoApprovalVO.getServiceRate().divide(new BigDecimal(100)));//从审批对象中获取
        lend.setReturnMethod(borrowInfo.getReturnMethod());
        lend.setLowestAmount(new BigDecimal(100));
        lend.setInvestAmount(new BigDecimal(0));
        lend.setInvestNum(0);
        lend.setPublishDate(LocalDateTime.now());

        //起息日期
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate lendStartDate = LocalDate.parse(borrowInfoApprovalVO.getLendStartDate(), dtf);
        lend.setLendStartDate(lendStartDate);
        //结束日期
        LocalDate lendEndDate = lendStartDate.plusMonths(borrowInfo.getPeriod());
        lend.setLendEndDate(lendEndDate);

        //描述
        lend.setLendInfo(borrowInfoApprovalVO.getLendInfo());

        //平台预期收益(月年化 = 年化/12)
        BigDecimal monthRate = lend.getServiceRate().divide(new BigDecimal(12),8,BigDecimal.ROUND_DOWN);

        //平台收益 = 标的金额 * 月年化 * 期数
        BigDecimal expectAmount = lend.getAmount().multiply(monthRate).multiply(new BigDecimal(lend.getPeriod()));
        lend.setExpectAmount(expectAmount);

        //实际收益
        lend.setRealAmount(new BigDecimal(0));
        //状态
        lend.setStatus(LendStatusEnum.INVEST_RUN.getStatus());
        //审核时间
        lend.setCheckTime(LocalDateTime.now());
        //审核人
        lend.setCheckAdminId(1L);

        baseMapper.insert(lend);
    }

    /*
    * 展示标的列表
    * */
    @Override
    public List<Lend> selectList() {
        List<Lend> lendList = baseMapper.selectList(null);
        lendList.forEach(lend -> {
            //还款方式
            String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod",lend.getReturnMethod());
            //状态
            String status = LendStatusEnum.getMsgByStatus(lend.getStatus());
            //添加到其他参数中
            lend.getParam().put("returnMethod", returnMethod);
            lend.getParam().put("status", status);
        });
        return lendList;
    }

    /*
    * 获取标的信息
    * */
    @Override
    public Map<String, Object> getLendDetail(Long id) {

        //查询对象
        Lend lend = baseMapper.selectById(id);
        //组装数据
        String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod",lend.getReturnMethod());
        String status = LendStatusEnum.getMsgByStatus(lend.getStatus());
        lend.getParam().put("returnMethod",returnMethod);
        lend.getParam().put("status",status);

        //根据user_id获取借款人对象
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper.eq("user_id", lend.getUserId());
        Borrower borrower = borrowerMapper.selectOne(borrowerQueryWrapper);
        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVOById(borrower.getId());

        //组装对象
        Map<String,Object> result = new HashMap<>();
        result.put("lend",lend);
        result.put("borrower",borrowerDetailVO);

        return result;
    }

    /*
    * 预期收益
    * */
    @Override
    public BigDecimal getInterestCount(BigDecimal invest, BigDecimal yearRate, Integer totalMonth, Integer returnMethod) {

        BigDecimal interestCount;

        //计算利息
        if(returnMethod.intValue() == ReturnMethodEnum.ONE.getMethod()){
            interestCount = Amount1Helper.getInterestCount(invest,yearRate,totalMonth);
        }else if(returnMethod.intValue() == ReturnMethodEnum.TWO.getMethod()){
            interestCount = Amount2Helper.getInterestCount(invest,yearRate,totalMonth);
        }else if(returnMethod.intValue() == ReturnMethodEnum.THREE.getMethod()){
            interestCount = Amount3Helper.getInterestCount(invest,yearRate,totalMonth);
        }else{
            interestCount = Amount4Helper.getInterestCount(invest,yearRate,totalMonth);
        }

        return interestCount;
    }

    /*
    * 满标放款
    * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void makeLoan(Long lendId) {

        //获取标的信息
        Lend lend = baseMapper.selectById(lendId);

        //放款接口调用
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentProjectCode",lend.getLendNo());//标的编号
        String agentBillNo = LendNoUtils.getLoanNo();//放标编号
        paramMap.put("agentBillNo",agentBillNo);

        //平台收益，放款扣除，借款人借款实际金额=借款金额-平台收益
        //月年化
        BigDecimal monthRate = lend.getServiceRate().divide(new BigDecimal(12), 8, BigDecimal.ROUND_DOWN);
        //平台实际收益 = 已投金额 * 月年化 * 标的期数
        BigDecimal realAmount = lend.getInvestAmount().multiply(monthRate).multiply(new BigDecimal(lend.getPeriod()));

        //发送同步远程调用
        paramMap.put("mchFee",realAmount);//商户手续费(平台实际收益)
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign",sign);
        JSONObject result = RequestHelper.sendRequest(paramMap,HfbConst.MAKE_LOAD_URL);
        log.info("放款结果：" + result.toJSONString());

        if (!"0000".equals(result.getString("resultCode"))) {
            throw new BusinessException(result.getString("resultMsg"));
        }

        //更新标的信息
        lend.setRealAmount(realAmount);
        lend.setStatus(LendStatusEnum.PAY_RUN.getStatus());
        lend.setPaymentTime(LocalDateTime.now());
        baseMapper.updateById(lend);

        //获取借款人信息
        Long userId = lend.getUserId();
        UserInfo userInfo = userInfoMapper.selectById(userId);
        String bindCode = userInfo.getBindCode();

        //给借款账号转入金额
        BigDecimal total = new BigDecimal(result.getString("voteAmt"));
        userAccountMapper.updateAccount(bindCode,total,new BigDecimal(0));

        //新增借款人交易流水
        TransFlowBO transFlowBO = new TransFlowBO(
                agentBillNo,
                bindCode,
                total,
                TransTypeEnum.BORROW_BACK,
                "借款放款到账，编号："+lend.getLendNo()
        );
        transFlowService.saveTransFlow(transFlowBO);

        //获取投资列表信息
        List<LendItem> lendItemList = lendItemService.selectByLendId(lendId,1);
        lendItemList.stream().forEach(item ->{

            //获取投资人信息
            Long investUserId = item.getInvestUserId();
            UserInfo investUserInfo = userInfoMapper.selectById(investUserId);
            String investBindCode = investUserInfo.getBindCode();

            //投资人账号冻结金额转出
            BigDecimal investAmount = item.getInvestAmount();//投资金额
            userAccountMapper.updateAccount(investBindCode,new BigDecimal(0),investAmount.negate());

            //新增投资人交易流水
            TransFlowBO investTransFlowBO = new TransFlowBO(
                    LendNoUtils.getTransNo(),
                    investBindCode,
                    investAmount,
                    TransTypeEnum.INVEST_UNLOCK,
                    "冻结资金转出，出借放款，编号："+lend.getLendNo()
            );
            transFlowService.saveTransFlow(investTransFlowBO);
        });

        //放款成功生成借款人还款计划和投资人回款计划
        this.repaymentPlan(lend);

    }

    /*
    * 还款计划
    * */
    private void repaymentPlan(Lend lend) {

        //还款计划列表(里面是借款人的borrow_info_id)
        List<LendReturn> lendReturnList = new ArrayList<>();

        //按还款时间生成还款计划(还款分len期)
        int len = lend.getPeriod().intValue();
        for (int i = 1; i <= len; i++) {

            //创建还款计划对象
            LendReturn lendReturn = new LendReturn();
            lendReturn.setReturnNo(LendNoUtils.getReturnNo());
            lendReturn.setLendId(lend.getId());
            lendReturn.setBorrowInfoId(lend.getBorrowInfoId());
            lendReturn.setUserId(lend.getUserId());
            lendReturn.setAmount(lend.getAmount());
            lendReturn.setBaseAmount(lend.getInvestAmount());
            lendReturn.setLendYearRate(lend.getLendYearRate());
            lendReturn.setCurrentPeriod(i);//当前期数
            lendReturn.setReturnMethod(lend.getReturnMethod());

            //说明：还款计划中的这三项 = 回款计划中对应的这三项和：因此需要先生成对应的回款计划
            //本金：lendReturn.setPrincipal();
            //利息：lendReturn.setInterest();
            //本息：lendReturn.setTotal();

            lendReturn.setFee(new BigDecimal(0));
            lendReturn.setReturnDate(lend.getLendStartDate().plusMonths(i)); //第二个月开始还款
            lendReturn.setOverdue(false);
            //最后一个月
            if (i == len) {
                //标识为最后一次还款
                lendReturn.setLast(true);
            } else {
                lendReturn.setLast(false);
            }
            lendReturn.setStatus(0);
            lendReturnList.add(lendReturn);//一个lendReturn对应一期还款计划(一期总额)
        }
        //批量保存
        lendReturnService.saveBatch(lendReturnList);

        //获取lendReturnList中还款期数与还款计划id对应map(标的当前期数 ---> 标当前期的还款计划id)
        Map<Integer, Long> lendReturnMap = lendReturnList.stream().collect(
                Collectors.toMap(LendReturn::getCurrentPeriod, LendReturn::getId)
        );

        //======================================================
        //=============获取所有投资者，生成回款计划===================
        //======================================================
        //回款计划列表(里面是投资人)
        List<LendItemReturn> lendItemReturnAllList = new ArrayList<>();
        //获取投资成功的投资记录(里面是借款人)
        List<LendItem> lendItemList = lendItemService.selectByLendId(lend.getId(), 1);
        for (LendItem lendItem : lendItemList) {

            //根据投资人来创建回款计划列表
            List<LendItemReturn> lendItemReturnList = this.returnInvest(lendItem.getId(), lendReturnMap, lend);
            lendItemReturnAllList.addAll(lendItemReturnList);
        }

        //更新还款计划中的相关金额数据
        for (LendReturn lendReturn : lendReturnList) {

            //本金
            BigDecimal sumPrincipal = lendItemReturnAllList.stream()
                    //过滤条件：当回款计划中的还款计划id == 当前还款计划id的时候
                    .filter(item -> item.getLendReturnId().longValue() == lendReturn.getId().longValue())
                    //将所有回款计划中计算的每月应收本金相加
                    .map(LendItemReturn::getPrincipal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            //利息
            BigDecimal sumInterest = lendItemReturnAllList.stream()
                    .filter(item -> item.getLendReturnId().longValue() == lendReturn.getId().longValue())
                    .map(LendItemReturn::getInterest)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            //本息
            BigDecimal sumTotal = lendItemReturnAllList.stream()
                    .filter(item -> item.getLendReturnId().longValue() == lendReturn.getId().longValue())
                    .map(LendItemReturn::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            lendReturn.setPrincipal(sumPrincipal); //每期还款本金
            lendReturn.setInterest(sumInterest); //每期还款利息
            lendReturn.setTotal(sumTotal); //每期还款本息
        }
        lendReturnService.updateBatchById(lendReturnList);
    }

    /*
    * 回款计划
    * */
    public List<LendItemReturn> returnInvest(Long lendItemId, Map<Integer, Long> lendReturnMap, Lend lend) {

        //投标信息
        LendItem lendItem = lendItemService.getById(lendItemId);

        //投资金额
        BigDecimal amount = lendItem.getInvestAmount();
        //年化利率
        BigDecimal yearRate = lendItem.getLendYearRate();
        //投资期数
        int totalMonth = lend.getPeriod();

        Map<Integer, BigDecimal> mapInterest = null;  //还款期数 -> 利息
        Map<Integer, BigDecimal> mapPrincipal = null; //还款期数 -> 本金

        //根据还款方式计算本金和利息
        if (lend.getReturnMethod().intValue() == ReturnMethodEnum.ONE.getMethod()) {
            //利息
            mapInterest = Amount1Helper.getPerMonthInterest(amount, yearRate, totalMonth);
            //本金
            mapPrincipal = Amount1Helper.getPerMonthPrincipal(amount, yearRate, totalMonth);
        } else if (lend.getReturnMethod().intValue() == ReturnMethodEnum.TWO.getMethod()) {
            mapInterest = Amount2Helper.getPerMonthInterest(amount, yearRate, totalMonth);
            mapPrincipal = Amount2Helper.getPerMonthPrincipal(amount, yearRate, totalMonth);
        } else if (lend.getReturnMethod().intValue() == ReturnMethodEnum.THREE.getMethod()) {
            mapInterest = Amount3Helper.getPerMonthInterest(amount, yearRate, totalMonth);
            mapPrincipal = Amount3Helper.getPerMonthPrincipal(amount, yearRate, totalMonth);
        } else {
            mapInterest = Amount4Helper.getPerMonthInterest(amount, yearRate, totalMonth);
            mapPrincipal = Amount4Helper.getPerMonthPrincipal(amount, yearRate, totalMonth);
        }

        //创建回款计划列表
        List<LendItemReturn> lendItemReturnList = new ArrayList<>();
        for (Map.Entry<Integer, BigDecimal> entry : mapInterest.entrySet()) {
            Integer currentPeriod = entry.getKey();
            //根据还款期数获取还款计划的id
            Long lendReturnId = lendReturnMap.get(currentPeriod);

            LendItemReturn lendItemReturn = new LendItemReturn();
            lendItemReturn.setLendReturnId(lendReturnId);
            lendItemReturn.setLendItemId(lendItemId);
            lendItemReturn.setInvestUserId(lendItem.getInvestUserId());
            lendItemReturn.setLendId(lendItem.getLendId());
            lendItemReturn.setInvestAmount(lendItem.getInvestAmount());
            lendItemReturn.setLendYearRate(lend.getLendYearRate());
            lendItemReturn.setCurrentPeriod(currentPeriod);
            lendItemReturn.setReturnMethod(lend.getReturnMethod());
            //最后一次本金计算
            if (lendItemReturnList.size() > 0 && currentPeriod.intValue() == lend.getPeriod().intValue()) {
                //最后一期本金 = 本金 - 前几次之和
                BigDecimal sumPrincipal = lendItemReturnList.stream()
                        .map(LendItemReturn::getPrincipal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                //最后一期应还本金 = 用当前投资人的总投资金额 - 除了最后一期前面期数计算出来的所有的应还本金
                BigDecimal lastPrincipal = lendItem.getInvestAmount().subtract(sumPrincipal);
                lendItemReturn.setPrincipal(lastPrincipal);
                //最后一期的利息
                BigDecimal sumInterest  =lendItemReturnList.stream()
                        .map(LendItemReturn::getInterest)
                        .reduce(BigDecimal.ZERO,BigDecimal::add);
                BigDecimal lastInterest = lendItem.getExpectAmount().subtract(sumInterest);
                lendItemReturn.setInterest(lastInterest);
            } else {
                lendItemReturn.setPrincipal(mapPrincipal.get(currentPeriod));
                lendItemReturn.setInterest(mapInterest.get(currentPeriod));
            }

            lendItemReturn.setTotal(lendItemReturn.getPrincipal().add(lendItemReturn.getInterest()));
            lendItemReturn.setFee(new BigDecimal("0"));
            lendItemReturn.setReturnDate(lend.getLendStartDate().plusMonths(currentPeriod));
            //是否逾期，默认未逾期
            lendItemReturn.setOverdue(false);
            lendItemReturn.setStatus(0);

            lendItemReturnList.add(lendItemReturn);
        }
        lendItemReturnService.saveBatch(lendItemReturnList);

        return lendItemReturnList;
    }
}
