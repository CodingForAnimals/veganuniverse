package org.codingforanimals.veganuniverse.product.data.di

import androidx.paging.PagingConfig
import com.google.firebase.firestore.FirebaseFirestore
import org.codingforanimals.veganuniverse.commons.firebase.storage.di.commonsFirebaseStorageModule
import org.codingforanimals.veganuniverse.product.data.source.GetLatestProductsFirestoreDataSource
import org.codingforanimals.veganuniverse.product.data.source.GetLatestProductsRemoteDataSource
import org.codingforanimals.veganuniverse.product.data.source.GetPaginatedProductFirestoreDataSource
import org.codingforanimals.veganuniverse.product.data.source.GetPaginatedProductsRemoteDataSource
import org.codingforanimals.veganuniverse.product.data.source.SuggestionFirestoreRepository
import org.codingforanimals.veganuniverse.product.data.source.SuggestionRepository
import org.koin.dsl.module

private const val PRODUCTS_ITEMS_COLLECTION = "content/products/items"
private const val PRODUCTS_SUGGESTIONS_COLLECTION = "content/products/suggestions"

val productDataModule = module {
    includes(
        commonsFirebaseStorageModule,
    )
    factory { get<FirebaseFirestore>().collection(PRODUCTS_ITEMS_COLLECTION) }
    factory<GetLatestProductsRemoteDataSource> {
        GetLatestProductsFirestoreDataSource(
            productsCollection = get<FirebaseFirestore>().collection(PRODUCTS_ITEMS_COLLECTION),
            publicImageApi = get(),
        )
    }
    factory<GetPaginatedProductsRemoteDataSource> {
        GetPaginatedProductFirestoreDataSource(
            productsCollection = get<FirebaseFirestore>().collection(PRODUCTS_ITEMS_COLLECTION),
            publicImageApi = get(),
            pagingConfig = PagingConfig(10)
        )
    }
    factory<SuggestionRepository> {
        SuggestionFirestoreRepository(
            suggestionsCollection = get<FirebaseFirestore>().collection(
                PRODUCTS_SUGGESTIONS_COLLECTION
            )
        )
    }
}
