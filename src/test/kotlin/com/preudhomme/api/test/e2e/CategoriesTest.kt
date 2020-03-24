package com.preudhomme.api.test.e2e

import com.preudhomme.api.cart.service.CategoryService
import com.preudhomme.api.cart.service.ProductService
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.*
import java.util.*
import javax.enterprise.inject.Default
import javax.inject.Inject

@QuarkusTest
open class CategoriesTest {

    @Inject
    @field: Default
    lateinit var categoryService: CategoryService

    @BeforeEach
    fun initEach() {
        categoryService.clear()
    }

    @Test
    fun testGetAllEmpty() {
        given()
                .`when`()["/categories"]
                .then()
                .statusCode(200)
                .body(`is`("[]"))
    }

    @Test
    fun testGetAll() {
        categoryService.create("none")?: fail("Category creation failed")
        given()
                .`when`()["/categories"]
                .then()
                .statusCode(200)
                .body(containsString("\"name\":\"none\""))
    }

    @Test
    fun testCreation() {
        val payload : String = "category"
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("categories")
                .then()
                .statusCode(200)
                .body(containsString("\"name\":\"category\""))
    }
}