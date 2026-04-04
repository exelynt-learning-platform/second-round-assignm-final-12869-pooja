package com.example.ecommerce.service;
import com.example.ecommerce.entity.*;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpEntity;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.entity.PaymentStatus;

import jakarta.transaction.Transactional;

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

	@Value("${stripe.secret.key}")
	private String stripeSecretKey;
	
	
	private static final String STRIPE_STATUS_REQUIRES_PAYMENT_METHOD ="requires_payment_method";
	private static final String STRIPE_STATUS_REQUIRES_CONFIRMATION ="requires_confirmation";
	private static final String STRIPE_STATUS_REQUIRES_ACTION ="requires_action";
	private static final String STRIPE_STATUS_SUCCEEDED="succeeded";
	
	
	private static final Set<String> PENDING_STATUSES = Set.of(
			STRIPE_STATUS_REQUIRES_PAYMENT_METHOD,
			STRIPE_STATUS_REQUIRES_CONFIRMATION,
			STRIPE_STATUS_REQUIRES_ACTION);
			
	
	public PaymentService(PaymentRepository paymentRepository, 
			OrderRepository orderRepository,RestTemplate restTemplate)
	{
		this.paymentRepository = paymentRepository;
		this.orderRepository = orderRepository;
		this.restTemplate = restTemplate;
	}
	@Transactional
	public Payment makePayment(Long orderId,String method)
	{
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));
		
		MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
		body.add("amount", String.valueOf(Math.round(order.getTotalAmount()*100)));
		body.add("currency", "inr");
		body.add("payment_method_types[]",method);
		body.add("description", "Payment for order " + order.getId());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBearerAuth(stripeSecretKey);
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
		String stripeUrl ="https://api.stripe.com/v1/payment_intents";
		
		ResponseEntity<Map<String, Object>> response;
		try
		{
			response = restTemplate.exchange(stripeUrl,org.springframework.http.HttpMethod.POST,
					entity,
					new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>()
					{});
		}
		catch(Exception e)
		{
			
			throw new RuntimeException("Stripe API error: " + e.getMessage(),e);
		}
		if(!response.getStatusCode().is2xxSuccessful() || response.getBody() == null)
		{
			throw new RuntimeException("Stripe payment failed");
		}
		Map<String, Object> responseBody = response.getBody();
		String paymentIntentId = (String) responseBody.get("id");
		String status = Objects.toString(responseBody.get("status"), "");
		
			Payment payment = new Payment();
			payment.setOrder(order);
			payment.setAmount(order.getTotalAmount());
			payment.setMethod(method);
			payment.setPaymentDate(new Date());
			payment.setTransactionId(paymentIntentId);
			
			
			PaymentStatus paymentStatus=mapStripeStatusToPaymentStatus(status);
			payment.setStatus(paymentStatus);
			
			if(paymentStatus == PaymentStatus.SUCCESS)
			{
				order.setStatus(OrderStatus.PAID);
				orderRepository.save(order);
			}
			return paymentRepository.save(payment);
	}
	private PaymentStatus mapStripeStatusToPaymentStatus(String stripeStatus)
	{
		if(PENDING_STATUSES.contains(stripeStatus))
		{
			return PaymentStatus.PENDING;
		}
		else if(STRIPE_STATUS_SUCCEEDED.equals(stripeStatus))
		{
			return PaymentStatus.SUCCESS;
		}
		else
		{
			return PaymentStatus.FAILED;
		}
	}
}
