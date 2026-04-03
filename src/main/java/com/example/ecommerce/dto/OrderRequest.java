package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.*;

import com.example.ecommerce.entity.OrderItem;

public class OrderRequest
{
	@NotNull(message = "Product IDs cannot be null")
	@NotEmpty(message = "Product list cannot be empty")
	
	private List<Long> productIDs;
	
	
}
