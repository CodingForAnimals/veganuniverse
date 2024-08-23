package org.codingforanimals.veganuniverse.product.data.di

import com.google.firebase.firestore.FirebaseFirestore
import org.codingforanimals.veganuniverse.product.data.model.ProductFirestoreEntityMapper
import org.codingforanimals.veganuniverse.product.data.model.ProductFirestoreEntityMapperImpl
import org.codingforanimals.veganuniverse.product.data.source.ProductFirestoreDataSource
import org.codingforanimals.veganuniverse.product.data.source.ProductRemoteDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val productDataModule = module {
    factoryOf(::ProductFirestoreEntityMapperImpl) bind ProductFirestoreEntityMapper::class

    factory<ProductRemoteDataSource> {
        val collectionPath = ProductFirestoreDataSource.PRODUCTS_COLLECTION
        ProductFirestoreDataSource(
            productsCollection = FirebaseFirestore.getInstance().collection(collectionPath),
            firestoreEntityMapper = get(),
            uploadPictureUseCase = get()
        )
    }
}