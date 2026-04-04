package com.example.ecommerce.controller;
import com.example.ecommerce.dto.ProductDTO;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/products")
public class ProductController 
{
	private final ProductService productService;
	
	public ProductController(ProductService productService)
	{
		this.productService = productService;
	}
	@PostMapping()
	public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO)
	{
		Product savedProduct=productService.createProduct(productDTO);
		return ResponseEntity.ok(savedProduct);
		
	}
	
	@GetMapping
	public List<Product> getAllProduct()
	{
		return productService.getAllProduct();
	}
	@GetMapping("/{id}")
	public Product getProductById(@PathVariable Long id)
	{
		 
		return productService.getProductbyId(id);
	}
	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO)
	{
		Product updateProduct = productService.updateProduct(id, productDTO);
		return ResponseEntity.ok(updateProduct);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id)
	{
		productService.deleteProduct(id);
		return ResponseEntity.ok("Product deleted successfully");
	}
}
