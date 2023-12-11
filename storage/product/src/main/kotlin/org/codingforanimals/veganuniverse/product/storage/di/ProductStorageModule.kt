package org.codingforanimals.veganuniverse.product.storage.di

import androidx.room.Room
import org.codingforanimals.veganuniverse.product.storage.ProductDatabase
import org.koin.dsl.module

val productStorageModule = module {
    factory {
        Room.databaseBuilder(
            context = get(),
            klass = ProductDatabase::class.java,
            name = ProductDatabase.NAME,
        ).build()
    }

    factory { get<ProductDatabase>().productDao() }
}