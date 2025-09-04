package org.product.management.domain.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.product.management.domain.entity.Product;

import java.util.List;
@ApplicationScoped
public class ProductDetailsRepository implements PanacheRepositoryBase<Product, Long> {

    public Uni<Product> findProductById(int id){
        return findById(Long.parseLong(String.valueOf(id)));
    }

    public Uni<List<Product>> findAllProducts(){
        return listAll();
    }

    public Uni<List<Product>> findAllProductByPriceAsc(){
        return listAll(Sort.ascending("price"));
    }

    public Uni<Integer> getProductQuantityById(Integer id) {
        return find("select quantity from Product where id = :id",
                Parameters.with("id", id))
                .project(Integer.class)
                .firstResult();
    }
}
