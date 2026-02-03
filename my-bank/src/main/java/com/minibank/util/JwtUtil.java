package com.minibank.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Properties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

    private static Key key;
    private static long expirationMs;
    private static boolean initialized = false;

    private static void init() {
        if (initialized) return;

        try {
            Properties props = new Properties();
            InputStream is = JwtUtil.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties");

            if (is == null) {
                return;
            }

            props.load(is);

            String secret = props.getProperty("jwt.secret");
            expirationMs = Long.parseLong(props.getProperty("jwt.expiration.ms"));
            key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            initialized = true;
        } catch (Exception e) {
            throw new RuntimeException("failed to load jwt config", e);
        }
    }

    public static String generateTokens(String username) {
        init();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims parseTokens(String token) {
        init();
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean validateToken(String token) {
        try {
            Claims claims = parseTokens(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUsernameFromToken(String token) {
        try {
            return parseTokens(token).getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
