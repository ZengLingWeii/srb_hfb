package com.atguigu.srb.core.controller.admin;

import com.alibaba.excel.EasyExcel;
import com.atguigu.common.exception.BusinessException;
import com.atguigu.common.result.R;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.atguigu.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@Api(tags = "数据字典管理")
@RestController
@Slf4j
@RequestMapping("/admin/core/dict")
public class AdminDictController {
    @Resource
    private DictService dictService;

    @ApiOperation("Excel批量导入数据字典")
    @PostMapping("/import")
    public R batchImport(
            @ApiParam(value = "Excel文件",required = true)
            //前端数据文件 file ---> MultipartFile
            @RequestParam("file") MultipartFile file
            ){
        try {
            InputStream inputStream = file.getInputStream();
            dictService.importData(inputStream);
            return R.ok().message("批量导入成功");
        } catch (IOException e) {
            //文件上传错误: UPLOAD_ERROR(-103,"文件上传错误")
            throw  new BusinessException(ResponseEnum.UPLOAD_ERROR,e);
        }
    }

    @ApiOperation("Excel导出数据字典")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        // 这里注意 使用swagger会导致各种问题，请直接用浏览器或者用postman
        try {
            //响应的内容文件类型设置
            response.setContentType("application/vnd.ms-excel");
            //响应内容的编码格式
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            //mydict为生成的Excel文件名，格式是UTF-8
            String fileName = URLEncoder.encode("mydict", "UTF-8").replaceAll("\\+", "%20");
            //设置响应头：Content-disposition(响应头文件位置)、attachment;filename*=utf-8(以utf-8格式附加文件)
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            //EasyExcel从数据库拿Dict数据写入Excel(ExcelDictDTO数据类型)文件之中
            EasyExcel.write(response.getOutputStream(), ExcelDictDTO.class).sheet("数据字典")
                     .doWrite(dictService.listDictData());
        } catch (IOException e) {
            //EXPORT_DATA_ERROR(104, "数据导出失败"),
            throw  new BusinessException(ResponseEnum.EXPORT_DATA_ERROR, e);
        }
    }

    /*
    * 字典数据列表展示
    * */
    @ApiOperation("根据上级id获取子节点数据列表")
    @GetMapping("/listByParentId/{parentId}")
    public R listByParentId(@ApiParam(value = "上级节点id",required = true)
                            @PathVariable Long parentId){
        List<Dict> dictList = dictService.listByParentId(parentId);
        return R.ok().data("list",dictList);
    }
}
