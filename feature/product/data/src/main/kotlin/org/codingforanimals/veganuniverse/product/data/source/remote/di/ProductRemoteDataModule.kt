package org.codingforanimals.veganuniverse.product.data.source.remote.di

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.codingforanimals.veganuniverse.product.data.source.remote.ProductFirebaseDataSource
import org.codingforanimals.veganuniverse.product.data.source.remote.ProductRemoteDataSource
import org.koin.dsl.module

internal val productRemoteDataModule = module {
    factory<ProductRemoteDataSource> {
        ProductFirebaseDataSource(
            database = Firebase.database,
            publicImageApi = get(),
            uploadPicture = get(),
            deletePicture = get()
        )
    }
}
