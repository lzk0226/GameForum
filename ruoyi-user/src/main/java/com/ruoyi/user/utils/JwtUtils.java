package com.ruoyi.user.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * JWT工具类 - 与若依框架TokenService保持一致
 * @Author : SockLightDust
 * @Date : 2025/5/25
 */
@Component
public class JwtUtils {

    /**
     * JWT签名密钥 - 必须与TokenService中的token.secret一致
     */
    @Value("${token.secret}")
    private String jwtSecretKey;

    /**
     * JWT过期时间（分钟）- 与TokenService保持一致
     */
    @Value("${token.expireTime}")
    private int tokenExpirationMinutes;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final long MILLIS_SECOND = 1000;
    private static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    /**
     * 生成JWT token - 使用HS512算法，与TokenService一致
     */
    public String generateToken(Long userId, String userName) {
        Date currentTime = new Date();
        Date tokenExpirationTime = new Date(currentTime.getTime() + tokenExpirationMinutes * MILLIS_MINUTE);

        return Jwts.builder()
                .setSubject(userName)
                .claim("userId", userId)
                .claim("userName", userName)
                .setIssuedAt(currentTime)
                .setExpiration(tokenExpirationTime)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

    /**
     * 生成刷新token - 使用HS512算法
     */
    public String generateRefreshToken(Long userId, String userName) {
        Date currentTime = new Date();
        // 刷新token有效期为原有效期的2倍
        long refreshExpirationMillis = (long) tokenExpirationMinutes * 2 * MILLIS_MINUTE;
        Date refreshTokenExpirationTime = new Date(currentTime.getTime() + refreshExpirationMillis);

        return Jwts.builder()
                .setSubject(userName)
                .claim("userId", userId)
                .claim("userName", userName)
                .claim("tokenType", "refresh")
                .setIssuedAt(currentTime)
                .setExpiration(refreshTokenExpirationTime)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

    /**
     * 从token中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims tokenClaims = parseTokenClaims(token);
        if (tokenClaims == null) {
            return null;
        }
        Object userIdClaim = tokenClaims.get("userId");
        return userIdClaim != null ? Long.valueOf(userIdClaim.toString()) : null;
    }

    /**
     * 从token中获取用户名
     */
    public String getUserNameFromToken(String token) {
        Claims tokenClaims = parseTokenClaims(token);
        return tokenClaims != null ? tokenClaims.getSubject() : null;
    }

    /**
     * 从token中获取过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims tokenClaims = parseTokenClaims(token);
        return tokenClaims != null ? tokenClaims.getExpiration() : null;
    }

    /**
     * 解析JWT token获取Claims - 使用HS512算法解析，与TokenService一致
     */
    private Claims parseTokenClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * 验证token是否有效
     */
    public boolean validateToken(String token) {
        try {
            // 检查token是否在黑名单中
            if (checkTokenInBlacklist(token)) {
                return false;
            }

            Claims tokenClaims = parseTokenClaims(token);
            if (tokenClaims == null) {
                return false;
            }

            // 检查是否过期
            Date tokenExpirationDate = tokenClaims.getExpiration();
            return tokenExpirationDate.after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 判断token是否即将过期（20分钟内过期则需要刷新）
     * 与TokenService的verifyToken逻辑保持一致
     */
    public boolean isTokenExpiringSoon(String token) {
        Date tokenExpirationDate = getExpirationDateFromToken(token);
        if (tokenExpirationDate == null) {
            return true;
        }

        long millisecondsUntilExpiration = tokenExpirationDate.getTime() - System.currentTimeMillis();
        long MILLIS_MINUTE_TWENTY = 20 * 60 * 1000L;
        return millisecondsUntilExpiration <= MILLIS_MINUTE_TWENTY;
    }

    /**
     * 将token加入黑名单
     */
    public void invalidateToken(String token) {
        try {
            Date tokenExpirationDate = getExpirationDateFromToken(token);
            if (tokenExpirationDate != null) {
                long timeToLiveMillis = tokenExpirationDate.getTime() - System.currentTimeMillis();
                if (timeToLiveMillis > 0) {
                    String blacklistKey = "blacklist:" + token;
                    redisTemplate.opsForValue().set(blacklistKey, "invalid", timeToLiveMillis, TimeUnit.MILLISECONDS);
                }
            }
        } catch (Exception ex) {
            System.err.println("Failed to invalidate token: " + ex.getMessage());
        }
    }

    /**
     * 检查token是否在黑名单中
     */
    private boolean checkTokenInBlacklist(String token) {
        try {
            String blacklistKey = "blacklist:" + token;
            return Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey));
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 获取token过期时间（秒）
     */
    public long getExpiration() {
        return (long) tokenExpirationMinutes * 60;
    }

    /**
     * 获取刷新token过期时间（秒）
     */
    public long getRefreshExpiration() {
        return (long) tokenExpirationMinutes * 2 * 60;
    }

    /**
     * 从token中获取剩余有效时间（秒）
     */
    public long getRemainingTime(String token) {
        Date tokenExpirationDate = getExpirationDateFromToken(token);
        if (tokenExpirationDate == null) {
            return 0;
        }
        long remainingTimeMillis = tokenExpirationDate.getTime() - System.currentTimeMillis();
        return Math.max(0, remainingTimeMillis / 1000);
    }
}