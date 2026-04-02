package com.example.ecommerce.security;
import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;


import java.security.Key;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.util.*;
import java.util.stream.Collectors;

import com.example.ecommerce.entity.Role;
@Component

public class JwtTokenProvider 
{
	@Value("${security.jwt.secret}")
	private  String secret;
	
	@Value("${security.jwt.expiration:86400000}")
	private long expiration;
	private Key key;
	
	@PostConstruct
	public void init()
	{
		key=Keys.hmacShaKeyFor(secret.getBytes());
	}
	
    public String generateToken(String username, Collection<? extends GrantedAuthority> roles)
	{
    	String roleString ="";
    	if(roles !=null)
    	{
    		roleString=roles.stream()
    				.map(GrantedAuthority::getAuthority)
    				.collect(Collectors.joining(","));
    	}
    	
    	
    	
    	
		return Jwts.builder()
				.setSubject(username)
				.claim("roles", roleString)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+ expiration))
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