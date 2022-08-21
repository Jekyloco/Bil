package com.jekyloco.service.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jekyloco.exception.ConditionException;

import java.util.Calendar;
import java.util.Date;

public class TokenUtil {

    private static final String ISSUER = "Jekyloco";

    /**
     * 创建JWT令牌
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public static String generateToken(Long userId) throws Exception {
        //存放的有效信息，一般包含签发者、所面向的用户、接受方、过期时间、签发时间以及唯一身份标识
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //token有效时间,second;
        //利用postman测试: 有效时间为1h
        calendar.add(Calendar.SECOND, 3600);
        return JWT.create().withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER)
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);
    }

    /**
     * JWT令牌验证
     * 抛出令牌过期异常或其他异常
     *
     * @param token
     * @return
     */
    public static Long verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String userId = jwt.getKeyId();
            return Long.valueOf(userId);
        } catch (TokenExpiredException e) {
            //令牌过期异常
            throw new ConditionException("555", "token过期！");
        } catch (Exception e) {
            throw new ConditionException("非法用户token");
        }

    }
}
