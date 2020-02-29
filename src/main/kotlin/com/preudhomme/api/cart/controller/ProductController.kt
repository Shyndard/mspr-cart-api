package com.preudhomme.api.cart.controller;

import com.preudhomme.api.cart.entity.Product
import com.preudhomme.api.cart.service.ProductService
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/product")
@Tag(name = "product", description = "Product operations.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ProductController {

    @Inject
    @field: Default
    lateinit var productService: ProductService
    @GET
    fun getAll(): Array<Product> {
       return productService.getAllProducts()
    }

    @GET
    @Path("/{id}")
    fun getById(@PathParam("id") productId: String): Product?  {
        return productService.getProductById(productId)
    }
}