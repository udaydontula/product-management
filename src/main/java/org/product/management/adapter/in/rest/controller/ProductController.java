package org.product.management.adapter.in.rest.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.product.management.adapter.in.rest.dto.AvailabilityResponse;
import org.product.management.adapter.in.rest.dto.ProductRequestResponseDto;
import org.product.management.application.service.ProductService;
import org.product.management.domain.entity.Product;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Path("/product")
public class ProductController {

    @Inject
    ProductService productService;

    @POST
    @Path("/saveProduct")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveProduct(@Valid ProductRequestResponseDto entity){
        // Call Service to Save the Entity
        entity = productService.saveProduct(entity);
        // Return 201 and Product Details onn save successful
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @GET
    @Path("/allProducts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProductDetails(){
        // Call Service to Get Details of All Products
        List<Product> allProduct = productService.getAllProduct();
        if(!allProduct.isEmpty()){
            return Response.ok(allProduct).build();
        }else{
            return Response.ok(Collections.emptyList()).build();
        }
    }

    @GET
    @Path("/getDetails/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductDetails(@PathParam("id") int id){
        // Call Service to Get Details of All Products
        Optional<Product> product = productService.getProductDetailsById(id);
        // If product exists, return 200 OK with updated data
        if(product.isPresent()){
            return Response.ok(product).build();
        }
        // If product with given ID doesn't exist, return 404 Not Found
        else{
            return Response.status(Response.Status.NOT_FOUND).entity("Product with ID:"+id+" is not Found.").build();
        }
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDetails(@PathParam("id") int id, ProductRequestResponseDto dto){
        // Call Service to update the Product Details
        Optional<Product> product = productService.updateProductDetails(id, dto);
        // If product exists and updated, return 200 OK with updated data
        if(product.isPresent()){
            return Response.ok(product).build();
        }
        // If product with given ID doesn't exist, return 404 Not Found
        else{
            return Response.status(Response.Status.NOT_FOUND).entity("Product with ID:"+id+" is not Found.").build();
        }
    }

    @DELETE
    @Path("/delete/{id}")
    public Response deleteProduct(@PathParam("id") int id){
        // Call Service to delete Product Details
        productService.deleteProductDetails(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/check-availability")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAvailability(@QueryParam("id")int id, @QueryParam("count")int count){
        // Call Service to Get Product Details
        Optional<Product> product = productService.getProductById(id);
        if(product.isPresent()){
            // Check the Quantity is Available
            boolean available = product.get().getQuantity() >= count;
            AvailabilityResponse response = new AvailabilityResponse(id, count, available);
            return Response.ok().entity(response).build();
        }else{
            // Response if No Product is Found
            return Response.status(Response.Status.NOT_FOUND).entity("Product not found with id: " + id).build();
        }
    }

    @GET
    @Path("/sorted-by-price")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductSorted(){
        // Call Service to Get All Product Details sorted in ASC order by Price
        List<Product> allSortedProduct = productService.getProductSortedByPrice();
        return Response.ok().entity(allSortedProduct).build();
    }
}
