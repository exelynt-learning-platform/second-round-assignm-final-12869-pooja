package com.example.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CartItemDTO 
{
	@NotNull(message = "Product Id cannot be null")
	private Long productId;
	
	@NotNull(message = "Product name cannot be null")
	@Size(min=1,max=255, message = "Product name must be between 1 and 255 characters")
	private String productName;
	
	@Min(value = 1, message = "Quantity must be at least 1")
	private int quantity;
	
	@Positive(message = "Price must be positive")
	private double price;
	
	public CartItemDTO()
	{
		
	}
	
	public CartItemDTO(Long productId,String productName,int quantity,double price)
	{
		this.productId=productId;
		this.productName=productName;
		this.quantity=quantity;
		this.price=price;
	}
	
	
	public Long getProductId()
	{
		return productId;
	}
	public void setProductId(Long productId)
	{
		this.productId=productId;
	}
	public String getProductName()
	{
		return productName;
	}
	public void setProductName(String productName)
	{
		this.productName=productName;
	}
	public int getQuantity()
	{
		return quantity;
		
	}
	public void setQuantity(int quantity)
	{
		this.quantity=quantity;
	}
	public double getPrice()
	{
		return price;
	}
	public void setPrice(double price)
	{
		this.price=price;
	}
	@Override
	public String toString()
	{
		return "CartItemDTO{" + "productId=" + productId +", productName='" +productName + '\'' +",quantity=" + quantity +",price=" + price +'}';
				
	}
}

