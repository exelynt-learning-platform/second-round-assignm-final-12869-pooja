package com.example.ecommerce.controller;
import com.example.ecommerce.dto.LoginRequest;


import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.security.JwtTokenProvider;
import com.example.ecommerce.service.UserService;
import java.util.*;

import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private final PasswordEncoder passwordEncoder;
	
	
	public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder)
	{
		this.userService = userService;
		this.jwtTokenProvider = jwtTokenProvider;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder=passwordEncoder;
	}
	@PostMapping("/register")
	public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request)
	{
		
		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		Set<String> roles = new HashSet<>();
		roles.add("ROLE_USER");
		User savedUser = userService.registerUser(user, roles);
 		
		Collection<SimpleGrantedAuthority> authorities =  savedUser.getRoles() != null ? savedUser.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role))
				.collect(Collectors.toList()):Collections.emptyList();
		
		
		String token = jwtTokenProvider.generateToken(savedUser.getUsername(), authorities);
		return ResponseEntity.ok(BEARER_PREFIX+ token);
	}
	@PostMapping("/login")
	public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request)
	{
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
		
		String token = jwtTokenProvider.generateToken(authentication.getName(),authentication.getAuthorities());
		
		return ResponseEntity.ok(BEARER_PREFIX + token);
		
				
	}
}