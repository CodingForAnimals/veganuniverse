package org.codingforanimals.veganuniverse.product.data.source.local.di

import android.content.Context
import androidx.room.Room
import org.codingforanimals.veganuniverse.product.data.source.local.ProductLocalDataSource
import org.codingforanimals.veganuniverse.product.data.source.local.ProductDatabase
import org.koin.dsl.module

internal val productLocalDataModule = module {
    single<ProductDatabase> {
        val context = get<Context>()
        Room.databaseBuilder(
            context = context,
            klass = ProductDatabase::class.java,
            name = ProductDatabase.DATABASE_NAME,
        ).fallbackToDestructiveMigration().build()
    }

    factory<ProductLocalDataSource> {
        val database = get<ProductDatabase>()
        database.getProductDao()
    }
}
