package com.example.ecommerce.service;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.exception.InvalidCredentialsException;
import com.example.ecommerce.exception.UserAlreadyExistsException;
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
	
	public UserService(UserRepository userRepository,BCryptPasswordEncoder passwordEncoder)
	{
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		
	}
	public User registerUser(User user)
	{
		if(userRepository.existsByUsername(user.getUsername()))
		{
			throw new UserAlreadyExistsException("Username already exists");
		}
		if(userRepository.existsByEmail(user.getEmail()))
		{
			throw new UserAlreadyExistsException("Email already exists");
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Set<String> roles = new HashSet<>();
		roles.add(Role.ROLE_USER.name());
		user.setRoles(roles);
		
		return userRepository.save(user);
		
	}
	public String login(String username, String password)
	{
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if(optionalUser.isEmpty())
		{
			throw new InvalidCredentialsException("Invalid credentials");
		}
		User user = optionalUser.get();
		
		if(!passwordEncoder.matches(password, user.getPassword()))
		{
			throw new InvalidCredentialsException("Invalid Credentials");
		}
		return "dummy-token-for-" + user.getUsername();
	}
}
