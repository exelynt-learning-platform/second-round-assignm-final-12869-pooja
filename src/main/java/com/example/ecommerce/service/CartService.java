package com.example.ecommerce.service;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.exception.CartNotFoundException;
import com.example.ecommerce.exception.InsufficientStockException;
import com.example.ecommerce.exception.InvalidQuantityException;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.exception.UserNotFoundException;
import com.example.ecommerce.repository.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartService 
{
	private final CartRepository cartRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	
	private final int minQuantity;
	
	public CartService(CartRepository cartRepository,
			ProductRepository productRepository,
			UserRepository userRepository,
			@Value("${cart.min.quantity:1}") int minQuantity)
	{
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.minQuantity=minQuantity;
	}
	
	private User getUserByUsername(String username)
	{
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
	}
	
	@Transactional(readOnly = true)
	public Cart getCart(String username)
	{
		User user=getUserByUsername(username);
		return cartRepository.findByUser(user)
				.orElseThrow(() -> new CartNotFoundException("Cart not found for user"));
	}
	
	@Transactional
	public Cart getOrCreateCart(String username)
	{
		User user = getUserByUsername(username);
		return cartRepository.findByUser(user)
				.orElseGet(() -> {
					Cart newCart= new Cart();
					newCart.setUser(user);
					newCart.setItems(new ArrayList<>());
					return cartRepository.save(newCart);
				});
		
				
		}
	@Transactional
	public Cart addToCart(String username, Long productId, int quantity)
	{
		if(quantity <minQuantity)
		{
			throw new InvalidQuantityException("Quantity must be at least " + minQuantity);
		}
		Cart cart = getOrCreateCart(username);

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));
		
		Optional<CartItem> existingItem = cart.getItems()
				.stream()
				.filter(item -> item.getProduct().getId().equals(productId))
				.findFirst();
		int totalQuantity = quantity;
		if(existingItem.isPresent())
		{
			totalQuantity += existingItem.get().getQuantity();
		}
		if(product.getStockQuantity() < totalQuantity)
		{
			throw new InsufficientStockException("Insufficient stock available");
		}
		if(existingItem.isPresent())
		{
			existingItem.get().setQuantity(totalQuantity);
		}
		else
		{
			CartItem cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setQuantity(quantity);
			cartItem.setCart(cart);
			cart.getItems().add(cartItem);
		}
		return cartRepository.save(cart);
		
	}
	@Transactional
	public Cart updateCartItem(String username, Long productId, int quantity)
	{
		if(quantity < minQuantity)
		{
			throw new InvalidQuantityException("Quantity must be at least " + minQuantity);
		}
		Cart cart = getCart(username);
		
		if(cart.getItems() == null || cart.getItems().isEmpty())
		{
			throw new CartNotFoundException("cart is Empty");
		}
		CartItem items = cart.getItems().stream()
				.filter(i -> i.getProduct().getId().equals(productId))
				.findFirst()
				.orElseThrow(() -> new ProductNotFoundException("Product not found in cart"));
		Product product =items.getProduct();
		if(product.getStockQuantity()<quantity)
		{
			throw new InsufficientStockException("Insufficient stock available");
		}
		items.setQuantity(quantity);
		return cartRepository.save(cart);
	}
	@Transactional
	public Cart removeFromCart(String username,  Long productId)
	{
		Cart cart = getCart(username);
		if(cart.getItems() == null ||cart.getItems().isEmpty() )
		{
			throw new CartNotFoundException("Cart is Empty");
		}
			
		boolean removed = cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
				if(!removed)
				{
					throw new ProductNotFoundException("Product not found in cart");
				}
	return cartRepository.save(cart);
	}
	@Transactional(readOnly = true)
	public Cart viewCart(String username)
	{
		return getCart(username);
	}
}