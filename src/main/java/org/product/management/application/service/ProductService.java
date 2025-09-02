package org.product.management.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.product.management.adapter.in.rest.dto.ProductRequestResponseDto;
import org.product.management.adapter.mapper.ProductMapper;
import org.product.management.domain.entity.Product;
import org.product.management.domain.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepository;
@Transactional
    public ProductRequestResponseDto saveProduct(ProductRequestResponseDto entityDTO) {
        Product entity = null;
        try {
            // Mapping the DTO class to Entity class
            entity = ProductMapper.INSTANCE.convertToProductEntity(entityDTO);
            // Product Details are saved in DB
            productRepository.save(entity);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ProductMapper.INSTANCE.convertToProductRequestResponseDTO(entity);
    }

    public List<Product> getAllProduct(){
        // Get Details of All Product
        List<Product> allProducts = productRepository.findAll();
        return allProducts;
    }

    public Optional<Product> getProductDetailsById(int id){
        // Get Product Details by ID
        Optional<Product> productDetail = productRepository.findById(id);
        return productDetail;
    }

    public Optional<Product> updateProductDetails(int id, ProductRequestResponseDto dto){
        // Get Existing Product Details
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            product.get().setName(dto.getName());
            product.get().setQuantity(dto.getQuantity());
            product.get().setPrice(dto.getPrice());
            product.get().setDescription(dto.getDescription());
            product.get().setUpdatedOn(LocalDateTime.now());
            // saving the updated Details
            productRepository.save(product.get());
        }
        return product;
    }

    public void deleteProductDetails(int id){
        // Delete the all Details of the Product
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()){
            productRepository.delete(product.get());
        }
    }

    public Optional<Product> getProductById(int id){
        return productRepository.findById(id);
    }

    public List<Product> getProductSortedByPrice(){
        return productRepository.findAllByOrderByPriceAsc();
    }
}
