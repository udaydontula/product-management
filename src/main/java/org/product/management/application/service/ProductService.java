package org.product.management.application.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.product.management.adapter.in.rest.dto.AvailabilityResponse;
import org.product.management.adapter.in.rest.dto.ProductRequestResponseDto;
import org.product.management.adapter.mapper.ProductMapper;
import org.product.management.domain.entity.Product;
import org.product.management.domain.repository.ProductDetailsRepository;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ProductService {
    private static final Logger log = Logger.getLogger(ProductService.class);
    @Inject
    ProductDetailsRepository productRepository;

    @Transactional
    public Uni<Product> saveProduct(ProductRequestResponseDto entityDTO) {

        // Mapping the DTO class to Entity class
        Product entity = ProductMapper.INSTANCE.convertToProductEntity(entityDTO);
        // Product Details are saved in DB and returned
        return productRepository.persist(entity)
                // return entity after persisting
                .replaceWith(entity)
                .onFailure().invoke(e -> log.error("Failed to save product", e));

    }

    public Uni<List<Product>> getAllProduct() {
        // Get Details of All Product
        return productRepository.findAllProducts();
    }

    public Uni<Product> getProductDetailsById(int id) {
        // Get Product Details by ID
        return productRepository.findProductById(id);
    }

    @Transactional
    public Uni<Product> updateProductDetails(int id, ProductRequestResponseDto dto) {
        // Get Existing Product Details
        Uni<Product> entity = productRepository.findProductById(id);
        return entity.onItem().ifNotNull().transform(product -> {
                product.setName(dto.getName());
                product.setQuantity(dto.getQuantity());
                product.setPrice(dto.getPrice());
                product.setDescription(dto.getDescription());
                product.setUpdatedOn(LocalDateTime.now());
                return product;
            });
    }

    public Uni<Boolean> deleteProductDetails(int id) {
        // Delete the all Details of the Product
        return productRepository.deleteById((Long.parseLong(String.valueOf(id))));
    }

    public Uni<AvailabilityResponse> getCheckProductQuantity(int id, int count) {
        Uni<Integer> quantity = productRepository.getProductQuantityById(id);
        return quantity.onItem().ifNotNull().transform(productQuantity -> {
            boolean available = productQuantity >= count;
            return new AvailabilityResponse(id, count, available);
        });
    }

    public Uni<List<Product>> getProductSortedByPrice() {
        return productRepository.findAllProductByPriceAsc();
    }
}
