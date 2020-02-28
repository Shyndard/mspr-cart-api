package com.preudhomme.api.cart.service

import com.preudhomme.api.cart.entity.Cart
import com.preudhomme.api.cart.entity.dto.CartCreation
import com.preudhomme.api.cart.entity.dto.CartProduct
import com.preudhomme.api.cart.entity.dto.CartProductCreation
import io.agroal.api.AgroalDataSource
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.inject.Singleton

@ApplicationScoped
class CartService {

    @Inject
    private lateinit var defaultDataSource: AgroalDataSource

    fun getCartByUser(userId: String) : Cart? {
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("select * from cart where user_id = ?::UUID")
        preStatement.setObject(1, userId)
        val result: ResultSet = preStatement.executeQuery()
        var cart: Cart? = null
        if (result.next()) {
            cart = Cart(result.getString("user_id"), result.getDate("created_at"), getProductOfUserCart(userId))
        }
        return cart
    }

    fun getProductOfUserCart(userId: String): Array<CartProduct> {
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("select * from cart_product JOIN product on id = product_id where user_id = ?::UUID")
        preStatement.setObject(1, userId)
        val result: ResultSet = preStatement.executeQuery()
        var list = mutableListOf<CartProduct>()
        while(result.next()) {
            list.add(CartProduct(result.getString("id"), result.getString("name"), result.getFloat("price"), result.getInt("amount")))
        }
        return list.toTypedArray()
    }

    fun createUserCart(cartCreation: CartCreation) : Cart? {
        if(getCartByUser(cartCreation.userId) == null) {
            val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("INSERT INTO cart (user_id) VALUES (?::UUID)")
            preStatement.setString(1, cartCreation.userId)
            preStatement.execute()
            addProductsToUserCart(cartCreation.userId, cartCreation.products)
            return getCartByUser(cartCreation.userId)
        }
        return null
    }

    fun addProductsToUserCart(userId: String, products: Array<CartProductCreation>): Array<CartProduct> {
        products.forEach { cartProduct -> addProductToUserCart(userId, cartProduct) }
        return getProductOfUserCart(userId)
    }

    private fun addProductToUserCart(userId: String, product: CartProductCreation) {
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("INSERT INTO cart_product (user_id, product_id, amount) VALUES (?::UUID, (SELECT id FROM product WHERE id = ?::UUID), ?)")
        preStatement.setString(1, userId)
        preStatement.setString(2, product.id)
        preStatement.setInt(3, product.amount)
        try {
            preStatement.execute()
        } catch (ex: Exception) {

        }
    }

    fun updateProductsOfUserCart(userId: String, products: Array<CartProductCreation>): Array<CartProduct> {
        products.forEach { cartProduct -> updateProductsOfUserCart(userId, cartProduct) }
        return getProductOfUserCart(userId)
    }

    private fun updateProductsOfUserCart(userId: String, product: CartProductCreation) {
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("UPDATE cart_product SET amount = ? WHERE user_id = ? AND product_id = ?")
        preStatement.setInt(1, product.amount)
        preStatement.setString(2, userId)
        preStatement.setString(3, product.id)
        try {
            preStatement.execute()
        } catch (ex: Exception) {

        }
    }

    fun deleteProductsOfUserCart(userId: String, products: Array<String>): Array<CartProduct> {
        products.forEach { productId -> deleteProductOfUserCart(userId, productId) }
        return getProductOfUserCart(userId)
    }

    fun deleteProductOfUserCart(userId: String, productId: String) {
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("DELETE FROM cart_product WHERE user_id = ? AND product_id = ?")
        preStatement.setString(1, userId)
        preStatement.setString(2, productId)
        try {
            preStatement.execute()
        } catch (ex: Exception) {

        }
    }
}