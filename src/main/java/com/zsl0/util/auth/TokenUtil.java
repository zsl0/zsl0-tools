package com.zsl0.util.auth;

import cn.hutool.core.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author zsl0
 * create on 2022/10/17 9:24
 */
public class TokenUtil {

    static Logger log = LoggerFactory.getLogger(TokenUtil.class);

    // todo 参数需要初始化
    // 访问Token过期分钟
    public static Integer ACCESS_TOKEN_MINUTE_EXPIRE = 3 * 24 * 60;
    // 刷新Token过期分钟
    public static Integer REFRESH_TOKEN_MINUTE_EXPIRE = 3 * 24 * 60;

    /**
     * 创建唯一Token, 凭借Payload中uuid作为当前用户的唯一凭证，与用户进行绑定
     */
    public static String createAccessToken(String uuid) {
        Date expire = DateUtil.offsetMinute(new Date(), ACCESS_TOKEN_MINUTE_EXPIRE);
        // 使用 uuid 作为 键值
        return JWTUtil.generateToken("access_token", expire, uuid);
    }

    /**
     * 创建唯一Token, 凭借Payload中uuid作为当前用户的唯一凭证，与用户进行绑定
     */
    public static String createAccessToken(String uuid, String authenticationJson) {
        Date expire = DateUtil.offsetMinute(new Date(), ACCESS_TOKEN_MINUTE_EXPIRE);
        // 使用 uuid 作为 键值
        return JWTUtil.generateToken("access_token", expire, uuid, authenticationJson);
    }


    public static String createRefreshToken(String uuid) {
        Date expire = DateUtil.offsetMinute(new Date(), REFRESH_TOKEN_MINUTE_EXPIRE);
        // 使用 uuid 作为 键值
        return JWTUtil.generateToken("refresh_token", expire, uuid);
    }

    /**
     * 获取access_token存储uuid
     */
    public static String getAccessTokenUuid(String token) {
        String subject = JWTUtil.getClaim(token, "sub");
        if (subject == null || !"access_token".equals(subject)) {
            throw new IllegalArgumentException("token认证失败，不是access_token!");
        }
        return JWTUtil.getClaim(token, "uuid");
    }

    /**
     * 获取access_token存储Authentication信息
     */
    public static String getAccessTokenAuthentication(String token) {
        String subject = JWTUtil.getClaim(token, "sub");
        if (subject == null || !"access_token".equals(subject)) {
            throw new IllegalArgumentException("token认证失败，不是access_token!");
        }

        return JWTUtil.getClaim(token, "Authentication");
    }

    /**
     * 获取refresh_token存储uuid
     */
    public static String getRefreshTokenUuid(String token) {
        String subject = JWTUtil.getClaim(token, "sub");
        if (subject == null || !"refresh_token".equals(subject)) {
            throw new IllegalArgumentException("token认证失败，refresh_token!");
        }
        return JWTUtil.getClaim(token, "uuid");
    }

    /**
     * 是否过期
     */
    public static boolean isExpire(String token) {
        Long expire = getExpire(token);
        return expire == null || expire < System.currentTimeMillis();
    }

    /**
     * 获取过期时间
     */
    public static Long getExpire(String token) {
        return JWTUtil.getExpire(token);
    }

}
