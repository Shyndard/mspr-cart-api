package com.preudhomme.api.cart.entity

import java.util.*

data class Product(val id: UUID, val name: String, val price: Float, val vatType: String)