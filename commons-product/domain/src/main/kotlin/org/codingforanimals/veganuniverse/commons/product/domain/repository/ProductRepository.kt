package org.codingforanimals.veganuniverse.commons.product.domain.repository

import android.os.Parcelable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductQueryParams

interface ProductRepository {
    suspend fun insertProduct(product: Product, imageModel: Parcelable?): String
    suspend fun reportProduct(productId: String, userId: String)
    suspend fun editProduct(productId: String, userId: String, edition: String)
    suspend fun deleteProductById(id: String)
    suspend fun getProductById(id: String): Product?
    suspend fun getProductListById(ids: List<String>): List<Product>
    fun queryProductsPagingDataFlow(params: ProductQueryParams): Flow<PagingData<Product>>
    fun queryProductsById(ids: List<String>): Flow<PagingData<Product>>
    suspend fun queryProducts(params: ProductQueryParams): List<Product>
}