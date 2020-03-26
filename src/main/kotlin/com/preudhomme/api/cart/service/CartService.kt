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
    @Default
    lateinit var defaultDataSource: AgroalDataSource

    fun getCartByUser(userId: String) : Cart? {
        val connection = defaultDataSource.connection
        val preStatement: PreparedStatement = connection.prepareStatement("select * from cart where user_id = ?")
        preStatement.setObject(1, userId)
        val result: ResultSet = preStatement.executeQuery()
        var cart: Cart? = null
        if (result.next()) {
            cart = Cart(result.getString("user_id") , result.getDate("created_at"), getProductOfUserCart(userId))
        }
        result.close()
        preStatement.close()
        connection.close()
        return cart
    }

    fun getProductOfUserCart(userId: String): Array<CartProduct> {
        val connection = defaultDataSource.connection
        val preStatement: PreparedStatement = connection.prepareStatement("select p.*, cp.amount as amount, v.name as vat_type, c.name as category_name from cart_product as cp JOIN product as p on p.id = cp.product_id JOIN vat AS v ON v.id = p.vat JOIN category as c ON c.id = p.category where user_id ILIKE ?")
        preStatement.setObject(1, userId)
        val result: ResultSet = preStatement.executeQuery()
        var list = mutableListOf<CartProduct>()
        while(result.next()) {
            list.add(CartProduct(result.getObject("id") as UUID, result.getString("name"), result.getFloat("price"), result.getInt("amount"), result.getString("vat_type"), result.getString("category_name")))
        }
        result.close()
        preStatement.close()
        connection.close()
        return list.toTypedArray()
    }

    fun createUserCart(cartCreation: CartCreation) : Cart? {
        if(getCartByUser(cartCreation.userId) == null) {
            val connection = defaultDataSource.connection
            val preStatement: PreparedStatement = connection.prepareStatement("INSERT INTO cart (user_id) VALUES (?)")
            preStatement.setObject(1, cartCreation.userId)
            preStatement.execute()
            preStatement.close()
            connection.close()
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
        val connection = defaultDataSource.connection
        val preStatement: PreparedStatement = connection.prepareStatement("INSERT INTO cart_product (user_id, product_id, amount) VALUES (?, (SELECT id FROM product WHERE id = ?), ?)")
        preStatement.setObject(1, userId)
        preStatement.setObject(2, product.id)
        preStatement.setInt(3, product.amount)
        try {
            preStatement.execute()
        } finally {
            preStatement.close()
            connection.close()
        }
    }

    fun updateProductsOfUserCart(userId: String, products: Array<CartProductCreation>): Array<CartProduct> {
        products.forEach { cartProduct -> updateProductOfUserCart(userId, cartProduct) }
        return getProductOfUserCart(userId)
    }

    private fun updateProductOfUserCart(userId: String, product: CartProductCreation) {
        val connection = defaultDataSource.connection
        val preStatement: PreparedStatement = connection.prepareStatement("UPDATE cart_product SET amount = ? WHERE user_id ILIKE ? AND product_id = ?")
        preStatement.setInt(1, product.amount)
        preStatement.setString(2, userId)
        preStatement.setObject(3, product.id)
        try {
            preStatement.execute()
        } finally {
            preStatement.close()
            connection.close()
        }
    }

    fun deleteProductsOfUserCart(userId: String, products: Array<UUID>): Array<CartProduct> {
        products.forEach { productId -> deleteProductOfUserCart(userId, productId) }
        return getProductOfUserCart(userId)
    }

    fun deleteProductOfUserCart(userId: String, productId: UUID) {
        val connection = defaultDataSource.connection
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("DELETE FROM cart_product WHERE user_id ILIKE ? AND product_id = ?")
        preStatement.setString(1, userId)
        preStatement.setObject(2, productId)
        try {
            preStatement.execute()
        } finally {
            preStatement.close()
            connection.close()
        }
    }

    fun clear() {
        var connection = defaultDataSource.connection;
        var statement = connection.prepareStatement("DELETE FROM cart_product")
        statement.execute()
        statement.close()

        statement = connection.prepareStatement("DELETE FROM cart")
        statement.execute()
        statement.close()

        connection.close()
    }

    fun totalUpdate(userId: String, cartProducts: Array<CartProductCreation>): Array<CartProduct> {
        val connection = defaultDataSource.connection
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("DELETE FROM cart_product WHERE user_id ILIKE ?")
        preStatement.setString(1, userId)
        try {
            preStatement.execute()
        } finally {
            preStatement.close()
            connection.close()
        }
        cartProducts.forEach { cartProduct -> addProductToUserCart(userId, cartProduct) }
        return getProductOfUserCart(userId)
    }
}