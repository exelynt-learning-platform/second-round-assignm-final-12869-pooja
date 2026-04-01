package com.example.ecommerce.entity;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.*;
@Entity
@Table(name = "orders")
public class Order 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL ,orphanRemoval =true)
	@JsonManagedReference
	private List<OrderItem> items = new ArrayList<>();
	
	private double totalAmount;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	private Date createdAt = new Date();
	
	public Long getId()
	{
		return id;
		
	}
	public void setId(Long id)
	{
		this.id=id;
	}
	public User getUser()
	{
		return user;
		
	}
	public void setUser(User user)
	{
		this.user=user;
	}
	public List<OrderItem> getItems()
	{
		return items;
		
	}
	public void setItems(List<OrderItem> items)
	{
		this.items=items;
	}
	public double getTotalAmount()
	{
		return totalAmount;
		
	}
	public void setTotalAmount(double totalAmount)
	{
		this.totalAmount=totalAmount;
	}
	public OrderStatus getStatus()
	{
		return status;
		
		
	}
	public void setStatus(OrderStatus status)
	{
		this.status=status;
	}
	public Date getCreatedAt()
	{
		return createdAt;
		
	}
	public void setCreatedAt(Date createdAt)
	{
		this.createdAt=createdAt;
	}
	
	
	
} 
