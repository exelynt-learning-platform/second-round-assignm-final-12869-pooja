package com.example.ecommerce.service;
import com.example.ecommerce.entity.Product;

import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ProductServiceTest
{
	@Mock
	private ProductRepository productRepository;
	
	@InjectMocks
	private ProductService productService;
	
	@Test
	void testAddProduct()
	{
		Product product = new Product();
		product.setName("Laptop");
		product.setPrice(10000);
		when(productRepository.save(any(Product.class))).thenReturn(product);
		Product saved = productService.createProduct(product);
		assertEquals("Laptop" , saved.getName());
		verify(productRepository, times(1)).save(product);
	}
	@Test
	void testGetAllProducts()
	{
		Product p1 = new Product();
		p1.setName("Phone");
		Product p2 = new Product();
		p2.setName("Laptop");
		
		when(productRepository.findAll()).thenReturn(List.of(p1,p2));
		List<Product> products = productService.getAllProduct();
		assertEquals(2, products.size());
	}
	@Test
	void testGetProductById()
	{
		Product product = new Product();
		product.setId(1L);
		product.setName("Phone");
		
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		 Product p =productService.getProductbyId(1L);
		 assertEquals("Phone",p.getName());
	}
}
