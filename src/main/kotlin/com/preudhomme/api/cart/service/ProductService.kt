package com.preudhomme.api.cart.service

import com.preudhomme.api.cart.entity.Product
import com.preudhomme.api.cart.entity.dto.CartProduct
import io.agroal.api.AgroalDataSource
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Default
import javax.inject.Inject

@ApplicationScoped
class ProductService {

    @Inject
    @field: Default
    private lateinit var defaultDataSource: AgroalDataSource

    fun getAllProducts(): Array<Product> {
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("select p.*, v.name AS tva_type from product p JOIN vat as v ON p.vat = v.id")
        val result: ResultSet = preStatement.executeQuery()
        var list = mutableListOf<Product>()
        while(result.next()) {
            list.add(Product(result.getObject("id") as UUID, result.getString("name"), result.getFloat("price"), result.getString("tva_type")))
        }
        return list.toTypedArray()
    }

    fun getProductById(productId: UUID) : Product? {
        val preStatement: PreparedStatement = defaultDataSource.connection.prepareStatement("select p.*, v.name AS tva_type from product p JOIN vat as v ON p.vat = v.id where p.id = ?")
        preStatement.setObject(1, productId)
        val result: ResultSet = preStatement.executeQuery()
        var product: Product? = null
        if (result.next()) {
            product = Product(result.getObject("id") as UUID, result.getString("name"), result.getFloat("price"), result.getString("tva_type"))
        }
        return product
    }
}