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
	private void validateQuantity(int quantity)
	{
		if(quantity < minQuantity)
		{
			throw new InvalidQuantityException("Quantity must be at least " + minQuantity);  
		}
	}
	private void validateStock(Product product, int quantity)
	{
		if(product.getStockQuantity() < quantity)
		{
			throw new InsufficientStockException("Insufficient stock available");
		}
	}
	private void validateCartNotEmpty(Cart cart)
	{
		if(cart.getItems() == null || cart.getItems().isEmpty())
		{
			throw new CartNotFoundException("Cart is empty");
		}
	}
	private CartItem findCartItem(Cart cart, Long productId)
	{
		return cart.getItems().stream()
				.filter(item -> item.getProduct().getId().equals(productId))
				.findFirst()
				.orElse(null);
	}
	private void addNewCart(Cart cart, Product product ,int quantity)
	{
		CartItem cartItem = new CartItem();
		cartItem.setProduct(product);
		cartItem.setQuantity(quantity);
		cartItem.setCart(cart);
		cart.getItems().add(cartItem);
	}
	private User getUserByUsername(String username)
	{
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
	}
	@Transactional(readOnly = true)
	public Cart getCart(String username)
	{
		User user = getUserByUsername(username);
		return cartRepository.findByUser(user)
				.orElseThrow(() -> new CartNotFoundException("Cart not found for user"));
	}
	@Transactional
	public Cart getOrCreateCart(String username)
	{
		User user = getUserByUsername(username);
		return cartRepository.findByUser(user)
				.orElseGet(() -> {
					Cart newCart = new Cart();
					newCart.setUser(user);
					newCart.setItems(new ArrayList<>());
					return cartRepository.save(newCart);
				});
	}
	@Transactional
	private Cart addtoCart(String username, Long productId, int quantity)
	{
		validateQuantity(quantity);
		
		Cart cart = getOrCreateCart(username);
		
		Product product = productRepository.findById(productId)
				.orElseThrow(() ->  new ProductNotFoundException("Product not found"));
		
		CartItem existingItem = findCartItem(cart,productId);
		
		int totalQuantity = quantity;
		if(existingItem != null)
		{
			totalQuantity += existingItem.getQuantity();
		}
		validateStock(product, totalQuantity);
		
		if(existingItem != null)
		{
			existingItem.setQuantity(totalQuantity);
		}
		else
		{
			addNewCart(cart,product,quantity);
		}
		return cartRepository.save(cart);
	}
	@Transactional
	public Cart updateCartItem(String username, Long productId, int quantity)
	{
		validateQuantity(quantity);
		Cart cart=getCart(username);
		validateCartNotEmpty(cart);
		
		CartItem item = findCartItem(cart,productId);
		if(item == null)
		{
			throw new ProductNotFoundException("Product not found in cart");
		}
		validateStock(item.getProduct(),quantity);
		item.setQuantity(quantity);
		return cartRepository.save(cart);
	}
	@Transactional
	public Cart removeFromCart(String username, Long productId)
	{
		Cart cart = getCart(username);
		validateCartNotEmpty(cart);
		boolean removed = cart.getItems()
				.removeIf(item -> item.getProduct().getId().equals(productId));
		
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

