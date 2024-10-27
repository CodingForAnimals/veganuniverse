package org.codingforanimals.veganuniverse.product.data.config.remote.di

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.codingforanimals.veganuniverse.product.data.config.remote.ProductConfigFirebaseSource
import org.codingforanimals.veganuniverse.product.data.config.remote.ProductConfigRemoteSource
import org.koin.dsl.module

internal val productConfigRemoteModule = module {
    factory<ProductConfigRemoteSource> {
        ProductConfigFirebaseSource(
            database = Firebase.database,
        )
    }
}
