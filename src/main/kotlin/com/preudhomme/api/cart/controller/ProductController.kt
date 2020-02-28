package com.preudhomme.api.cart.controller;

import com.preudhomme.api.cart.entity.Product
import io.agroal.api.AgroalDataSource
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.sql.PreparedStatement
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/product")
@Tag(name = "product", description = "Product operations.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ProductController {

    @Inject
    private lateinit var dataSource: AgroalDataSource

    @GET
    fun getAll(): Array<Product> {
       return arrayOf()
    }

    @GET
    @Path("/{id}")
    fun getById(@PathParam("id") productId: String): Product?  {
        return null
    }
}