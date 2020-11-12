package com.blog.system.util;

import com.blog.system.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 生成token、解析token、校验token
 */
@Component
public class JwtUtil {

    @Value("${acceptToken}")
    private String acceptToken;

    /**
     * 用户登录成功后生成Jwt
     * 使用Hs256算法 私钥是使用用户密码
     * @param ttlMillis
     * @param user
     * @return
     */
    public String createJWT(long ttlMillis, User user){
        SignatureAlgorithm signatureAlgorithm=SignatureAlgorithm.HS256;

        long nowMillis=System.currentTimeMillis();
        Date now=new Date(nowMillis);

        Map<String,Object> claims=new HashMap<String,Object>();
        claims.put("user_name",user.getUser_name());
        String key=acceptToken;
        String subject=user.getUser_name();

        JwtBuilder builder= Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(signatureAlgorithm,key);
        if(ttlMillis>0){
            long expMillis=nowMillis+ttlMillis;
            Date exp=new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * Token的解密
     * @param token 加密后的token
     * @return
     */
    public Claims parseJWT(String token) {
        //签名秘钥，和生成的签名的秘钥一模一样
        String key = acceptToken;

        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(key)
                //设置需要解析的jwt
                .parseClaimsJws(token).getBody();
        return claims;
    }

    /**
     * 校验token
     * 在这里可以使用官方的校验，我这里校验的是token中携带的密码于数据库一致的话就校验通过
     * @param token
     * @return
     */
    public Boolean isVerify(String token) {
        //签名秘钥，和生成的签名的秘钥一模一样
        String key = acceptToken;

        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(key)
                //设置需要解析的jwt
                .parseClaimsJws(token).getBody();

        if (claims.get("password").equals(acceptToken)) {
            return true;
        }
        return false;
    }
}
