package com.example.demo.helper.JWT;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.dto.UserDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

//	private static final String SECRET_KEY = "weary!!!";
//	private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private static final String SECRET_STRING  = "Vfbv+6vnZIDBbuv5mf0o+hLxUMy9d8FXL+bnl8kAwD4=";
	private static final Key SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_STRING));
	public static Key getSecretKey() {
	    return SECRET_KEY;
	}
	
	private static final int EXPIRATION_TIME = 1000 * 60 * 60; 
	
    public static void main(String[] args) {
    	System.out.println("your key:");
        System.out.println(Base64.getEncoder().encodeToString(SECRET_KEY.getEncoded()));
    }

	
// generateToken
		public static String generateToken(UserDTO userDTO) {
		    Map<String, Object> claims = new HashMap<>();
		    claims.put("userId", userDTO.getUserId());
		    claims.put("name", userDTO.getName());
		    claims.put("email", userDTO.getEmail());
		    claims.put("gender",userDTO.getGender());
		    claims.put("status",userDTO.getStatus());
		    claims.put("coupon",userDTO.getCoupon());
		    
		return Jwts
				.builder()
				.setClaims(claims)
				.setSubject(userDTO.getEmail())
				
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SECRET_KEY, SignatureAlgorithm.HS256)
				.compact();
	}

// extractUsername
	public static String extractUsername(String token) {
		return extractClaims(token).getSubject();
	}

	private static Claims extractClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(SECRET_KEY)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
}