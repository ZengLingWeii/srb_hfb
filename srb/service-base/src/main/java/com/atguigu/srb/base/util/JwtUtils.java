package com.atguigu.srb.base.util;

import com.atguigu.common.exception.BusinessException;
import com.atguigu.common.result.ResponseEnum;
import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JwtUtils {

    private static long tokenExpiration = 24*60*60*1000;//24h
    private static String tokenSignKey = "Zlw123456";

    private static Key getKeyInstance(){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;//HS256算法
        byte[] bytes = DatatypeConverter.parseBase64Binary(tokenSignKey);//就是将Base64编码后的String还原成字节数组
        return new SecretKeySpec(bytes,signatureAlgorithm.getJcaName());//返回密钥
    }

    public static String createToken(Long userId, String userName) {
        String token = Jwts.builder()
                .setSubject("ZLW-USER")//设置主题
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))//设置过期时间
                .claim("userId", userId)//载荷：user_id
                .claim("userName", userName)//载荷：user_name
                .signWith(SignatureAlgorithm.HS512, getKeyInstance())//获取签名秘钥，并采用HS512加密
                .compressWith(CompressionCodecs.GZIP)//当载荷过长时可对其进行压缩
                .compact();
        return token;
    }

    /**
     * 判断token是否有效
     * @param token
     * @return
     */
    public static boolean checkToken(String token) {
        if(StringUtils.isEmpty(token)) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static Long getUserId(String token) {
        Claims claims = getClaims(token);
        Integer userId = (Integer)claims.get("userId");
        return userId.longValue();
    }

    public static String getUserName(String token) {
        Claims claims = getClaims(token);
        return (String)claims.get("userName");
    }

    public static void removeToken(String token) {
        //jwttoken无需删除，客户端扔掉即可。
    }

    /**
     * 校验token并返回Claims
     * @param token
     * @return
     */
    private static Claims getClaims(String token) {
        if(StringUtils.isEmpty(token)) {
            // LOGIN_AUTH_ERROR(-211, "未登录"),
            throw new BusinessException(ResponseEnum.LOGIN_AUTH_ERROR);
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return claims;
        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.LOGIN_AUTH_ERROR);
        }
    }
}

