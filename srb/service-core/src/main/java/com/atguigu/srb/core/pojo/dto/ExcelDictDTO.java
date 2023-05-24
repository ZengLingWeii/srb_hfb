package com.atguigu.srb.core.pojo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
//Excel导入数据的Dto类
public class ExcelDictDTO {
    //索引从0开始，也就是除去标题的第一行数据
    @ExcelProperty(value = "id",index = 0)
    private Long id;
    @ExcelProperty("上级id")
    private Long parentId;
    @ExcelProperty("名称")
    private String name;
    @ExcelProperty("值")
    private Integer value;
    @ExcelProperty("编码")
    private String dictCode;
}
