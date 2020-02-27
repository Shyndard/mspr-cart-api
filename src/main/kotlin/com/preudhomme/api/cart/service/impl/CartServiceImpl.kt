package com.preudhomme.api.cart.service.impl

import com.preudhomme.api.cart.entity.Cart
import io.agroal.api.AgroalDataSource
import java.util.*
import javax.inject.Inject

class CartServiceImpl {

    fun loadUserCart(userId: String): Cart? {
        return Cart(userId, userId, Date(), arrayOf())
    }
}