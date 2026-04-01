package com.example.ecommerce.controller;
import com.example.ecommerce.entity.Payment;
import com.example.ecommerce.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController
{
	private final PaymentService paymentService;
	
	public PaymentController(PaymentService paymentService)
	{
		this.paymentService = paymentService;
	}
	@PostMapping("/{orderId}")
	public ResponseEntity<Payment> pay(
			@PathVariable Long orderId,
			@RequestParam String method)
	{
		Payment payment = paymentService.makePayment(orderId, method);
		return ResponseEntity.ok(payment);
	}
}
