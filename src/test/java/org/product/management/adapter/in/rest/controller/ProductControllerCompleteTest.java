package org.product.management.adapter.in.rest.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.product.management.adapter.in.rest.dto.ProductRequestResponseDto;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.core.Is.is;

@QuarkusTest
public class ProductControllerCompleteTest {
    private ProductRequestResponseDto createSampleProduct() {
        ProductRequestResponseDto dto = new ProductRequestResponseDto();
        dto.setName("Laptop");
        dto.setDescription("Dell Latitude 3900 with i5 Processor and 512 GB SSD.");
        dto.setPrice(75000.0);
        dto.setQuantity(10);
        return dto;
    }

    @Test
    void testSaveProduct() {
        ProductRequestResponseDto dto = createSampleProduct();
        given()
                .contentType("application/json")
                .body(dto)
                .when().post("/product/saveProduct")
                .then()
                .statusCode(201)
                .body("name", is("Laptop"));
    }

    @Test
    void testGetAllProducts() {
        given()
                .when().get("/product/allProducts")
                .then()
                .statusCode(200);
    }

    @Test
    void testGetProductById() {
        int productId = 1;
        given()
                .pathParam("id", productId)
                .when().get("/product/getDetails/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    void testUpdateProduct() {
        int productId = 1;
        ProductRequestResponseDto dto = createSampleProduct();
        dto.setPrice(80000.0);
        dto.setDescription("Dell Latitude 3900 with i5 Processor and 1 TB SSD.");
        dto.setName("Laptop");
        dto.setQuantity(15);

        given()
                .pathParam("id", productId)
                .contentType("application/json")
                .body(dto)
                .when().put("/product/update/{id}")
                .then()
                .statusCode(200)
                .body("quantity", is(15));
    }

    @Test
    void testDeleteProduct() {
        int productId = 1;

        given()
                .pathParam("id", productId)
                .when().delete("/product/delete/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    void testCheckAvailability() {
        int productId = 1;
        int count = 5;

        given()
                .queryParam("id", productId)
                .queryParam("count", count)
                .when().get("/product/check-availability")
                .then()
                .statusCode(200);
    }

    @Test
    void testGetProductsSortedByPrice() {
        given()
                .when().get("/product/sorted-by-price")
                .then()
                .statusCode(200);
    }
}
