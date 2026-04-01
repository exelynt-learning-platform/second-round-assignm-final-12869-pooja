package com.example.ecommerce.controller;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController 
{
	private final CartService cartService;
	public CartController(CartService cartService)
	{
		this.cartService = cartService;
	}
	@GetMapping
	public Cart viewCart(Authentication authentication)
	{
		String username = authentication.getName();
		return cartService.viewCart(username);
	}
	@PostMapping("/add")
	public Cart addToCart(
			Authentication authentication,
			@RequestParam Long productId,
			@RequestParam int quantity)
	{
		String username = authentication.getName();
		return cartService.addToCart(username, productId, quantity);
	}
	@PutMapping("/update")
	public Cart updateCart(
			Authentication authentication,
			@RequestParam Long productId,
			@RequestParam int quantity)
	{
		String username =authentication.getName();
		return cartService.updateCartItem(username, productId, quantity);
	}
	@DeleteMapping("/remove")
	public Cart removeFromCart(
			Authentication authentication,
			@RequestParam Long productId)
	{
		String username = authentication.getName();
		return cartService.removeFromCart(username, productId);
	}
	
}
