package com.example.ecommerce.controller;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.ProductService;
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
	@PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody Product product)
	{
		return ResponseEntity.status(201).body(productService.createProduct(product));
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
	public Product updateProduct(@PathVariable Long id,@RequestBody Product product)
	{
		return productService.updateProduct(id, product);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id)
	{
		productService.deleteProduct(id);
		return ResponseEntity.ok("Product deleted successfully");
	}
}
