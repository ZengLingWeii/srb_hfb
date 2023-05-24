package com.atguigu.srb.core.controller.admin;

import com.atguigu.common.result.R;
import com.atguigu.srb.core.pojo.entity.Lend;
import com.atguigu.srb.core.service.LendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api(tags = "标的管理")
@RestController
@RequestMapping("/admin/core/lend")
@Slf4j
public class AdminLendController {

    @Resource
    private LendService lendService;

    @ApiOperation("标的列表")
    @GetMapping("/list")
    public R list(){
        List<Lend> lendList = lendService.selectList();
        return R.ok().data("list",lendList);
    }

    @ApiOperation("获取标的信息")
    @GetMapping("/show/{id}")
    public R show(
            @ApiParam(value = "标号",required = true)
            @PathVariable Long id
    ){
        Map<String,Object> lendDetail = lendService.getLendDetail(id);
        return R.ok().data("lendDetail",lendDetail);
    }

    @ApiOperation("放款")
    @GetMapping("/makeLoan/{id}")
    public R makeLoan(@ApiParam(value = "标的id",required = true)
                      @PathVariable("id") Long id
    ){
        lendService.makeLoan(id);
        return R.ok().message("放款成功");
    }

}
