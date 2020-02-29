package com.preudhomme.api.cart.controller;

import com.preudhomme.api.cart.entity.Cart
import com.preudhomme.api.cart.entity.dto.CartCreation
import com.preudhomme.api.cart.entity.dto.CartProduct
import com.preudhomme.api.cart.entity.dto.CartProductCreation
import com.preudhomme.api.cart.service.CartService
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/cart/")
@Tag(name = "cart", description = "Cart operations.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class CartController {

    @Inject
    @field: Default
    lateinit var cartService: CartService

    @GET
    @Path("/user/{userId}")
    @APIResponse(responseCode = "200", description = "Getting user cart")
    fun getByUser(@PathParam("userId") userId: String): Cart? {
        return cartService.getCartByUser(userId)
    }

    @POST
    @APIResponse(responseCode = "201", description = "Creating user cart")
    fun create(@RequestBody() cartCreation: CartCreation): Cart? {
        return cartService.createUserCart(cartCreation)
    }

    @POST
    @APIResponse(responseCode = "201", description = "Creating user cart product")
    @Path("/user/{userId}/products")
    fun add(@PathParam("userId") userId: String, @RequestBody cartProducts: Array<CartProductCreation>): Array<CartProduct> {
        return cartService.addProductsToUserCart(userId, cartProducts);
    }

    @PUT
    @APIResponse(responseCode = "200", description = "Updating user cart product")
    @Path("/user/{userId}/products")
    fun update(@PathParam("userId") userId: String, @RequestBody cartProducts: Array<CartProductCreation>): Array<CartProduct> {
        return cartService.updateProductsOfUserCart(userId, cartProducts);
    }

    @DELETE
    @APIResponse(responseCode = "200", description = "Deleting user cart product")
    @Path("/user/{userId}/products")
    fun delete(@PathParam("userId") userId: String, @RequestBody cartProducts: Array<String>): Array<CartProduct> {
        return cartService.deleteProductsOfUserCart(userId, cartProducts);
    }
}