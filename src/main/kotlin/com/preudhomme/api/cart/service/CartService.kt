package com.preudhomme.api.cart.service

import com.preudhomme.api.cart.entity.Cart
import com.preudhomme.api.cart.entity.dto.CartCreation
import com.preudhomme.api.cart.entity.dto.CartProduct
import com.preudhomme.api.cart.entity.dto.CartProductCreation
import io.agroal.api.AgroalDataSource
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Default
import javax.inject.Inject

@ApplicationScoped
class CartService {

    @Inject
    @field: Default
    private lateinit var defaultDataSource: AgroalDataSource

    fun getCartByUser(userId: UUID) : Cart? {
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("select * from cart where user_id = ?::UUID")
        preStatement.setObject(1, userId)
        val result: ResultSet = preStatement.executeQuery()
        var cart: Cart? = null
        if (result.next()) {
            cart = Cart(result.getObject("user_id") as UUID, result.getDate("created_at"), getProductOfUserCart(userId))
        }
        return cart
    }

    fun getProductOfUserCart(userId: UUID): Array<CartProduct> {
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("select * from cart_product JOIN product on id = product_id where user_id = ?::UUID")
        preStatement.setObject(1, userId)
        val result: ResultSet = preStatement.executeQuery()
        var list = mutableListOf<CartProduct>()
        while(result.next()) {
            list.add(CartProduct(result.getObject("id") as UUID, result.getString("name"), result.getFloat("price"), result.getInt("amount")))
        }
        return list.toTypedArray()
    }

    fun createUserCart(cartCreation: CartCreation) : Cart? {
        if(getCartByUser(cartCreation.userId) == null) {
            val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("INSERT INTO cart (user_id) VALUES (?::UUID)")
            preStatement.setObject(1, cartCreation.userId)
            preStatement.execute()
            addProductsToUserCart(cartCreation.userId, cartCreation.products)
            return getCartByUser(cartCreation.userId)
        }
        return null
    }

    fun addProductsToUserCart(userId: UUID, products: Array<CartProductCreation>): Array<CartProduct> {
        products.forEach { cartProduct -> addProductToUserCart(userId, cartProduct) }
        return getProductOfUserCart(userId)
    }

    private fun addProductToUserCart(userId: UUID, product: CartProductCreation) {
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("INSERT INTO cart_product (user_id, product_id, amount) VALUES (?, (SELECT id FROM product WHERE id = ?::UUID), ?)")
        preStatement.setObject(1, userId)
        preStatement.setObject(2, product.id)
        preStatement.setInt(3, product.amount)
        try {
            preStatement.execute()
        } catch (ex: Exception) {

        }
    }

    fun updateProductsOfUserCart(userId: UUID, products: Array<CartProductCreation>): Array<CartProduct> {
        products.forEach { cartProduct -> updateProductsOfUserCart(userId, cartProduct) }
        return getProductOfUserCart(userId)
    }

    private fun updateProductsOfUserCart(userId: UUID, product: CartProductCreation) {
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("UPDATE cart_product SET amount = ? WHERE user_id = ? AND product_id = ?")
        preStatement.setInt(1, product.amount)
        preStatement.setObject(2, userId)
        preStatement.setObject(3, product.id)
        try {
            preStatement.execute()
        } catch (ex: Exception) {

        }
    }

    fun deleteProductsOfUserCart(userId: UUID, products: Array<UUID>): Array<CartProduct> {
        products.forEach { productId -> deleteProductOfUserCart(userId, productId) }
        return getProductOfUserCart(userId)
    }

    fun deleteProductOfUserCart(userId: UUID, productId: UUID) {
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("DELETE FROM cart_product WHERE user_id = ? AND product_id = ?")
        preStatement.setObject(1, userId)
        preStatement.setObject(2, productId)
        try {
            preStatement.execute()
        } catch (ex: Exception) {

        }
    }
}