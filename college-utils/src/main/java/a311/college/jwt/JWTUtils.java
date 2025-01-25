package a311.college.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 */
public class JWTUtils {

    /**
     * 生成JWT令牌
     * @param secretKey 密钥
     * @param millis 过期时间
     * @param claims 存储数据
     * @return JWT令牌
     */
    public static String createJWT(String secretKey, long millis, Map<String, Object> claims) {
        // 指定签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 设置JWT令牌过期时间
        long expMillis = System.currentTimeMillis() + millis;
        Date expTime = new Date(expMillis);

        // 生成JWT令牌
        return Jwts.builder()
                .setClaims(claims)
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                .setExpiration(expTime)
                .compact();
    }

    /**
     * 解析JWT令牌
     * @param secretKey 密钥
     * @param token JTW令牌
     * @return Claims键值对 - 存储数据
     */
    public static Claims parseJWT(String secretKey, String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }

}
