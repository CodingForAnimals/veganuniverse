package org.codingforanimals.veganuniverse.product.data.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.commons.firebase.storage.PublicImageApi
import org.codingforanimals.veganuniverse.product.data.model.Product

interface GetLatestProductsRemoteDataSource {
    suspend fun getProducts(): List<Product>
}

internal class GetLatestProductsFirestoreDataSource(
    private val productsCollection: CollectionReference,
    private val publicImageApi: PublicImageApi,
) : GetLatestProductsRemoteDataSource {
    override suspend fun getProducts(): List<Product> {
        return productsCollection
            .orderBy(CREATED_AT, Query.Direction.DESCENDING)
            .limit(3)
            .get().await()
            .toObjects(Product::class.java)
            .map { product ->
                product.imageStorageRef?.let { imageStorageRef ->
                    product.copy(
                        imageStorageRef = publicImageApi.withImageStorageRef(
                            imageStorageRef
                        )
                    )
                } ?: product
            }
    }

    companion object {
        private const val CREATED_AT = "createdAt"
    }
}
