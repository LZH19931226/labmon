package com.hc.my.common.core.jwt;

/**
 * @author LiuZhiHao
 * @date 2019/10/21 14:12
 * 描述:
 **/
import com.hc.my.common.core.bean.Audience;
import com.hc.my.common.core.exception.IError;
import com.hc.my.common.core.exception.TokenException;
import com.hc.my.common.core.util.Base64Util;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * @author LiuZhiHao
 * @date 2019/10/16 9:25
 * 描述: jwt集成
 **/
public class JwtTokenUtil {


    public static final String AUTH_HEADER_KEY = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String TOKEN = "token";

    /**
     * 解析jwt
     * @param jsonWebToken
     * @param base64Security
     * @return
     */
    public static Claims parseJWT(String jsonWebToken, String base64Security) {
        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
        } catch (ExpiredJwtException  eje) {
            throw new TokenException(IError.TIMEOUT);
        } catch (Exception e){
            throw new TokenException(IError.TOKEN);
        }
    }

    /**
     * 构建jwt
     * @param userId
     * @param username
     * @param audience
     * @return
     */
    public static String createJWT(String userId, String username,String lang,Audience audience) {
        try {
            // 使用HS256加密算法
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);

            //生成签名密钥
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(audience.getBase64Secret());
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

            //userId是重要信息，进行加密下
            String encryId = Base64Util.encode(userId);
            String lange = Base64Util.encode(lang);

            //添加构成JWT的参数
            JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                    // 可以将基本不重要的对象信息放到claims
                    .claim("userId", encryId)
                    .claim("lang",lange)
                    //.claim("userPermissions",Base64Util.encode(userPermissions))
                    // 代表这个JWT的主体，即它的所有人
                    .setSubject(username)
                    // 代表这个JWT的签发主体；
                    .setIssuer(audience.getClientId())
                    // 是一个时间戳，代表这个JWT的签发时间；
                    .setIssuedAt(new Date())
                    // 代表这个JWT的接收对象；
                    .setAudience(audience.getName())
                    .signWith(signatureAlgorithm, signingKey);
            //添加Token过期时间
            int ttlMillis = audience.getExpiresSecond();
            if (ttlMillis >= 0) {
                long expMillis = nowMillis + ttlMillis;
                Date exp = new Date(expMillis);
                // 是一个时间戳，代表这个JWT的过期时间；
                builder.setExpiration(exp)
                        // 是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的
                        .setNotBefore(now);
            }
            //生成JWT
            return builder.compact();
        } catch (Exception e) {
            throw new TokenException(IError.TOKEN);
        }
    }

    /**
     * 从token中获取用户名
     * @param token
     * @param base64Security
     * @return
     */
    public static String getUsername(String token, String base64Security){
        return parseJWT(token, base64Security).getSubject();
    }

    /**
     * 从token中获取用户ID
     * @param token
     * @param base64Security
     * @return
     */
    public static String getUserId(String token, String base64Security){
        String userId = parseJWT(token, base64Security).get("userId", String.class);
        return Base64Util.decode(userId);
    }

    /**
     * 从token中获取语种
     * @param token
     * @param base64Security
     * @return
     */
    public static String getLang(String token,String base64Security){
        String lang = parseJWT(token, base64Security).get("lang", String.class);
        return Base64Util.decode(lang);
    }


    /**
     * 从token中获取医院id
     * @param token
     * @param base64Security
     * @return
     */
    public static String getHospitalId(String token, String base64Security){
        String userId = parseJWT(token, base64Security).get("hospitalId", String.class);
        return Base64Util.decode(userId);
    }


    /**
     * 是否已过期
     * @param token
     * @param base64Security
     * @return
     */
    public static boolean isExpiration(String token, String base64Security) {
        return parseJWT(token, base64Security).getExpiration().before(new Date());
    }

    public static void  main(String args[]){
        String token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiIxMTEyMyIsInN1YiI6IjExMjMiLCJpc3MiOiIwOThmNmJjZDQ2MjFkMzczY2FkZTRlODMyNjI3YjRmNiIsImlhdCI6MTU3MTY0ODA1OCwiYXVkIjoiaGMiLCJleHAiOjE1NzE2NDg5NTgsIm5iZiI6MTU3MTY0ODA1OH0.VzCi0QEw3IUjU8_q_l8MGsODQVmoRZzCShBehWubz40";
//        String userId =getUserId(token,
//                "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY");
            System.out.println(token.substring(7));


    }

}