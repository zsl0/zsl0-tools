package com.zsl0.util.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author zsl0
 * create on 2022/10/17 9:17
 */
public class JWTUtil {

    static Logger log = LoggerFactory.getLogger(JWTUtil.class);

    // todo 参数需要初始化
    // 密钥
    public static String secret = "pacx:zsl:secret:123456789abc";
    // 发行人
    public static String issuer = "pacx";


    /**
     * 获取唯一凭证uuid
     */
    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成token
     */
    public static String generateToken(String subject, Date expire, String uuid) {
//        Assert.notNull(secret, "secret(密钥) 不能为空");
//        Assert.notNull(issuer, "issuer(发行人) 不能为空");

        String token = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            token = JWT.create()
                    .withIssuer(issuer)
                    .withClaim("uuid", uuid)
                    .withSubject(subject)
                    .withExpiresAt(expire)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return token;
    }


    /**
     * 生成token
     * @param subject 主题
     * @param expire 过期时间
     * @param authenticationJson 认证信息json字符串
     * @return
     */
    public static String generateToken(String subject, Date expire, String uuid, String authenticationJson) {
//        Assert.notNull(secret, "secret(密钥) 不能为空");
//        Assert.notNull(issuer, "issuer(发行人) 不能为空");

        String token = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            token = JWT.create()
                    .withIssuer(issuer)
                    .withClaim("uuid", uuid)
                    .withClaim("Authentication", authenticationJson)
                    .withSubject(subject)
                    .withExpiresAt(expire)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return token;
    }
    /**
     * 获取过期时间
     */
    public static Long getExpire(String token) {
        DecodedJWT decodedJWT = verity(token);
        return decodedJWT == null ? null : decodedJWT.getExpiresAt().getTime();
    }


    /**
     * 获取Payload信息
     */
    public static String getClaim(String token, String key) {
        Map<String, Claim> claims = getClaims(token);
        return claims == null ? null : claims.get(key).asString();
    }

    /**
     * 获取Payload信息
     */
    public static Map<String, Claim> getClaims(String token) {
        DecodedJWT decodedJWT = verity(token);
        return decodedJWT == null ? null : decodedJWT.getClaims();
    }

    /**
     * 解析token
     */
    private static DecodedJWT verity(String token) {
//        Assert.notNull(secret, "secret(密钥) 不能为空");
//        Assert.notNull(issuer, "issuer(发行人) 不能为空");

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build(); //Reusable verifier instance
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
        }
        return null;
    }
}
