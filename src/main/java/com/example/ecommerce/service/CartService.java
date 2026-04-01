package com.example.ecommerce.service;
import com.example.ecommerce.entity.*;
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
	
	public Cart getOrCreateCart(String username)
	{
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("user not found"));
		Optional<Cart> cartOptional = cartRepository.findByUser(user);
		 if(cartOptional.isPresent())
		    {
		        return cartOptional.get();   
		    }
		Cart newCart = new Cart();
		newCart.setUser(user);
		newCart.setItems(new ArrayList<>());
		return cartRepository.save(newCart);
		}
	public Cart addToCart(String username, Long productId, int quantity)
	{
		Cart cart = getOrCreateCart(username);
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));
		for(CartItem item : cart.getItems())
		{
			if(item.getProduct().getId().equals(productId))
			{
				item.setQuantity(item.getQuantity() + quantity);
				return cartRepository.save(cart);
			}
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
		Cart cart = getOrCreateCart(username);
		
		for(CartItem item : cart.getItems())
		{
			if(item.getProduct().getId().equals(productId))
			{
				item.setQuantity(quantity);
				return cartRepository.save(cart);
			}
		}
		throw new RuntimeException("Product not found in cart");
	}
	public Cart removeFromCart(String username,  Long productId)
	{
		Cart cart = getOrCreateCart(username);
		cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
		return cartRepository.save(cart);
	}
	public Cart viewCart(String username)
	{
		return getOrCreateCart(username);
	}
}
