package com.example.ecommerce.controller;
import com.example.ecommerce.entity.Order;

import com.example.ecommerce.service.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController 
{
	private final OrderService orderService;
	
	public OrderController(OrderService orderService) 
	{
		this.orderService = orderService;
	}
	@PostMapping()
	public Order placeOrder(Authentication authentication)
	{
		String username = authentication.getName();
		return orderService.placeOrderFromCart(username);
	}
	@GetMapping
	public List<Order> getUserOrders(Authentication authentication)
	{
		String username = authentication.getName();
		return orderService.getUserOrders(username);
	}
	@GetMapping("/{id}")
	public Order getOrderById(@PathVariable Long id,Authentication authentication)
	{
		String username = authentication.getName();
		Order order=orderService.getOrderById(id);
		if(order == null || order.getUser() == null)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found");
		}
		if(!order.getUser().getUsername().equals(username))
		{
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to view this order");
		}
		return order;
	}
}
