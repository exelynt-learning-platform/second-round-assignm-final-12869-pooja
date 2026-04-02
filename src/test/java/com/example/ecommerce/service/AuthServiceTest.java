package com.example.ecommerce.service;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.JwtTokenProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.mock.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AuthServiceTest
{
	@Mock
	private UserRepository userRepository;
	
	
	
	@InjectMocks
	private UserService userService;
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Test
	void testRegisterUser()
	{
		User user = new User();
		user.setUsername("pooja");
		user.setEmail("pooja@gmail.com");
		user.setPassword("123");
		
		when(userRepository.save(any(User.class))).thenReturn(user);
		
		User saved = userService.registerUser(user);
		assertEquals("pooja@gmail.com", saved.getEmail());
		verify(userRepository, times(1)).save(user);
	}
	
	@Test
	void testLoginSuccess()
	{
		User user = new User();
		user.setEmail("pooja@gmail.com");
		user.setPassword(passwordEncoder.encode("123"));
		
		when(userRepository.findByEmail("pooja@gmail.com")).thenReturn(Optional.of(user));
		
		
		String token = userService.login("pooja@gmail.com", "123");
		assertNotNull(token);
	}
	@Test
	void testLoginFail()
	{
		when(userRepository.findByEmail("Wrong@gmail.com")).thenReturn(Optional.empty());
		
		RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.login("Wrong@gmail.com", "123"));
		
		assertEquals("Invalid credentials", ex.getMessage());
		
		
	}
	
	
	
}
