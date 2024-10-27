package org.codingforanimals.veganuniverse.product.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.codingforanimals.veganuniverse.product.data.source.local.model.ProductEntity

@Dao
interface ProductLocalDataSource {
    @Insert
    suspend fun insertProduct(vararg product: ProductEntity)

    @Query("DELETE FROM ProductEntity")
    suspend fun clearProducts()

    @Query("SELECT * FROM ProductEntity")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM ProductEntity" +
            " WHERE (nameAccentInsensitive LIKE '%' || :searchText || '%' " +
            "OR brandAccentInsensitive LIKE '%' || :searchText || '%') ")
    suspend fun queryProducts(searchText: String): List<ProductEntity>

    @Query("SELECT * FROM ProductEntity" +
            " WHERE (nameAccentInsensitive LIKE '%' || :searchText || '%' " +
            "OR brandAccentInsensitive LIKE '%' || :searchText || '%') " +
            "AND type LIKE :type ")
    suspend fun queryProductsByType(searchText: String, type: String = ""): List<ProductEntity>

    @Query("SELECT * FROM ProductEntity" +
            " WHERE (nameAccentInsensitive LIKE '%' || :searchText || '%' " +
            "OR brandAccentInsensitive LIKE '%' || :searchText || '%') " +
            "AND category LIKE :category ")
    suspend fun queryProductsByCategory(searchText: String, category: String = ""): List<ProductEntity>

    @Query("SELECT * FROM ProductEntity" +
            " WHERE (nameAccentInsensitive LIKE '%' || :searchText || '%' " +
            "OR brandAccentInsensitive LIKE '%' || :searchText || '%') " +
            "AND type LIKE :type " +
            "AND category LIKE :category ")
    suspend fun queryProductsByTypeAndCategory(searchText: String, type: String = "", category: String = ""): List<ProductEntity>

    @Query("SELECT * FROM ProductEntity WHERE brandAccentInsensitive = :brand AND nameAccentInsensitive = :name")
    suspend fun getByBrandAndName(brand: String, name: String): ProductEntity?

    @Query("SELECT * FROM ProductEntity WHERE id = :id")
    suspend fun getById(id: String): ProductEntity
}