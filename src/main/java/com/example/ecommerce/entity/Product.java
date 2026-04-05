package com.example.ecommerce.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "products")
public class Product 
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Product name cannot be blank")
	@Size(max=100, message = "Product name cannot exceed 100 characters")
	private String name;
	
	@NotBlank(message="Description cannot be blank")
	@Size(max=500, message = "Description cannot excced 500 characters")
	private String description;
	
	@Positive(message="Price must be positive")
	private double price;
	
	@Min(value=0, message="StockQuantity cannot be negative")
	private int stockQuantity;
	
	@Size(max=255,message = "Image Url cannot exceed 255 characters")
	private String imageUrl;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id=id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDescription()
	{
		return description;
		
	}
	public void setDescription(String description)
	{
		this.description =description;
	}
	public double getPrice()
	{
		return price;
		
	}
	public void setPrice(double price)
	{
		this.price=price;
	}
	public int getStockQuantity()
	{
		return stockQuantity;
		
	}
	public void setStockQuantity(int stockQuantity)
	{
		this.stockQuantity=stockQuantity;
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
