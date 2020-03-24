package com.preudhomme.api.cart.service

import com.preudhomme.api.cart.entity.Category
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
class CategoryService {

    @Inject
    @Default
    lateinit var defaultDataSource: AgroalDataSource

    fun getAll(): Array<Category> {
        val connection: Connection = defaultDataSource.connection
        val preStatement: PreparedStatement = connection.prepareStatement("select * FROM category")
        val result: ResultSet = preStatement.executeQuery()
        var list = mutableListOf<Category>()
        while(result.next()) {
            list.add(Category(result.getObject("id") as UUID, result.getString("name")))
        }
        result.close()
        preStatement.close()
        connection.close()
        return list.toTypedArray()
    }

    fun getById(categoryId: UUID) : Category? {
        val connection: Connection = defaultDataSource.connection
        val preStatement: PreparedStatement = connection.prepareStatement("select * FROM category WHERE id = ?")

        preStatement.setObject(1, categoryId)
        val result: ResultSet = preStatement.executeQuery()
        var category: Category? = null
        if (result.next()) {
            category = Category(result.getObject("id") as UUID, result.getString("name"))
        }

        result.close()
        preStatement.close()
        connection.close()
        return category
    }

    fun create(name: String): Category? {
        val connection: Connection = defaultDataSource.connection
        val preStatement: PreparedStatement = connection.prepareStatement("INSERT INTO category (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)
        preStatement.setString(1, name)
        var resultCategory: Category? = null
        try {
            preStatement.executeUpdate()
            val keys: ResultSet = preStatement.generatedKeys
            if (keys.next()) {
                resultCategory = getById(keys.getObject(1) as UUID)
            }
            keys.close()
        } finally {
            preStatement.close()
            connection.close()
        }
        return resultCategory
    }

    fun clear() {
        var connection = defaultDataSource.connection;
        var statement = connection.prepareStatement("DELETE FROM category");
        statement.executeUpdate()
        statement.close()
        connection.close()
    }
}