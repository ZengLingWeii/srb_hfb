package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */

public interface DictService extends IService<Dict> {
    //定义业务层I/O流导入数据的方法
    void importData(InputStream inputStream);

    //导出Excel表的方法
    List<ExcelDictDTO> listDictData();

    //字典数据：根据parentId拿Dict集合
    List<Dict> listByParentId(Long parentId);

    //获取借款信息的下拉框
    List<Dict> findByDictCode(String dictCode);

    //审核信息
    String getNameByParentDictCodeAndValue(String dictCode, Integer value);
}
