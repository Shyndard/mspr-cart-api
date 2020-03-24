package com.preudhomme.api.test.e2e

import com.preudhomme.api.cart.service.ProductService
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.enterprise.inject.Default
import javax.inject.Inject

@QuarkusTest
open class ProductsTest {

    @Inject
    @field: Default
    lateinit var productService: ProductService

    @BeforeEach
    fun initEach() {
        productService.clear()
    }

    @Test
    fun testEmptyResponse() {
        given()
                .`when`()["/products"]
                .then()
                .statusCode(200)
                .body(`is`("[]"))
    }

    @Test
    fun testCreation() {
        val payload : String = "{\"name\":\"apple tv\",\"price\":590.99,\"vatType\":\"normal\"}"
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("products")
                .then()
                .statusCode(200)
                .body(containsString("apple tv"), containsString("590.99"), containsString("normal"))
    }

    @Test
    fun testCreationWithBadVat() {
        val payload : String = "{\"name\":\"apple tv\",\"price\":590.99,\"vatType\":\"bad\"}"
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("products")
                .then()
                .statusCode(204)
    }

    @Test
    fun testCreationWithIncompletePayload() {
        val payload : String = "{\"name\":\"apple tv\",\"price\":590.99}"
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("products")
                .then()
                .statusCode(400)
    }
}