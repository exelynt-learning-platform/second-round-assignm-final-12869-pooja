package com.example.ecommerce.service;
import com.example.ecommerce.entity.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpEntity;
import com.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
@Service
public class PaymentService 
{
	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;
	
	private final RestTemplate restTemplate;

	@Value("${stripe.secret")
	private String stripeSecretKey;
	
	public PaymentService(PaymentRepository paymentRepository, 
			OrderRepository orderRepository,RestTemplate restTemplate)
	{
		this.paymentRepository = paymentRepository;
		this.orderRepository = orderRepository;
		this.restTemplate = restTemplate;
	}
	public Payment makePayment(Long orderId,String method)
	{
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));
		
		//Map<String,Object> body =new HashMap<>();
		MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
		body.add("amount", String.valueOf((int)(order.getTotalAmount()*100)));
		body.add("currency", "usd");
		body.add("payment_method_types[]",method);
		body.add("description", "Payment for order " + order.getId());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth(stripeSecretKey, "");
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
		String stripeUrl ="https://api.stripe.com/v1/payment_intents";
		
		ResponseEntity<Map> response =restTemplate.postForEntity(stripeUrl, entity,Map.class );
		if(!response.getStatusCode().is2xxSuccessful())
		{
			throw new RuntimeException("Stripe payment failed");
		}
		
			Payment payment = new Payment();
			payment.setOrder(order);
			payment.setAmount(order.getTotalAmount());
			payment.setMethod(method);
			payment.setPaymentDate(new Date());
			
			payment.setStatus("Success");
	        order.setStatus(OrderStatus.PAID);
	        orderRepository.save(order);

			
			return paymentRepository.save(payment);
	}
}
