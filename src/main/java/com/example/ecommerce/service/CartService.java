package com.example.ecommerce.service;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService 
{
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	
	public CartService(CartRepository cartRepository,
			CartItemRepository cartItemRepository,
			ProductRepository productRepository,
			UserRepository userRepository)
	{
		this.cartRepository = cartRepository;
		this.cartItemRepository =cartItemRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
	}
	public Cart getCart(String username)
	{
		return getOrCreateCart(username);
	}
	
	public Cart getOrCreateCart(String username)
	{
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("user not found"));
		Optional<Cart> cartOptional = cartRepository.findByUser(user);
		 if(cartOptional.isPresent())
		    {
			 Cart cart = cartOptional.get();
			 if(cart.getItems() == null)
			 {
				 cart.setItems(new ArrayList<>());
			 }
		        return cart;   
		    }
		Cart newCart = new Cart();
		newCart.setUser(user);
		newCart.setItems(new ArrayList<>());
		return cartRepository.save(newCart);
		}
	public Cart addToCart(String username, Long productId, int quantity)
	{
		Cart cart = getCart(username);
		if(quantity <=0)
		{
			throw new RuntimeException("Quantity must be greater than 0");
		}
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));
		
		if(product.getStockQuantity()<quantity)
		{
			throw new RuntimeException("Insufficient stock available");
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
				throw new RuntimeException("Insufficient stock available");
			}
				item.setQuantity(newQuantity);
				return cartRepository.save(cart);
		}
		CartItem cartItem = new CartItem();
		cartItem.setProduct(product);
		cartItem.setQuantity(quantity);
		cartItem.setCart(cart);
		
		cart.getItems().add(cartItem);
		return cartRepository.save(cart);
	}
	public Cart updateCartItem(String username, Long productId, int quantity)
	{
		Cart cart = getCart(username);
		
		Optional<CartItem> existingItem = cart.getItems()
				.stream()
				.filter(item -> item.getProduct().getId().equals(productId))
				.findFirst();
		if(existingItem.isPresent())
		{
			if(quantity <= 0)
			{
				throw new RuntimeException("Quantity must be greater than 0");
			}
			CartItem item =existingItem.get();
			Product product=item.getProduct();
			if(product.getStockQuantity() < quantity)
			{
				throw new RuntimeException("Insufficient stock available");
			}
				item.setQuantity(quantity);
				return cartRepository.save(cart);
			}
		
		throw new ResourceNotFoundException("Product not found in cart");
	}
	public Cart removeFromCart(String username,  Long productId)
	{
		Cart cart = getCart(username);
		cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
		return cartRepository.save(cart);
	}
	public Cart viewCart(String username)
	{
		return getCart(username);
	}
}
