package org.product.management.adapter.in.rest.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.product.management.adapter.in.rest.dto.AvailabilityResponse;
import org.product.management.adapter.in.rest.dto.ProductRequestResponseDto;
import org.product.management.application.service.ProductService;
import org.product.management.domain.entity.Product;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class ProductControllerCompleteTest {

    @InjectMock
    ProductService productService;

    private ProductRequestResponseDto createSampleProductDto() {
        ProductRequestResponseDto dto = new ProductRequestResponseDto();
        dto.setName("Laptop");
        dto.setDescription("Dell Latitude 3900 with i5 Processor and 512 GB SSD.");
        dto.setPrice(75000.0);
        dto.setQuantity(10);
        return dto;
    }

    private Product createSampleProductEntity() {
        Product product = new Product();
        product.setId(1);
        product.setName("Laptop");
        product.setDescription("Dell Latitude 3900 with i5 Processor and 512 GB SSD.");
        product.setPrice(75000.0);
        product.setQuantity(10);
        return product;
    }

    @Test
    void testSaveProduct() {
        Product sample = createSampleProductEntity();
        Mockito.when(productService.saveProduct(Mockito.any(ProductRequestResponseDto.class)))
                .thenReturn(Uni.createFrom().item(sample));

        ProductRequestResponseDto dto = createSampleProductDto();

        given()
                .contentType("application/json")
                .body(dto)
                .when().post("/product/saveProduct")
                .then()
                .statusCode(201)
                .body("name", is("Laptop"))
                .body("id", notNullValue());
    }

    @Test
    void testGetAllProducts() {
        Product sample = createSampleProductEntity();
        Mockito.when(productService.getAllProduct())
                .thenReturn(Uni.createFrom().item(List.of(sample)));

        given()
                .when().get("/product/allProducts")
                .then()
                .statusCode(200)
                .body("[0].name", is("Laptop"));
    }

    @Test
    void testGetProductById_found() {
        Product sample = createSampleProductEntity();
        Mockito.when(productService.getProductDetailsById(1))
                .thenReturn(Uni.createFrom().item(sample));

        given()
                .pathParam("id", 1)
                .when().get("/product/getDetails/{id}")
                .then()
                .statusCode(200)
                .body("id", is(1));
    }

    @Test
    void testGetProductById_notFound() {
        Mockito.when(productService.getProductDetailsById(99))
                .thenReturn(Uni.createFrom().nullItem());

        given()
                .pathParam("id", 99)
                .when().get("/product/getDetails/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    void testUpdateProduct_found() {
        Product updated = createSampleProductEntity();
        updated.setQuantity(15);
        updated.setPrice(80000.0);
        Mockito.when(productService.updateProductDetails(Mockito.eq(1), Mockito.any()))
                .thenReturn(Uni.createFrom().item(updated));

        ProductRequestResponseDto dto = createSampleProductDto();
        dto.setQuantity(15);
        dto.setPrice(80000.0);

        given()
                .pathParam("id", 1)
                .contentType("application/json")
                .body(dto)
                .when().put("/product/update/{id}")
                .then()
                .statusCode(200)
                .body("quantity", is(15))
                .body("price", is(80000.0F));
    }

    @Test
    void testUpdateProduct_notFound() {
        Mockito.when(productService.updateProductDetails(Mockito.eq(99), Mockito.any()))
                .thenReturn(Uni.createFrom().nullItem());

        ProductRequestResponseDto dto = createSampleProductDto();

        given()
                .pathParam("id", 99)
                .contentType("application/json")
                .body(dto)
                .when().put("/product/update/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    void testCheckAvailability_found() {
        // Mock service to return a valid AvailabilityResponse
        AvailabilityResponse response = new AvailabilityResponse(1, 5, true);
        Mockito.when(productService.getCheckProductQuantity(1, 5))
                .thenReturn(Uni.createFrom().item(response));

        given()
                .queryParam("id", 1)
                .queryParam("count", 5)
                .when().get("/product/check-availability")
                .then()
                .statusCode(200)
                .body("requestedCount", is(5))
                .body("available", is(true));
    }

    @Test
    void testCheckAvailability_notFound() {
        Mockito.when(productService.getCheckProductQuantity(99, 5))
                .thenReturn(Uni.createFrom().nullItem());

        given()
                .queryParam("id", 99)
                .queryParam("count", 5)
                .when().get("/product/check-availability")
                .then()
                .statusCode(404);
    }

    @Test
    void testGetProductsSortedByPrice() {
        Product sample = createSampleProductEntity();
        Mockito.when(productService.getProductSortedByPrice())
                .thenReturn(Uni.createFrom().item(List.of(sample)));

        given()
                .when().get("/product/sorted-by-price")
                .then()
                .statusCode(200)
                .body("[0].name", is("Laptop"));
    }

    @Test
    void testDeleteProduct_found() {
        Mockito.when(productService.deleteProductDetails(1))
                .thenReturn(Uni.createFrom().item(true));

        given()
                .pathParam("id", 1)
                .when().delete("/product/delete/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    void testDeleteProduct_notFound() {
        Mockito.when(productService.deleteProductDetails(99))
                .thenReturn(Uni.createFrom().item(false));

        given()
                .pathParam("id", 99)
                .when().delete("/product/delete/{id}")
                .then()
                .statusCode(404);
    }
}
