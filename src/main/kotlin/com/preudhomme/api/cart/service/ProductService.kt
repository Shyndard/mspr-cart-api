package com.preudhomme.api.cart.service

import com.preudhomme.api.cart.entity.Product
import com.preudhomme.api.cart.entity.dto.ProductCreation
import io.agroal.api.AgroalDataSource
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Default
import javax.inject.Inject

@ApplicationScoped
class ProductService {

    @Inject
    @Default
    lateinit var defaultDataSource: AgroalDataSource

    fun getAllProducts(): Array<Product> {
        val connection: Connection = defaultDataSource.connection
        val preStatement: PreparedStatement = connection.prepareStatement("select p.*, v.name AS tva_type from product p JOIN vat as v ON p.vat = v.id")
        val result: ResultSet = preStatement.executeQuery()
        var list = mutableListOf<Product>()
        while(result.next()) {
            list.add(Product(result.getObject("id") as UUID, result.getString("name"), result.getFloat("price"), result.getString("tva_type")))
        }
        result.close()
        preStatement.close()
        connection.close()
        return list.toTypedArray()
    }

    fun getProductById(productId: UUID) : Product? {
        val connection: Connection = defaultDataSource.connection
        val preStatement: PreparedStatement = connection.prepareStatement("select p.*, v.name AS tva_type from product p JOIN vat as v ON p.vat = v.id where p.id = ?")

        preStatement.setObject(1, productId)
        val result: ResultSet = preStatement.executeQuery()
        var product: Product? = null
        if (result.next()) {
            product = Product(result.getObject("id") as UUID, result.getString("name"), result.getFloat("price"), result.getString("tva_type"))
        }

        result.close()
        preStatement.close()
        connection.close()
        return product
    }

    fun create(product: ProductCreation): Product? {
        val connection: Connection = defaultDataSource.connection
        val preStatement: PreparedStatement = connection.prepareStatement("INSERT INTO product (name, price, vat) VALUES (?, ?, (SELECT id FROM vat WHERE name ILIKE ?))", Statement.RETURN_GENERATED_KEYS)
        preStatement.setString(1, product.name)
        preStatement.setFloat(2, product.price)
        preStatement.setString(3, product.vatType)
        var resultProduct: Product? = null
        try {
            preStatement.executeUpdate()
            val keys: ResultSet = preStatement.generatedKeys
            if (keys.next()) {
                resultProduct = getProductById(keys.getObject(1) as UUID)
            }
            keys.close()
        } finally {
            preStatement.close()
            connection.close()
        }
        return resultProduct
    }

    fun clear() {
        var connection = defaultDataSource.connection;
        var statement = connection.prepareStatement("DELETE FROM product");
        statement.executeUpdate()
        statement.close()
        connection.close()
    }
}