package com.example.ecommerce.service;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class UserService 
{
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository)
	{
		this.userRepository = userRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
		
	}
	public User registerUser(User user)
	{
		if(userRepository.existsByUsername(user.getUsername()))
		{
			throw new RuntimeException("Username already exists");
		}
		if(userRepository.existsByEmail(user.getEmail()))
		{
			throw new RuntimeException("Email already exists");
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Set<String> roles = new HashSet<>();
		roles.add(Role.ROLE_USER.name());
		user.setRoles(roles);
		
		return userRepository.save(user);
		
	}
	public String login(String email, String password)
	{
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if(optionalUser.isEmpty())
		{
			throw new RuntimeException("Invalid credentials");
		}
		User user = optionalUser.get();
		
		if(!passwordEncoder.matches(password, user.getPassword()))
		{
			throw new RuntimeException("Invalid Credentials");
		}
		return "dummy-token-for-" + user.getUsername();
	}
}
