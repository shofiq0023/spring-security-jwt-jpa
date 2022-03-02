package com.security.jwt.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
	private final String SECRET_KEY = "shofiqul";
	
	// Extracting username from token using extract claims
	public String extractUsername(String token) {
		return extractClaims(token, Claims::getSubject);
	}
	
	// Extracting expiration date from token using claims
	public Date extractExpiration(String token) {
		return extractClaims(token, Claims::getExpiration);
	}
	
	// Extract claims
	public <T>T extractClaims(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	// Check is the token is expired
	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	// Extracting all claims from a jwt token
	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY)
				   .parseClaimsJws(token)
				   .getBody();
	}
	
	// Generating the token from user information
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}
	
	/* There are some steps to create a JWT token
	 * 1. Call jwts builder and set claims like issue date, setting the subject, expiration date 
	 * 2. Then sign the jwt with H512 signature algorith and the SECRET key
	 * 3. Then compact the jwt into a string
	 */
	public String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
			 			.setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 60 * 1000))
			 			.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
			 			.compact();
	}
	
	// Validate the token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
