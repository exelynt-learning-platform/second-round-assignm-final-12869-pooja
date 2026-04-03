package com.example.ecommerce.controller;
import com.example.ecommerce.dto.LoginRequest;

import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.security.JwtTokenProvider;
import com.example.ecommerce.service.UserService;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController 
{
	private static final String BEARER_PREFIX = "Bearer ";
	
	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManager authenticationManager;
	
	
	public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager)
	{
		this.userService = userService;
		this.jwtTokenProvider = jwtTokenProvider;
		this.authenticationManager = authenticationManager;
	}
	@PostMapping("/register")
	public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request)
	{
		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		
		User savedUser = userService.registerUser(user);
		List<SimpleGrantedAuthority> authorities;
		if(savedUser.getRoles() == null)
		{
			authorities = Collections.emptyList();
		}
		else
		{
			authorities= savedUser.getRoles().stream()
					.map(role -> new SimpleGrantedAuthority(role))
					.collect(Collectors.toList());
		}
		
		String token = jwtTokenProvider.generateToken(savedUser.getUsername(), authorities);
		return ResponseEntity.ok(BEARER_PREFIX+ token);
	}
	@PostMapping("/login")
	public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request)
	{
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
		
		String token = jwtTokenProvider.generateToken(request.getUsername(),authentication.getAuthorities());
		
		return ResponseEntity.ok(BEARER_PREFIX + token);
		
				
	}
}