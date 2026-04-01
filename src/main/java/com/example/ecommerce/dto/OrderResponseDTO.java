package com.example.ecommerce.dto;
import java.util.*;
public class OrderResponseDTO 
{	
	private Long orderId;
	private double totalAmount;
	private String status;
	private List<CartItemDTO> items;
	
	public Long getOrderId()
	{
		return orderId;
		
	}
	public void setOrderId(Long orderId)
	{
		this.orderId = orderId;
	}
	public double getTotalAmount()
	{
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount)
	{
		this.totalAmount = totalAmount;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status=status;
	}
	public List<CartItemDTO> getItems()
	{
		return items;
	}
	public void setItems(List<CartItemDTO> items)
	{
		this.items=items;
	}
}
