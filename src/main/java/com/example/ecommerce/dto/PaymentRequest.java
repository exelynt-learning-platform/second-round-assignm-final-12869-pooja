package com.example.ecommerce.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
public class PaymentRequest 
{	
	@NotNull
	private Long orderId;
	
	@NotBlank
	private String method;
	
	public PaymentRequest(Long orderId,String method)
	{
		this.orderId = orderId;
		this.method = method;
	}
	public Long getOrderId()
	{
		return orderId;
	}
	public void setOrderId(Long orderId)
	{
		this.orderId = orderId;
	}
	public String getMethod()
	{
		return method;
	}
	public void setMethod(String method)
	{
		this.method=method;
	}

}
