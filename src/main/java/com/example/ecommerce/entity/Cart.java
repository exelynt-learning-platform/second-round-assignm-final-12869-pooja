package com.example.ecommerce.entity;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.*;
@Entity
@Table(name = "carts")
public class Cart 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private User user;
	
	@OneToMany(mappedBy ="cart", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<CartItem> items;
	public Cart()
	{
		this.items=new ArrayList<>();
	}
	public Long getId()
	{
		return id;
	}
	public User getUser()
	{
		return user;
	}
	public void setUser(User user)
	{
		this.user = user;
	}
	public List<CartItem> getItems()
	{
		
		return items;
	}
	public void setItems(List<CartItem>items)
	{
		this.items=items;
	}
	
	
}
