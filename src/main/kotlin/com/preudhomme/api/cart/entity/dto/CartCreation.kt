package com.preudhomme.api.cart.entity.dto

data class CartCreation(val userId: String, val products: Array<CartProductCreation>)