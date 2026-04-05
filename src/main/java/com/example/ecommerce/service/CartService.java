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
	
	
	@Transactional
	public Cart getCart(String username)
	{
		User user=userRepository.findByUsername(username)
				.orElseThrow(()-> new UserNotFoundException("User not found"));
	Cart cart= cartRepository.findByUser(user)
				.orElseThrow(() -> new CartNotFoundException("Cart not found for user"));
	
	return cart;
	}
	
	@Transactional
	public Cart getOrCreateCart(String username)
	{
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		Optional<Cart> cartOptional = cartRepository.findByUser(user);
		 if(cartOptional.isPresent())
		    {
			 Cart cart = cartOptional.get();
		        return cart;   
		    }
		Cart newCart = new Cart();
		newCart.setUser(user);
		return cartRepository.save(newCart);
		}
	@Transactional
	public Cart addToCart(String username, Long productId, int quantity)
	{
		Cart cart = getOrCreateCart(username);
		if(quantity <minQuantity)
		{
			throw new InvalidQuantityException("Quantity must be at least " + minQuantity);
		}
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));
		
		if(product.getStockQuantity()<quantity)
		{
			throw new InsufficientStockException("Insufficient stock available");
		}
		Optional<CartItem> existingItem = cart.getItems()
				.stream()
				.filter(item -> item.getProduct().getId().equals(productId))
				.findFirst();
		if(existingItem.isPresent())
		{
			CartItem item = existingItem.get();
			int newQuantity = item.getQuantity() + quantity;
			if((product.getStockQuantity()) < newQuantity)
			{
				throw new InsufficientStockException("Insufficient stock available");
			}
				item.setQuantity(newQuantity);
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
		Cart cart = getCart(username);
		
		if(cart.getItems().isEmpty())
		{
			throw new CartNotFoundException("cart is Empty");
		}

		
		Optional<CartItem> existingItem = cart.getItems()
				.stream()
				.filter(item -> item.getProduct().getId().equals(productId))
				.findFirst();
		if(existingItem.isPresent())
		{
			if(quantity < minQuantity)
			{
				throw new InvalidQuantityException("Quantity must be at least " + minQuantity);
			}
			CartItem item =existingItem.get();
			Product product=item.getProduct();
			if(product.getStockQuantity() < quantity)
			{
				throw new InsufficientStockException("Insufficient stock available");
			}
				item.setQuantity(quantity);
				return cartRepository.save(cart);
			}
		throw new CartNotFoundException("Product not found in cart");
		
	}
	@Transactional
	public Cart removeFromCart(String username,  Long productId)
	{
		Cart cart = getCart(username);
		if(cart.getItems().isEmpty() )
		{
			throw new CartNotFoundException("Cart is empty");
		}
			
		boolean removed = cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
				if(!removed)
				{
					throw new CartNotFoundException("Product not found in cart");
				}
	return cartRepository.save(cart);
	}
	@Transactional
	public Cart viewCart(String username)
	{
		return getCart(username);
	}
}