package org.product.management.adapter.in.rest.controller;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.product.management.adapter.in.rest.dto.AvailabilityResponse;
import org.product.management.adapter.in.rest.dto.ProductRequestResponseDto;
import org.product.management.application.service.ProductService;
import org.product.management.domain.entity.Product;

import java.util.List;

@Path("/product")
public class ProductController {

    @Inject
    ProductService productService;

    @POST
    @Path("/saveProduct")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> saveProduct(@Valid ProductRequestResponseDto entity) {
        // Call Service to Save the Entity
        Uni<Product> entitySaved = productService.saveProduct(entity);
        // Return 201 and Product Details onn save successful
        return entitySaved.onItem().transform(productEntity -> Response.status(Response.Status.CREATED).entity(productEntity).build());
    }

    @GET
    @Path("/allProducts")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllProductDetails(){
        // Call Service to Get Details of All Products
        Uni<List<Product>> allProduct = productService.getAllProduct();
        return allProduct.onItem().transform(products -> Response.ok(products).build());
    }

    @GET
    @Path("/getDetails/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getProductDetails(@PathParam("id") int id){
        // Call Service to Get Details of All Products
        Uni<Product> product = productService.getProductDetailsById(id);
        // If product exists, return 200 OK with updated data
        return product.onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
        // If product with given ID doesn't exist, return 404 Not Found
                .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateDetails(@PathParam("id") int id, ProductRequestResponseDto dto){
        // Call Service to update the Product Details
        Uni<Product> product = productService.updateProductDetails(id, dto);
        // If product exists and updated, return 200 OK with updated data
        return product.onItem().ifNotNull().transform(productEntity -> Response.ok(productEntity).build())
        // If product with given ID doesn't exist, return 404 Not Found
                .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/delete/{id}")
    public Uni<Response> deleteProduct(@PathParam("id") int id){
        // Call Service to delete Product Details
        Uni<Boolean> deleted = productService.deleteProductDetails(id);
        return deleted.onItem().transform(deletedResponse -> {
            // Product Found and Deleted
            if(deletedResponse){
                return Response.noContent().build();
            }
            // No Product is Found by Id
            else{
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        });
    }

    @GET
    @Path("/check-availability")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<AvailabilityResponse> checkAvailability(@QueryParam("id") int id, @QueryParam("count") int count) {
        // Call Service to Get Product Details
        Uni<AvailabilityResponse> available = productService.getCheckProductQuantity(id, count);
        return available.onItem().ifNull().failWith(new NotFoundException("Product not found with id: " + id));
    }

    @GET
    @Path("/sorted-by-price")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getProductSorted(){
        // Call Service to Get All Product Details sorted in ASC order by Price
        Uni<List<Product>> allSortedProduct = productService.getProductSortedByPrice();
        return allSortedProduct.onItem().transform(products -> Response.ok(products).build());
    }
}
