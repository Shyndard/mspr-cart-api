package com.preudhomme.api.cart.controller

import com.preudhomme.api.cart.entity.Product
import com.preudhomme.api.cart.service.ProductService
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.util.*
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/products")
@Tag(name = "product", description = "Product operations.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ProductsController {

    @Inject
    @field: Default
    lateinit var productService: ProductService

    @GET
    fun getAll(): Array<Product> {
        return productService.getAllProducts()
    }

    @GET
    @Path("/{id}")
    fun getById(@PathParam("id") productId: UUID): Product?  {
        return productService.getProductById(productId)
    }

    @POST
    fun create(): Response  {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build()
    }

    @PUT
    fun update(): Response {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build()
    }

    @DELETE
    fun delete(): Response {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build()
    }
}