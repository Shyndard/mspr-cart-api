package com.preudhomme.api.test.e2e

import com.preudhomme.api.cart.entity.Product
import com.preudhomme.api.cart.entity.dto.ProductCreation
import com.preudhomme.api.cart.service.CartService
import com.preudhomme.api.cart.service.CategoryService
import com.preudhomme.api.cart.service.ProductService
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.*
import java.util.*
import javax.enterprise.inject.Default
import javax.inject.Inject


@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class CartsTest {

    @Inject
    @field: Default
    lateinit var cartService: CartService

    @Inject
    @field: Default
    lateinit var productService: ProductService

    @Inject
    @field: Default
    lateinit var categoryService: CategoryService

    lateinit var userId: UUID

    @BeforeAll
    fun init() {
        categoryService.clear()
        categoryService.create("none")
    }

    @BeforeEach
    fun initEach() {
        userId = UUID.randomUUID()
        cartService.clear()
        productService.clear()
    }

    @Test
    fun testCreation() {
        val payload : String = "{\"products\":[],\"userId\":\"$userId\"}"
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("carts")
                .then()
                .statusCode(200)
                .body(CoreMatchers.containsString("userId"), CoreMatchers.containsString(userId.toString()))
    }

    @Test
    fun testCreationAndGet() {
        val payload : String = "{\"products\":[],\"userId\":\"$userId\"}"
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("carts")

        given()
                .get("carts/user/$userId")
                .then()
                .statusCode(200)
                .body(CoreMatchers.containsString("userId"), CoreMatchers.containsString(userId.toString()))
    }

    @Test
    fun testCreateCartAndAddProduct() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        val product = productService.create(ProductCreation("item", 100.99f, "normal", "none", "super description", "a logo url"))?: fail("Product creation failed")
        val postCartPayload : String = "{\"products\":[],\"userId\":\"$userId\"}"
        given()
                .contentType(ContentType.JSON)
                .body(postCartPayload)
                .post("carts")

        val postPayload : String = "[{\"amount\": 5,\"id\": \"${product.id}\"}]"
        given()
                .contentType(ContentType.JSON)
                .body(postPayload)
                .post("/carts/user/$userId/products")
                .then()
                .statusCode(200)
                .body(CoreMatchers.containsString("\"amount\":5"), CoreMatchers.containsString("\"id\":\"${product.id.toString()}\""))
    }

    @Test
    fun testCreateCartAndAddThenUpdateProduct() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        val product = productService.create(ProductCreation("item", 100.99f, "normal", "none", "super description", "a logo url"))?: fail("Product creation failed")
        val postCartPayload : String = "{\"products\":[{\"amount\": 5,\"id\": \"${product.id}\"}],\"userId\":\"$userId\"}"
        given()
                .contentType(ContentType.JSON)
                .body(postCartPayload)
                .post("carts")

        val postPayload : String = "[{\"amount\": 18,\"id\": \"${product.id}\"}]"
        given()
                .contentType(ContentType.JSON)
                .body(postPayload)
                .put("/carts/user/$userId/products")
                .then()
                .statusCode(200)
                .body(CoreMatchers.containsString("\"amount\":18"), CoreMatchers.containsString("\"id\":\"${product.id.toString()}\""))
    }
}