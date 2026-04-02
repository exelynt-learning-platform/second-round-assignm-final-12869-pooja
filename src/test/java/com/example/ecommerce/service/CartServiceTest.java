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
		User user = new User();
		user.setUsername("join");
		
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setItems(new ArrayList<>());
		
		when(userRepository.findByUsername("pooja")).thenReturn(Optional.of(user));
		when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
		
		Cart result = cartService.getOrCreateCart("pooja");
		
		assertNotNull(result);
		assertEquals(user, result.getUser());
		
	}
	@Test
	void testGetOrCreateCart_NewCart()
	{
		User user =new User();
		user.setUsername("john");
		
		when(userRepository.findByUsername("pooja")).thenReturn(Optional.of(user));
		when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
		
		Cart savedCart = new Cart();
		savedCart.setUser(user);
		savedCart.setItems(new ArrayList<>());
		
		when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);
		
		Cart result = cartService.getOrCreateCart("pooja");
		assertNotNull(result);
		verify(cartRepository, times(1)).save(any(Cart.class));
		
	}
	@Test
	void testAddToCart_NewItem()
	{
		User user = new User();
		user.setUsername("pooja");
		
		Product product = new Product();
		product.setId(1L);
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setItems(new ArrayList<>());
		
		when(userRepository.findByUsername("pooja")).thenReturn(Optional.of(user));
		when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		when(cartRepository.save(any(Cart.class))).thenReturn(cart);
		
		Cart result = cartService.addToCart("pooja", 1L, 2);
		
		assertEquals(1,result.getItems().size());
		verify(cartRepository).save(cart);
		
	}
	@Test
	void testUpdateCartItem()
	{
		User user = new User();
		user.setUsername("pooja");
		
		Product product= new Product();
		product.setId(1L);
		
		CartItem item = new CartItem();
		item.setProduct(product);
		
		item.setQuantity(2);
		
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setItems(new ArrayList<>(List.of(item)));
		
		when(userRepository.findByUsername("pooja")).thenReturn(Optional.of(user));
		when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
		when(cartRepository.save(any(Cart.class))).thenReturn(cart);
		
		Cart result = cartService.updateCartItem("pooja", 1L, 5);
		
		assertEquals(5,result.getItems().get(0).getQuantity());
	}
	
	@Test
	void testRemoveFromCart()
	{
		User user = new User();
		user.setUsername("pooja");
		
		Product product = new Product();
		product.setId(1L);
		CartItem item = new CartItem();
		item.setProduct(product);
		
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setItems(new ArrayList<>(List.of(item)));
		
		when(userRepository.findByUsername("pooja")).thenReturn(Optional.of(user));
		when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
		when(cartRepository.save(any(Cart.class))).thenReturn(cart);
		
		Cart result = cartService.removeFromCart("pooja", 1L);
		
		assertEquals(0,result.getItems().size());
	}
	@Test
	void testViewCart()
	{
		User user = new User();
		user.setUsername("pooja");
		
		Cart cart = new Cart();
		cart.setUser(user);
		
		when(userRepository.findByUsername("pooja")).thenReturn(Optional.of(user));
		when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
		
		Cart result = cartService.viewCart("pooja");
		assertNotNull(result);
	}
	
}
