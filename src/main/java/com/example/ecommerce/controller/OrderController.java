package com.example.ecommerce.controller;
import com.example.ecommerce.entity.Order;

import com.example.ecommerce.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.ecommerce.dto.OrderRequest;
import jakarta.validation.Valid;
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
	@PostMapping("/place")
	public Order placeOrder(@Valid @RequestBody OrderRequest orderRequest,Authentication authentication)
	{
		String username = authentication.getName();
		return orderService.placeOrder(username,orderRequest);
	}
	@GetMapping
	public List<Order> getUserOrders(Authentication authentication)
	{
		String username = authentication.getName();
		return orderService.getUserOrders(username);
	}
	@GetMapping("/{id}")
	public Order getOrderById(@PathVariable Long id)
	{
		return orderService.getOrderById(id);
	}
}
