package com.atguigu.srb.core.controller.admin;


import com.atguigu.common.exception.Assert;
import com.atguigu.common.exception.BusinessException;
import com.atguigu.common.result.R;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.core.pojo.entity.IntegralGrade;
import com.atguigu.srb.core.service.IntegralGradeService;
import com.sun.media.jfxmedia.logging.Logger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 管理员 积分等级表 前端控制器
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */

@Api(tags = "积分管理")
@Slf4j
@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {

    @Resource
    private IntegralGradeService integralGradeService;

    @ApiOperation("积分列表查询")
    @GetMapping("/list")
    public R listAll(){

        //日志输出测试
        log.info("hi i am zlw");
        log.warn("warning!!!");
        log.error("ti is a error");

        List<IntegralGrade> list = integralGradeService.list();
        return R.ok().data("list",list);
    }

    @ApiOperation("根据id删除积分")
    @DeleteMapping("/remove/{id}")
    public R removeById(
                        @ApiParam(value = "逻辑删除的id",required = true,example = "100")
                        @PathVariable
                        Long id){
        boolean result = integralGradeService.removeById(id);
        if(result){
            return R.ok().message("删除成功");
        }else{
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("新增积分等级")
    @PostMapping("/save")
    public R save(@ApiParam(value = "积分等级对象",required = true)
                  @RequestBody
                  IntegralGrade integralGrade){
        boolean result = integralGradeService.save(integralGrade);

        //①测试自定义异常捕获 ---> 借款额度为空则直接抛出异常
        if(integralGrade.getBorrowAmount() == null){
            throw new BusinessException(ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
        }

        //②断言的方式抛出异常 ---> 借款额度为空则直接抛出异常
        Assert.notNull(integralGrade.getBorrowAmount(),ResponseEnum.BORROW_AMOUNT_NULL_ERROR);

        if(result){
            return R.ok().message("保存成功");
        }else{
            return R.error().message("保存失败");
        }
    }

    @ApiOperation("根据id获取积分等级")
    @GetMapping("/get/{id}")
    public R getById(
                     @ApiParam(value = "数据id",required = true, example = "1")
                     @PathVariable
                     Long id){
        IntegralGrade integralGrade = integralGradeService.getById(id);
        if(integralGrade != null){
            return R.ok().data("record",integralGrade);
        }else{
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("更新积分等级")
    @PutMapping("/update")
        public R updateById(@ApiParam(value = "积分等级对象",required = true)
                            @RequestBody
                            IntegralGrade integralGrade){
        boolean result = integralGradeService.updateById(integralGrade);
        if(result){
            return R.ok().message("修改成功");
        }else {
            return R.error().message("修改失败");
        }
    }

}

