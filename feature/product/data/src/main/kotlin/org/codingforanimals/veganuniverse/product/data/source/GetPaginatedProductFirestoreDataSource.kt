package org.codingforanimals.veganuniverse.product.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.codingforanimals.veganuniverse.commons.firebase.storage.PublicImageApi
import org.codingforanimals.veganuniverse.product.data.model.Product
import org.codingforanimals.veganuniverse.product.data.paging.ProductFirestorePagingSource

interface GetPaginatedProductsRemoteDataSource {
    fun getProducts(name: String, category: String?, type: String?): Flow<PagingData<Product>>
}

internal class GetPaginatedProductFirestoreDataSource(
    private val productsCollection: CollectionReference,
    private val publicImageApi: PublicImageApi,
    private val pagingConfig: PagingConfig,
) : GetPaginatedProductsRemoteDataSource {

    override fun getProducts(
        name: String,
        category: String?,
        type: String?,
    ): Flow<PagingData<Product>> {
        var query = productsCollection
            .orderBy(NAME)
            .limit(pagingConfig.pageSize.toLong())

        if (type?.isNotBlank() == true) {
            query = query.whereEqualTo(TYPE, type)
        }

        if (category?.isNotBlank() == true) {
            query = query.whereEqualTo(CATEGORY, category)
        }

        if (name.isNotBlank()) {
            query = query.whereArrayContains(KEYWORDS, name)
        }
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { ProductFirestorePagingSource(query) }
        ).flow.map { pagingData ->
            pagingData.map { product ->
                product.imageStorageRef?.let { imageStorageRef ->
                    product.copy(
                        imageStorageRef = publicImageApi.withImageStorageRef(imageStorageRef)
                    )
                } ?: product
            }
        }
    }

    companion object {
        private const val NAME = "name"
        private const val TYPE = "type"
        private const val CATEGORY = "category"
        private const val KEYWORDS = "keywords"
    }
}