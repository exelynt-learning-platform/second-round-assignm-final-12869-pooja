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
	
	@NotBlank
	@Size(min = 2, max=100)
	private String name;
	
	@NotBlank
	@Size(min=5, max=500)
	private String description;
	
	@NotNull
	@DecimalMin(value = "1.0", inclusive=true)
	private double price;
	
	@NotNull
	@Min(0)
	private Integer stockQuantity;
	
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
	public Integer getStockQuantity()
	{
		return stockQuantity;
		
	}
	public void setStockQuantity(Integer stockQuantity)
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
