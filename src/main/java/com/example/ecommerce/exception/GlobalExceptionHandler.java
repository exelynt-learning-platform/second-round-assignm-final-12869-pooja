package com.example.ecommerce.exception;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


@RestControllerAdvice
public class GlobalExceptionHandler
{
	private static final Logger logger=LoggerFactory.getLogger(GlobalExceptionHandler.class);
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex)
	{
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex)
	{
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidation(MethodArgumentNotValidException ex)
	{
		String error=ex.getFieldError()
				
				.getDefaultMessage();
		return ResponseEntity.badRequest().body(error);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGeneral(Exception ex)
	{
		logger.error("Unexcepted error occured",ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Something went wrong. Please try again later");
	}
	@ExceptionHandler(ProductNotFoundException.class)
	
		public ResponseEntity<Map<String,Object>> handleProductNotFound(ProductNotFoundException ex)
		{
			Map<String, Object> error = new HashMap<>();
			error.put("timestamp", LocalDateTime.now());
			error.put("message", ex.getMessage());
			error.put("status", HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
		}
	}

