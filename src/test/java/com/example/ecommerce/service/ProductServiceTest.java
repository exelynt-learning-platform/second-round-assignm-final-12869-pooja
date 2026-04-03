package com.example.ecommerce.service;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.dto.ProductDTO;


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
		ProductDTO productDTO=new ProductDTO();
		productDTO.setName("Laptop");
		productDTO.setPrice(1000.0);
		productDTO.setStockQuantity(5);
		
		Product productEntity = new Product();
		productEntity.setName(productDTO.getName());
		productEntity.setPrice(productDTO.getPrice());
		productEntity.setStockQuantity(productDTO.getStockQuantity());
		
		when(productRepository.save(any(Product.class))).thenReturn(productEntity);
		
		Product saved= productService.createProduct(productDTO);
		
		assertEquals("Laptop",saved.getName());
		assertEquals(1000.0,saved.getPrice());
		assertEquals(5,saved.getStockQuantity());
		verify(productRepository, times(1)).save(any(Product.class));
		
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
		assertEquals("Phone",products.get(0).getName());
		assertEquals("Laptop",products.get(1).getName());
		verify(productRepository,times(1)).findAll();
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
		 assertEquals(1L,p.getId());
		 verify(productRepository, times(1)).findById(1L);
	}
}
