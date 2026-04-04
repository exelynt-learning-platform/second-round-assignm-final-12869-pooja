package com.example.ecommerce.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductDTO 
{
	private Long id;
	
	@NotBlank(message = "Name cannot be blank")
	@Size(min = 2, max=100,message = "Name must be 2-100 characters")
	private String name;
	
	@NotBlank(message = "Description cannot be blank")
	@Size(min=5,max=500,message="Description must be 5-500 characters")
	private String description;
	
	@NotNull(message="price is required")
	@DecimalMin(value="1.0", inclusive=true, message="Price must be 1.0")
	private Double price;
	
	@NotNull(message="Stock quantity is required")
	@Min(value=0,message="Stock quantity cannot be negative")
	private Integer stockQuantity;
	
	private String imageUrl;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
		
	}
	public void setName(String name)
	{
		this.name =name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description =description;
	}
	public Double getPrice()
	{
		return price;
	}
	public void setPrice(Double price)
	{
		this.price=price;
	}
	public Integer getStockQuantity()
	{
		return stockQuantity;
	}
	public void setStockQuantity(Integer stockQuantity)
	{
		this.stockQuantity = stockQuantity;
	}
	public String getImageUrl()
	{
		return imageUrl;
		
	}
	public void setImageUrl(String imageUrl)
	{
		this.imageUrl=imageUrl;
	}
}
