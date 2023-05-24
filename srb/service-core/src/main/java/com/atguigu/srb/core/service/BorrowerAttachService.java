package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.BorrowerAttach;
import com.atguigu.srb.core.pojo.vo.BorrowerAttachVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 借款人上传资源表 服务类
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */
public interface BorrowerAttachService extends IService<BorrowerAttach> {

    //借款人的房、车、身份证
    List<BorrowerAttachVO> selectBorrowerAttachVOList(Long borrowerId);
}
