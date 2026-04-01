package com.example.ecommerce.controller;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api/admin/products")
public class AdminController 
{
	private final ProductRepository productRepository;
	
	public AdminController(ProductRepository productRepository)
	{
		this.productRepository = productRepository;
	}
	@PostMapping
	public Product addProduct(@RequestBody Product product)
	{
		return productRepository.save(product);
	}
	@PutMapping("/{id}")
	public Product updateProduct(@PathVariable Long id, @RequestBody Product product)
	{
		product.setId(id);
		return productRepository.save(product);
	}
	@DeleteMapping("/{id}")
	public String deleteProduct(@PathVariable Long id)
	{
		productRepository.deleteById(id);
		return "Product deleted Successfully";
	}
	@GetMapping
	public List<Product> getAllProducts()
	{
		return productRepository.findAll();
	}
}
