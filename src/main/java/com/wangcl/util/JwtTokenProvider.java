package com.wangcl.util;

import com.wangcl.dao.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret:mySecretKeyForDemoPurposesOnlyAndShouldBeLongerThan512Bits1234567890abcdefghijklmnopqrstuvwxyz}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:604800}") // 7 days in seconds
    private int jwtExpiration;

    // 核心修复：固定密钥，不随机生成
    private SecretKey getSigningKey() {
        // 1. 校验密钥非空
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            throw new IllegalArgumentException("JWT密钥不能为空！请配置app.jwt.secret");
        }

        // 2. 强制指定UTF-8编码，避免环境差异
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);

        // 3. 校验密钥长度（HS512要求≥64位），不足则抛异常（让开发者修正）
        if (keyBytes.length < 64) {
            throw new IllegalArgumentException(
                    String.format("HS512算法要求JWT密钥长度≥64位！当前密钥长度：%d位，密钥内容：%s",
                            keyBytes.length, jwtSecret)
            );
        }

        // 4. 返回固定密钥（全程用同一个）
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (long) jwtExpiration * 1000); // 强转long避免int溢出

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setId(user.getId().toString())
                .setIssuedAt(now) // 复用now，避免重复new Date()
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (SignatureException e) {
            throw new RuntimeException("Token签名验证失败：密钥不一致或Token被篡改", e);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token已过期", e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Token格式错误", e);
        } catch (Exception e) {
            throw new RuntimeException("解析Token失败：" + e.getMessage(), e);
        }
    }

    public Long getUserIdFromJWT(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return Long.parseLong(claims.getId());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Token中用户ID格式错误", e);
        } catch (Exception e) {
            throw new RuntimeException("解析用户ID失败：" + e.getMessage(), e);
        }
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 打印具体异常，方便排查
            System.err.println("Token验证失败：" + e.getMessage());
            return false;
        }
    }
}