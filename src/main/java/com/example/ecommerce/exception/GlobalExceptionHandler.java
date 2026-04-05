package com.example.ecommerce.exception;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


@RestControllerAdvice
public class GlobalExceptionHandler
{
	private static final Logger logger=LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String,Object>> handleNotFound(ResourceNotFoundException ex)
	{
		Map<String,Object> error=new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("message",ex.getMessage());
		error.put("status", HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String,Object>> handleBadRequest(IllegalArgumentException ex)
	{
		Map<String,Object> error= new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("message", ex.getMessage());
		error.put("status", HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
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
	
	@ExceptionHandler(CartNotFoundException.class)
	public ResponseEntity<Map<String, Object>>handleCartNotFound(CartNotFoundException ex)
	{
		Map<String,Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("message", ex.getMessage());
		error.put("status", HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Map<String,Object>>handleUserNotFound(UserNotFoundException ex)
	{
		Map<String,Object> error=new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("message", ex.getMessage());
		error.put("status", HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<Map<String,Object>>handleUserAlreadyExists(UserAlreadyExistsException ex)
	{
		Map<String,Object> error=new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("message", ex.getMessage());
		error.put("status", HttpStatus.CONFLICT.value());
		return new ResponseEntity<>(error,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<Map<String, Object>>handleInsufficientStock(InsufficientStockException ex)
	{
		Map<String,Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("message", ex.getMessage());
		error.put("status", HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidQuantityException.class)
	public ResponseEntity<Map<String,Object>>handleInvalidQuantity(InvalidQuantityException ex)
	{
		Map<String,Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("message", ex.getMessage());
		error.put("status", HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<Map<String,Object>>handleInvalidCredential(InvalidCredentialsException ex)
	{
		Map<String,Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("message", ex.getMessage());
		error.put("status", HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(CartEmptyException.class)
	public ResponseEntity<Map<String,Object>>handleEmptyCart(CartEmptyException ex)
	{
		Map<String,Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("message", ex.getMessage());
		error.put("status", HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<Map<String,Object>>handleOrderNotFound(OrderNotFoundException ex)
	{
		Map<String,Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("message", ex.getMessage());
		error.put("status", HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
		
	}
	

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,Object>> handleValidationException(MethodArgumentNotValidException ex)
	{
		List<String> errors = ex.getBindingResult()
				.getAllErrors()
				.stream()
				.map(error -> error.getDefaultMessage())
				.collect(Collectors.toList());
		logger.info("Validation error : {}", errors);
		
		Map<String,Object> errorResponse=new HashMap<>();
		errorResponse.put("timestamp", LocalDateTime.now());
		errorResponse.put("message", errors);
		errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<Map<String,Object>>handleNullPointer(NullPointerException ex)
	{
		logger.error("Null pointer exception occured", ex);
		Map<String,Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("message", "A required value was missing");
		error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String,Object>>handleDataIntegrity(DataIntegrityViolationException ex)
	{
		logger.error("Data integrity violation",ex);
		Map<String,Object> error= new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("message","Database constraint violation.");
		error.put("status", HttpStatus.CONFLICT.value());
		return new ResponseEntity<>(error,HttpStatus.CONFLICT);
	}
	
	
	
	
	
	
	

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String,Object> >handleGeneral(Exception ex)
	{
		logger.error("Unexpected error occured",ex);
		Map<String,Object> error=new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("message","Something went wrong. Please try again later.");
		error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}


