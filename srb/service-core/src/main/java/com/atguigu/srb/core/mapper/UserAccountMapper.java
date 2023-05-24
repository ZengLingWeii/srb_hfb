package com.atguigu.srb.core.mapper;

import com.atguigu.srb.core.pojo.entity.UserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

/**
 * <p>
 * 用户账户 Mapper 接口
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    void updateAccount(@Param("bindCode") String bindCode,//绑定号
                       @Param("amount") BigDecimal bigDecimal,//充值金额
                       @Param("freezeAmount") BigDecimal bigDecimal1//冻结资金
    );
}
