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
			@Value("${cart.min.quantity}") int minQuantity)
	{
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.minQuantity=minQuantity;
	}
	
	private void ensureCartItemsInitialized(Cart cart)
	{
		if(cart.getItems()==null)
		{
			cart.setItems(new ArrayList<>());
		}
	}
	@Transactional
	public Cart getCart(String username)
	{
		User user=userRepository.findByUsername(username)
				.orElseThrow(()-> new UserNotFoundException("User not found"));
	Cart cart= cartRepository.findByUser(user)
				.orElseThrow(() -> new CartNotFoundException("Cart not found for user"));
	
		ensureCartItemsInitialized(cart);
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
			 ensureCartItemsInitialized(cart);
		        return cart;   
		    }
		Cart newCart = new Cart();
		newCart.setUser(user);
		ensureCartItemsInitialized(newCart);
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
		ensureCartItemsInitialized(cart);
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
		ensureCartItemsInitialized(cart);
		cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
		return cartRepository.save(cart);
	}
	@Transactional
	public Cart viewCart(String username)
	{
		return getCart(username);
	}
}
