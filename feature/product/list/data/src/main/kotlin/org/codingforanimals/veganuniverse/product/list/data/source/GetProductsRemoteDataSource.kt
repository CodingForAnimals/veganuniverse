package org.codingforanimals.veganuniverse.product.list.data.source

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.product.list.data.model.ProductEntity

interface GetProductsRemoteDataSource {
    fun getProducts(name: String, category: String?, type: String?): Flow<PagingData<ProductEntity>>
}
