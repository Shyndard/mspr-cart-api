package com.preudhomme.api.test.unit

import com.preudhomme.api.cart.entity.Product
import com.preudhomme.api.cart.entity.dto.CartCreation
import com.preudhomme.api.cart.entity.dto.CartProductCreation
import com.preudhomme.api.cart.entity.dto.ProductCreation
import com.preudhomme.api.cart.service.CartService
import com.preudhomme.api.cart.service.ProductService
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.*
import javax.enterprise.inject.Default
import javax.inject.Inject


@QuarkusTest
open class CartServiceTest {

    @Inject
    @field: Default
    lateinit var cartService: CartService

    @Inject
    @field: Default
    lateinit var productService: ProductService

    @BeforeEach
    fun initEach() {
        cartService.clear()
        productService.clear()
    }

    @Test
    fun testCreateCartWithNoProduct() {
        val userId = UUID.randomUUID()
        val cart = cartService.createUserCart(CartCreation(userId, arrayOf()))?: fail("User cart creation failed")
        assertTrue(userId == cart.userId && cart.products.isEmpty())
    }

    @Test
    fun testCreateCartWithProducts() {
        val userId = UUID.randomUUID()
        val product = productService.create(ProductCreation("item", 100f, "normal"))?: fail("Product creation failed")
        val cart = cartService.createUserCart(CartCreation(userId, arrayOf(CartProductCreation(product.id, 10))))?: fail("User cart creation failed")
        assertTrue(userId == cart.userId && cart.products.size == 1 && cart.products[0].id == product.id && cart.products[0].amount == 10)
    }

    @Test
    fun testGetUserCartProducts() {
        val userId = UUID.randomUUID()
        val product = productService.create(ProductCreation("item", 100f, "normal"))?: fail("Product creation failed")
        cartService.createUserCart(CartCreation(userId, arrayOf(CartProductCreation(product.id, 10))))?: fail("User cart creation failed")
        val userCartProducts = cartService.getProductOfUserCart(userId)
        assertTrue(userCartProducts.size == 1 && userCartProducts[0].id == product.id && userCartProducts[0].amount == 10)
    }

    @Test
    fun testUpdateUserCartProducts() {
        val userId = UUID.randomUUID()
        val product = productService.create(ProductCreation("item", 100f, "normal"))?: fail("Product creation failed")
        cartService.createUserCart(CartCreation(userId, arrayOf(CartProductCreation(product.id, 10))))?: fail("User cart creation failed")
        val userCartProducts = cartService.updateProductsOfUserCart(userId, arrayOf(CartProductCreation(product.id, 50)))
        assertTrue(userCartProducts.size == 1 && userCartProducts[0].id == product.id && userCartProducts[0].amount == 50)
    }

    @Test
    fun testDeleteUserCartProduct() {
        val userId = UUID.randomUUID()
        val product = productService.create(ProductCreation("item", 100f, "normal"))?: fail("Product creation failed")
        cartService.createUserCart(CartCreation(userId, arrayOf(CartProductCreation(product.id, 10))))?: fail("User cart creation failed")
        val userCartProducts = cartService.deleteProductsOfUserCart(userId, arrayOf(product.id))
        assertTrue(userCartProducts.isEmpty())
    }
}