package com.preudhomme.api.cart.entity.dto

import java.util.*

data class ProductCreation(val name: String, val price: Float, val vatType: String, val category: String, val description: String, val logoUrl: String)