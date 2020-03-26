package com.preudhomme.api.cart.entity.dto

import java.util.*

data class CartCreation(val userId: String, val products: Array<CartProductCreation>)