package com.nuomi.backend.utils;

import com.nuomi.backend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * JWT工具类，用于生成和解析Token
 */
public class JwtUtil {
    // 建议放到配置文件
    private static final String SECRET = "campus-trade-secret";
    private static final long EXPIRATION = 7 * 24 * 3600 * 1000L; // 7天，单位毫秒

    /**
     * 生成token
     * @param user 用户ID
     * @return JWT字符串
     */
    public static String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getUserId())
                .claim("nickname", user.getNickname())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * 解析token，获取userId
     * @param token JWT字符串
     * @return 用户ID（Long），如果无效返回null
     */
    public static Long getUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("userId", Long.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析token，获取username
     * @param token JWT字符串
     * @return 用户名（String），如果无效返回null
     */
    public static String getUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}