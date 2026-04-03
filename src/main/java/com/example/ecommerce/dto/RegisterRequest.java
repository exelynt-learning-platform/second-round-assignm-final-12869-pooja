package com.example.ecommerce.dto;
import jakarta.validation.constraints.*;

public class RegisterRequest 
{
	@NotBlank
	private String username;
	
	@Email
	private String email;
	
	@NotBlank
	@Pattern(  regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
	message="Password must be at least * chars, including uppercase,numbers and special character")
	private String password;
	
	public String getUsername()
	{
		return username;
		
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password =password;
	}
}
