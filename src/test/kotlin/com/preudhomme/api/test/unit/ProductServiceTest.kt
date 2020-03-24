package com.preudhomme.api.test.unit

import com.preudhomme.api.cart.entity.dto.ProductCreation
import com.preudhomme.api.cart.service.ProductService
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import javax.enterprise.inject.Default
import javax.inject.Inject


@QuarkusTest
open class ProductServiceTest {

    @Inject
    @field: Default
    lateinit var productService: ProductService

    @BeforeEach
    fun initEach() {
        productService.clear()
    }

    @Test
    fun testGetAllEmpty() {
        assertTrue(productService.getAllProducts().isEmpty())
    }

    @Test
    fun testGetAll() {
        productService.create(ProductCreation("item", 100f, "normal"))
        assertTrue(productService.getAllProducts().size == 1)
    }

    @Test
    fun testGetById() {
        val product = productService.create(ProductCreation("item", 100f, "normal")) ?: fail("Created product is null")
        val foundProduct = productService.getProductById(product.id) ?: fail("Product not found")
        assertEquals(product, foundProduct)
    }

    @Test
    fun testCreate() {
        assertNotNull(productService.create(ProductCreation("item", 100f, "normal")))
    }

    @Test
    fun testCreateBadVat() {
        assertNull(productService.create(ProductCreation("item", 100f, "random")))
    }
}