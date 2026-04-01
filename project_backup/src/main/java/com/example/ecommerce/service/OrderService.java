package com.example.ecommerce.service;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class OrderService
{
	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	
	public OrderService(OrderRepository orderRepository,
			CartRepository cartRepository,
			UserRepository userRepository)
	{
		this.orderRepository=orderRepository;
		this.cartRepository=cartRepository;
		this.userRepository=userRepository;
	}
	public Order placeOrder(String username)
	{
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		Cart cart = cartRepository.findByUser(user)
				.orElseThrow(()-> new RuntimeException("cart not found"));
		if(cart.getItems().isEmpty())
		{
			throw new RuntimeException("cart is empty");
		}
		
		Order order = new Order();
		order.setUser(user);
		order.setStatus(OrderStatus.PLACED);
		
		List<OrderItem> orderItems = new ArrayList<>();
		double total = 0;
		
		for(CartItem cartItem : cart.getItems())
		{
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			
			double price = cartItem.getProduct().getPrice();
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
				.orElseThrow(() -> new RuntimeException("User not found"));
		return orderRepository.findByUser(user);
	}
	public Order getOrderById(Long orderId)
	{
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));
	}
}