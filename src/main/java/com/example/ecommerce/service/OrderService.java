package com.example.ecommerce.service;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.exception.CartEmptyException;
import com.example.ecommerce.exception.CartNotFoundException;
import com.example.ecommerce.exception.InsufficientStockException;
import com.example.ecommerce.exception.OrderNotFoundException;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.exception.UserNotFoundException;
import com.example.ecommerce.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
@Service
public class OrderService
{
	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	
	public OrderService(OrderRepository orderRepository,
			CartRepository cartRepository,
			UserRepository userRepository,ProductRepository productRepository)
	{
		this.orderRepository=orderRepository;
		this.cartRepository=cartRepository;
		this.userRepository=userRepository;
		this.productRepository=productRepository;
	}
	@Transactional(rollbackFor = Exception.class)
	public Order placeOrderFromCart(String username)
	{
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		Cart cart = cartRepository.findByUser(user)
				.orElseThrow(()-> new CartNotFoundException("cart not found"));
		if(cart.getItems() == null || cart.getItems().isEmpty())
		{
			throw new CartEmptyException("cart is empty");
		}
		
		Order order = new Order();
		order.setUser(user);
		order.setStatus(OrderStatus.PLACED);
		
		List<OrderItem> orderItems = new ArrayList<>();
		double total = 0;
		
		for(CartItem cartItem : cart.getItems())
		{
			
			Product product=productRepository.findById(cartItem.getProduct().getId())
					.orElseThrow(() -> new ProductNotFoundException("Product not found"));
			if(product.getStockQuantity()<cartItem.getQuantity())
			{
				throw new InsufficientStockException("Insufficient stock for product: " +product.getName());
			}
			product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
			
			productRepository.save(product);
	
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(product);
			orderItem.setQuantity(cartItem.getQuantity());
			
			double price = product.getPrice();
			orderItem.setPrice(price);
			
			total += price * cartItem.getQuantity();
			orderItems.add(orderItem);
		}
		order.setItems(orderItems);
		order.setTotalAmount(total);
		
		Order saveOrder = orderRepository.save(order);
		
		cart.getItems().clear();
		cartRepository.save(cart);
		return saveOrder;
	}
	public List<Order> getUserOrders(String username)
	{
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		return orderRepository.findByUser(user);
	}
	public Order getOrderById(Long orderId)
	{
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found"));
	}
}