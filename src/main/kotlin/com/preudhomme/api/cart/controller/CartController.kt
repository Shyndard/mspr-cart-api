package com.preudhomme.api.cart.controller;

import com.preudhomme.api.cart.entity.Cart
import com.preudhomme.api.cart.entity.Product
import java.util.*
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/cart/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class CartController {

    @GET
    @Path("/user/{userid}")
    fun get(@PathParam("userid") userid: String): Cart {
        return Cart(userid, userid, Date(), arrayOf());
    }
}