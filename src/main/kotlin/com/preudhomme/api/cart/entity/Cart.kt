package com.preudhomme.api.cart.entity

import java.util.Date

data class Cart(val id: String, val user: String, val createdAt: Date, val products: Array<CartProduct>)