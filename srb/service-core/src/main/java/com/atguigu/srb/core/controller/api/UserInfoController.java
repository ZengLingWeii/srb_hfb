package com.atguigu.srb.core.controller.api;


import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.R;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.common.util.RegexValidateUtils;
import com.atguigu.srb.base.util.JwtUtils;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.vo.LoginVO;
import com.atguigu.srb.core.pojo.vo.RegisterVO;
import com.atguigu.srb.core.pojo.vo.UserIndexVO;
import com.atguigu.srb.core.pojo.vo.UserInfoVO;
import com.atguigu.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */
@Api(tags = "会员接口")
@Slf4j
@RestController
@RequestMapping("/api/core/userInfo")
public class UserInfoController {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private UserInfoService userInfoService;

    //注册方法
    @ApiOperation("会员注册")
    @PostMapping("/register")
    public R register(@RequestBody RegisterVO registerVO){

        String mobile = registerVO.getMobile();
        String code = registerVO.getCode();
        String password = registerVO.getPassword();

        //校验
        //MOBILE_NULL_ERROR(-202,"手机号不能为空")
        Assert.notEmpty(mobile,ResponseEnum.MOBILE_NULL_ERROR);
        //MOBILE_NULL_ERROR(-203,"手机号不正确")
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);
        //PASSWORD_NULL_ERROR(-204,"密码不能为空)
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);
        //CODE_NULL_ERROR(-205, "验证码不能为空"),
        Assert.notEmpty(code, ResponseEnum.CODE_NULL_ERROR);

        //校验验证码是否正确
        String codeGen = (String) redisTemplate.opsForValue().get("srb:sms:code:" + mobile);
        System.out.println("code = "+code+",codeGen = "+codeGen+",mobile = "+mobile);
        Assert.equals(code,codeGen, ResponseEnum.CODE_ERROR);

        //注册
        userInfoService.register(registerVO);
        return R.ok().message("注册会员成功");
    }

    //会员登入
    @ApiOperation("会员登入")
    @PostMapping("/login")
    public R login(@RequestBody LoginVO loginVO, HttpServletRequest request){

        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        Assert.notEmpty(mobile,ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);

        String ip = request.getRemoteAddr();
        UserInfoVO userInfoVO = userInfoService.login(loginVO,ip);

        return R.ok().data("userInfo",userInfoVO);
    }

    //token校验
    @ApiOperation("校验令牌")
    @GetMapping("/checkToken")
    public R checkToken(HttpServletRequest request) {

        String token = request.getHeader("token");
        boolean result = JwtUtils.checkToken(token);

        if(result){
            return R.ok();
        }else{
            //LOGIN_AUTH_ERROR(-211, "未登录"),
            return R.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
        }
    }

    //校验手机号是否已被注册(OpenFeign)
    @ApiOperation("校验手机号是否注册")
    @GetMapping("/checkMobile/{mobile}")
    public boolean checkMobile(
            @ApiParam(value = "手机号码",required = true)
            @PathVariable String mobile){
        return userInfoService.checkMobile(mobile);
    }

    @ApiOperation("获取个人空间用户信息")
    @GetMapping("/auth/getIndexUserInfo")
    public R getIndexUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        UserIndexVO userIndexVO = userInfoService.getIndexUserInfo(userId);
        return R.ok().data("userIndexVO", userIndexVO);
    }

}

