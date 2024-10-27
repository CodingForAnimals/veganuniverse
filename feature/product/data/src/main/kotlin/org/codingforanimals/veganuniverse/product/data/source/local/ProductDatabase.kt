package org.codingforanimals.veganuniverse.product.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.codingforanimals.veganuniverse.product.data.source.local.model.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
internal abstract class ProductDatabase : RoomDatabase() {
    abstract fun getProductDao(): ProductLocalDataSource

    companion object {
        const val DATABASE_NAME = "product-database"
    }
}
