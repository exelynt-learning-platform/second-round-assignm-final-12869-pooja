package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.*;

public class OrderRequest
{
	@NotNull(message = "Product IDs cannot be null")
	@NotEmpty(message = "Product list cannot be empty")
	
	private List<Long> productIds;
	
	public List<Long> getProductIds()
	{
		return productIds;
		
	}
	public void setProductIds(List<Long> productIds)
	{
		this.productIds=productIds;
	}
	
}