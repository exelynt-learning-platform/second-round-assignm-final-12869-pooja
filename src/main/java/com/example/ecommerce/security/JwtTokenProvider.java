package com.example.ecommerce.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.*;
import com.example.ecommerce.entity.Role;
@Component

public class JwtTokenProvider 
{
	private final String SECRET = "mysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkey123456";
	private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
	
	private final long EXPIRATION = 86400000;
	
    public String generateToken(String username, Collection<? extends GrantedAuthority> roles)
	{
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+ EXPIRATION))
				.signWith(key,SignatureAlgorithm.HS512)
				.compact();
	}
	public String getUsernameFromJwt(String token)
	{
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	public boolean validateToken(String token)
	{
		try
		{
			
			Jwts.parserBuilder().setSigningKey(key)
			.build()
			.parseClaimsJws(token);
			return true;
			
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
}