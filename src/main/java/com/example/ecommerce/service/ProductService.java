package com.example.ecommerce.service;
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
	public Product createProduct(Product product)
	{
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
	public Product updateProduct(Long id,Product updateProduct)
	{
		Product product = getProductbyId(id);
		
		
		product.setName(updateProduct.getName());
		product.setDescription(updateProduct.getDescription());
		product.setPrice(updateProduct.getPrice());
		product.setStockQuantity(updateProduct.getStockQuantity());
		product.setImageUrl(updateProduct.getImageUrl());
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
 