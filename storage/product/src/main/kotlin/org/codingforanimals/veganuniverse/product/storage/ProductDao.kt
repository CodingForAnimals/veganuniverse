package org.codingforanimals.veganuniverse.product.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface ProductDao {

    @Query("SELECT * FROM productentity")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM productentity WHERE name LIKE :name")
    suspend fun getByName(name: String): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(productEntity: ProductEntity)

    @Delete
    suspend fun deleteProduct(productEntity: ProductEntity)
}
