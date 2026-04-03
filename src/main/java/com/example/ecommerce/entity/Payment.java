package com.example.ecommerce.entity;
import jakarta.persistence.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.*;
@Entity
public class Payment 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String method;
	private double amount;
	
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date paymentDate;
	
	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	@Column(name ="transacton_id")
	private String transactionId;
	
	public Long getId()
	{
		return id;
	}
	public String getMethod()
	{
		return method;
	}
	public void setMethod(String method)
	{
		this.method=method;
	}
	public double getAmount()
	{
		return amount;
	}
	public void setAmount(double amount)
	{
		this.amount=amount;
	}
	public PaymentStatus getStatus()
	{
		return status;
		
		
		
	}
	public void setStatus(PaymentStatus status)
	{
		this.status=status;
	}
	public Date getPaymentDate()
	{
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate)
	{
		this.paymentDate = paymentDate;
	}
	public Order getOrder()
	{
		return order;
	}
	public void setOrder(Order order)
	{
		this.order=order;
	}
	public String getTransactionId()
	{
		return transactionId;
		
	}
	public void setTransactionId(String transactionId)
	{
		this.transactionId=transactionId;
	}
}
