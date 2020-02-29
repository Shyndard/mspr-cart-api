package com.preudhomme.api.cart.service

import com.preudhomme.api.cart.entity.Product
import com.preudhomme.api.cart.entity.dto.CartProduct
import io.agroal.api.AgroalDataSource
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Default
import javax.inject.Inject

@ApplicationScoped
class ProductService {

    @Inject
    @field: Default
    private lateinit var defaultDataSource: AgroalDataSource

    fun getAllProducts(): Array<Product> {
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("select * from product")
        val result: ResultSet = preStatement.executeQuery()
        var list = mutableListOf<Product>()
        while(result.next()) {
            list.add(Product(result.getString("id"), result.getString("name"), result.getFloat("price"), result.getFloat("tva")))
        }
        return list.toTypedArray()
    }

    fun getProductById(productId: String) : Product? {
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("select * from product where id = ?::UUID")
        preStatement.setObject(1, productId)
        val result: ResultSet = preStatement.executeQuery()
        var product: Product? = null
        if (result.next()) {
            product = Product(result.getString("id"), result.getString("name"), result.getFloat("price"), result.getFloat("tva"))
        }
        return product
    }
}