package com.preudhomme.api.test.unit

import com.preudhomme.api.cart.entity.dto.ProductCreation
import com.preudhomme.api.cart.service.CategoryService
import com.preudhomme.api.cart.service.ProductService
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import javax.enterprise.inject.Default
import javax.inject.Inject


@QuarkusTest
open class CategoryServiceTest {

    @Inject
    @field: Default
    lateinit var categoryService: CategoryService

    @BeforeEach
    fun initEach() {
        categoryService.clear()
    }

    @Test
    fun testGetAllEmpty() {
        assertTrue(categoryService.getAll().isEmpty())
    }

    @Test
    fun testGetAll() {
        categoryService.create("some-category")
        assertTrue(categoryService.getAll().size == 1)
    }

    @Test
    fun testGetById() {
        val category = categoryService.create("some-category") ?: fail("Created category is null")
        val foundCategory = categoryService.getById(category.id) ?: fail("Category not found")
        assertEquals(category, foundCategory)
    }

    @Test
    fun testCreate() {
        assertNotNull(categoryService.create("some-category"))
    }
}