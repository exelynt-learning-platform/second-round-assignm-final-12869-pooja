package com.example.ecommerce.entity;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "cart_items")
public class CartItem 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Product product;
	
	private int quantity;
	
	@ManyToOne
	@JoinColumn(name = "cart_id")
	@JsonBackReference
	private Cart cart;
	public CartItem()
	{}
	public Long getId()
	{
		return id;
		
	}
	public Product getProduct()
	{
		return product;
	}
	public void  setProduct(Product product)
	{
		this.product = product;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int quantity)
	{
		this.quantity =quantity;
	}
	public Cart getCart()
	{
		return cart;
	}
	public void setCart(Cart cart)
	{
		this.cart =cart;
	}
	

}
