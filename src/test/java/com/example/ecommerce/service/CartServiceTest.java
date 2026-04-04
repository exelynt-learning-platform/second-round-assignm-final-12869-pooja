package com.example.ecommerce.service;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CartServiceTest 
{
	@Mock
	private CartRepository cartRepository;
	
	@Mock
	private CartItemRepository cartItemRepository;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private CartService cartService;
	
	@Test
	void testGetOrCreateCart_ExistingCart()
	{
		String username="pooja";
		
		User user = new User();
		user.setUsername(username);
		
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setItems(new ArrayList<>());
		
		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
		
		Cart result = cartService.getOrCreateCart(username);
		
		assertNotNull(result);
		assertEquals(user, result.getUser());
		
	}
	@Test
	void testGetOrCreateCart_NewCart()
	{
		String username="pooja";
		User user =new User();
		user.setUsername(username);
		
		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
		
		Cart savedCart = new Cart();
		savedCart.setUser(user);
		savedCart.setItems(new ArrayList<>());
		
		when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);
		
		Cart result = cartService.getOrCreateCart(username);
		assertNotNull(result);
		verify(cartRepository, times(1)).save(any(Cart.class));
		
	}
	@Test
	void testAddToCart_NewItem()
	{
		String username="pooja";
		User user = new User();
		user.setUsername(username);
		
		Product product = new Product();
		product.setId(1L);
		product.setStockQuantity(10);
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setItems(new ArrayList<>());
		
		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		when(cartRepository.save(any(Cart.class))).thenReturn(cart);
		
		Cart result = cartService.addToCart(username, 1L, 2);
		
		assertEquals(1,result.getItems().size());
		verify(cartRepository).save(cart);
		
	}
	@Test
	void testUpdateCartItem()
	{
		String username="pooja";
		User user = new User();
		user.setUsername(username);
		
		Product product= new Product();
		product.setId(1L);
		product.setStockQuantity(10);
		
		CartItem item = new CartItem();
		item.setProduct(product);
		
		item.setQuantity(2);
		
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setItems(new ArrayList<>(List.of(item)));
		
		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
		when(cartRepository.save(any(Cart.class))).thenReturn(cart);
		
		Cart result = cartService.updateCartItem(username, 1L, 5);
		
		assertEquals(5,result.getItems().get(0).getQuantity());
	}
	
	@Test
	void testRemoveFromCart()
	{
		String username="pooja";
		User user = new User();
		user.setUsername(username);
		
		Product product = new Product();
		product.setId(1L);
		CartItem item = new CartItem();
		item.setProduct(product);
		
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setItems(new ArrayList<>(List.of(item)));
		
		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
		when(cartRepository.save(any(Cart.class))).thenReturn(cart);
		
		Cart result = cartService.removeFromCart(username, 1L);
		
		assertEquals(0,result.getItems().size());
	}
	@Test
	void testViewCart()
	{
		String username="pooja";
		User user = new User();
		user.setUsername(username);
		
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setItems(new ArrayList<>());
		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
		
		Cart result = cartService.viewCart(username);
		assertNotNull(result);
	}
	
}
