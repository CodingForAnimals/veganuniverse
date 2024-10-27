package org.codingforanimals.veganuniverse.product.data.config.local.di

import android.content.Context
import org.codingforanimals.veganuniverse.product.data.config.local.ProductConfigDataStore
import org.codingforanimals.veganuniverse.product.data.config.local.ProductConfigLocalSource
import org.codingforanimals.veganuniverse.product.data.config.local.productConfigDataStore
import org.koin.dsl.module

internal val productConfigLocalModule = module {
    factory<ProductConfigLocalSource> {
        val context = get<Context>()
        ProductConfigDataStore(
            dataStore = context.productConfigDataStore,
        )
    }
}
