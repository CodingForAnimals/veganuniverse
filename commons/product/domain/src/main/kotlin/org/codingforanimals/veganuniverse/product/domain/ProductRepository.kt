package org.codingforanimals.veganuniverse.product.domain

import android.os.Parcelable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.product.model.Product
import org.codingforanimals.veganuniverse.product.model.ProductQueryParams

interface ProductRepository {
    suspend fun insertProduct(product: Product, imageModel: Parcelable): String
    suspend fun deleteProductById(id: String)
    suspend fun getProductById(id: String): Product?
    suspend fun getProductListById(ids: List<String>): List<Product>
    fun queryProductsPagingDataFlow(params: ProductQueryParams): Flow<PagingData<Product>>
    suspend fun queryProducts(params: ProductQueryParams): List<Product>
}

