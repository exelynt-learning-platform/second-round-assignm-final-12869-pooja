package com.example.ecommerce.service;
import com.example.ecommerce.entity.*;


import com.example.ecommerce.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PaymentServiceTest 
{
	@Mock
	private PaymentRepository paymentRepository;
	
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private PaymentService paymentService;
	
	@BeforeEach
	void setup()
	{
		ReflectionTestUtils.setField(paymentService, "stripeSecretKey", "test_key");
	}
	
	@Test
	void testMakePayment_Success()
	{
		Order order = new Order();
		order.setId(1L);
		order.setTotalAmount(200.0);
		order.setStatus(OrderStatus.PLACED);
		
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		
		ResponseEntity<Map> response =new ResponseEntity<>(new HashMap<>(),HttpStatus.OK);
		
		when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class))).thenReturn(response);
		
		when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArgument(0));
	
		Payment payment = paymentService.makePayment(1L, "card");
	
		assertNotNull(payment);
		assertEquals(200.0,payment.getAmount());
		assertEquals("card",payment.getMethod());
		assertEquals(PaymentStatus.SUCCESS,payment.getStatus());
	 
		assertEquals(OrderStatus.PAID,order.getStatus());
	 	verify(orderRepository).save(order);
	 	verify(paymentRepository).save(any(Payment.class));
	 	
	 
	 	}
	@Test
	void testMakePayment_OrderNotFound()
	{
		when(orderRepository.findById(1L)).thenReturn(Optional.empty());
		RuntimeException exception = assertThrows(RuntimeException.class,() -> 
		{
			paymentService.makePayment(1L, "card");
		});
			assertEquals("Order not found", exception.getMessage());
	}
	@Test
	void testMakePayment_StripeFailure()
	{
		
		Order order = new Order();
		order.setId(1L);
		order.setTotalAmount(200.0);
		
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		
		ResponseEntity<Map> response = new ResponseEntity<>(new HashMap<>(),HttpStatus.BAD_REQUEST);
		
		when(restTemplate.postForEntity(anyString(),any(HttpEntity.class),eq(Map.class))).thenReturn(response);
		
		RuntimeException exception = assertThrows(RuntimeException.class,() ->
		{
			paymentService.makePayment(1L,"card");
		});
		assertEquals("Stripe payment failed", exception.getMessage());
	}
}
