package com.atguigu.srb.core.mapper;

import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */
public interface DictMapper extends BaseMapper<Dict> {
    //mapper层定义导入函数
    void insertBatch(List<ExcelDictDTO> list);
}
