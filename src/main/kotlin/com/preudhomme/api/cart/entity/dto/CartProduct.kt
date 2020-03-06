package com.preudhomme.api.cart.entity.dto

import java.util.*

data class CartProduct(val id: UUID, val name: String, val price: Float, val amount: Int, val vatType: String)
