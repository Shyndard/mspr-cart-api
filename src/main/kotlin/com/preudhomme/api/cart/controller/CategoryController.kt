package com.preudhomme.api.cart.controller;

import com.preudhomme.api.cart.entity.Cart
import com.preudhomme.api.cart.entity.Category
import com.preudhomme.api.cart.entity.dto.CartCreation
import com.preudhomme.api.cart.entity.dto.CartProduct
import com.preudhomme.api.cart.entity.dto.CartProductCreation
import com.preudhomme.api.cart.service.CartService
import com.preudhomme.api.cart.service.CategoryService
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.util.*
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/categories/")
@Tag(name = "category", description = "Category operations.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class CategoryController {

    @Inject
    @field: Default
    lateinit var categoryService: CategoryService

    @GET
    fun getAll(): Array<Category> {
        return categoryService.getAll()
    }

    @GET
    @Path("/{categoryId}")
    fun getById(@PathParam("categoryId") categoryId: UUID): Category? {
        return categoryService.getById(categoryId)
    }

    @POST
    fun create(@RequestBody() name: String): Category? {
            return categoryService.create(name)
    }
}