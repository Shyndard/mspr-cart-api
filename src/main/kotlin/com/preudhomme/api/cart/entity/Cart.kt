package com.preudhomme.api.cart.entity

import com.preudhomme.api.cart.entity.dto.CartProduct
import java.util.Date

data class Cart(val userId: String, val createdAt: Date, val products: Array<CartProduct>)