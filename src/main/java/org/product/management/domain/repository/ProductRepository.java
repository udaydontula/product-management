package org.product.management.domain.repository;

import org.product.management.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findById(int id);

    List<Product> findAll();

    List<Product> findAllByOrderByPriceAsc();
}
