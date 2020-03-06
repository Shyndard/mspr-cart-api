package com.preudhomme.api.cart.entity

import com.preudhomme.api.cart.entity.dto.CartProduct
import java.util.*

data class Cart(val userId: UUID, val createdAt: Date, val products: Array<CartProduct>)