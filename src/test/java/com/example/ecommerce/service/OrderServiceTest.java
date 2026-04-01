package com.example.ecommerce.service;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest 
{
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private CartRepository cartRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private OrderService orderService;
	
	@Test
	void testPlaceOrder_Success()
	{
		User user = new User();
		user.setUsername("pooja");
		
		Product product = new Product();
		product.setId(1L);
		product.setPrice(100.0);
		
		
		CartItem cartItem = new CartItem();
		cartItem.setProduct(product);
		cartItem.setQuantity(2);
		
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setItems(new ArrayList<>(List.of(cartItem)));
		
		when(userRepository.findByUsername("pooja")).thenReturn(Optional.of(user));
		when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
		when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));
		
		Order order = orderService.placeOrder("pooja");
		
		assertNotNull(order);
		assertEquals(200.0,order.getTotalAmount(),0.001);
		assertEquals(OrderStatus.PLACED, order.getStatus());
		assertEquals(1,order.getItems().size());
		
		verify(cartRepository).save(cart);
		
	}
	@Test
	void testPlaceOrder_EmptyCart()
	{
		User user = new User();
		user.setUsername("pooja");
		
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setItems(new ArrayList<>());
		
		when(userRepository.findByUsername("pooja")).thenReturn(Optional.of(user));
		when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
		
		RuntimeException exception = assertThrows(RuntimeException.class, ()->
		{
			orderService.placeOrder("pooja");
		});
		assertEquals("cart is empty", exception.getMessage());
	}
	@Test
	void testGetUSerOrders()
	{
		User user = new User();
		user.setUsername("pooja");
		
		Order order1 = new Order();
		Order order2 = new Order();
		
		when(userRepository.findByUsername("pooja")).thenReturn(Optional.of(user));
		when(orderRepository.findByUser(user)).thenReturn(List.of(order1,order2));
		
		List<Order> orders = orderService.getUserOrders("pooja");
		assertEquals(2,orders.size());
		
	}
	@Test
	void testGetOrderById()
	{
		Order order = new Order();
		order.setId(1L);
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		
		Order result = orderService.getOrderById(1L);
		
		assertEquals(1L,result.getId());
	}
	@Test
	void testGetOrderByID_notFound()
	{
		when(orderRepository.findById(1L)).thenReturn(Optional.empty());
		RuntimeException exception = assertThrows(RuntimeException.class, () ->
		{
			orderService.getOrderById(1L);
		});
		
		assertEquals("Order not found", exception.getMessage());
		
		
	}
	
	
	
	
	
	
}








