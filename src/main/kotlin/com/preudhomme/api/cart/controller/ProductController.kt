package com.preudhomme.api.cart.controller;

import com.preudhomme.api.cart.entity.Product
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ProductController {

    @GET
    fun getAll(): Array<Product> {
        return arrayOf()
    }
    @GET
    @Path("/{id}")
    fun getById(@PathParam("id") id: String): Product? {
        return Product(id, "salut", 10.5f)
    }
}