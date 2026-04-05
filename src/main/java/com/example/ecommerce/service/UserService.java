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
	public User registerUser(User user,Set<String> requestRoles)
	{
		if(userRepository.existsByUsername(user.getUsername()))
		{
			throw new UserAlreadyExistsException("Username already exists");
		}
		if(userRepository.existsByEmail(user.getEmail()))
		{
			throw new UserAlreadyExistsException("Email alreay exists");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		Set<String> roles =new HashSet<>();
		if(requestRoles == null || requestRoles.isEmpty())
		{
			roles.add(Role.ROLE_USER.name());
		}
		else
		{
			for(String role : requestRoles)
			{
				if(role.equals(Role.ROLE_USER.name()) || role.equals(Role.ROLE_ADMIN.name()))
				{
					roles.add(role);
				}
				else
				{
					throw new IllegalArgumentException("Invalid role:" +role);
				}
			}
		}
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
