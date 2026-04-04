package com.example.ecommerce.service;
import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.repository.ProductRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService 
{	
	private final ProductRepository productRepository;
	public ProductService(ProductRepository productRepository)
	{
		this.productRepository = productRepository;
	}
	public Product createProduct(ProductDTO productDTO)
	{
		Product product=new Product();
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());;
		product.setPrice(productDTO.getPrice());
		product.setStockQuantity(productDTO.getStockQuantity());;
		product.setImageUrl(productDTO.getImageUrl());
		return productRepository.save(product);
		
	}
	public List<Product> getAllProduct()
	{
		return productRepository.findAll();
	}
	public Product getProductbyId(Long id)
	{
		return productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("product not found with id: " + id));
	}
	public Product updateProduct(Long id,ProductDTO productDTO)
	{
		Product product = getProductbyId(id);
		
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
		product.setStockQuantity(productDTO.getStockQuantity());
		product.setImageUrl(productDTO.getImageUrl());
		
		return productRepository.save(product);
	}
	public void deleteProduct(Long id)
	{
		if(!productRepository.existsById(id))
		{
			throw new ProductNotFoundException("Product not found with id: " + id);
		}
		productRepository.deleteById(id);
	}
}
 