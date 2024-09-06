package org.codingforanimals.veganuniverse.commons.product.data.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.codingforanimals.veganuniverse.commons.product.data.model.ProductFirestoreEntityMapper
import org.codingforanimals.veganuniverse.commons.product.data.model.ProductFirestoreEntityMapperImpl
import org.codingforanimals.veganuniverse.commons.product.data.source.ProductFirebaseReferences
import org.codingforanimals.veganuniverse.commons.product.data.source.ProductFirestoreDataSource
import org.codingforanimals.veganuniverse.commons.product.data.source.ProductFirestoreDataSource.Companion.PRODUCTS_COLLECTION
import org.codingforanimals.veganuniverse.commons.product.data.source.ProductFirestoreDataSource.Companion.PRODUCTS_REPORTS_REFERENCE
import org.codingforanimals.veganuniverse.commons.product.data.source.ProductFirestoreDataSource.Companion.PRODUCTS_SUGGESTIONS_REFERENCE
import org.codingforanimals.veganuniverse.commons.product.data.source.ProductRemoteDataSource
import org.codingforanimals.veganuniverse.firebase.storage.di.firebaseStorageModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val productCommonDataModule = module {
    includes(firebaseStorageModule)
    factoryOf(::ProductFirestoreEntityMapperImpl) bind ProductFirestoreEntityMapper::class

    factory<ProductRemoteDataSource> {
        ProductFirestoreDataSource(
            references = ProductFirebaseReferences(
                items = FirebaseFirestore.getInstance().collection(PRODUCTS_COLLECTION),
                reports = FirebaseDatabase.getInstance().getReference(PRODUCTS_REPORTS_REFERENCE),
                suggestions = FirebaseDatabase.getInstance().getReference(PRODUCTS_SUGGESTIONS_REFERENCE)
            ),
            firestoreEntityMapper = get(),
            uploadPictureUseCase = get()
        )
    }
}