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

	private static final String SECRET_KEY;
	private static final long EXPIRATION_MS;
	private static final Key Key;
	
	static {
		try {
			Properties props = new Properties();
			InputStream is = JwtUtil.class.getClassLoader().getResourceAsStream("db.properties");
			
			if(is == null) {
				throw new RuntimeException("db.properties not found in class path");
			}
			
			props.load(is);
			SECRET_KEY = props.getProperty("jwt.secret");
			EXPIRATION_MS = Long.parseLong(props.getProperty("jwt.expiration.ms"));
			
			Key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
			
		}catch(Exception e) {
			throw new RuntimeException("failed to load jwt config", e);
		}
	}
	
	public static String generateTokens(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_MS))
				.signWith(Key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public static Claims parseTokens(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(Key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public static boolean validateToken(String token) {
		try {
			Claims claims = parseTokens(token);
			
			return claims.getExpiration().after(new Date());
		}catch(Exception e) {
			return false;
		}
	}
	
	public static String getUsernameFromToken(String token) {
		try {
			return parseTokens(token).getSubject();
		}catch (Exception e) {
			return null;
		}
	}
}
