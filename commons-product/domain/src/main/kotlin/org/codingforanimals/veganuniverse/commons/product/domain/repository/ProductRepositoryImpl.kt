package org.codingforanimals.veganuniverse.commons.product.domain.repository

import android.os.Parcelable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.commons.product.data.source.ProductRemoteDataSource
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductQueryParams

internal class ProductRepositoryImpl(
    private val remoteDataSource: ProductRemoteDataSource,
) : ProductRepository {
    override suspend fun insertProduct(product: Product, imageModel: Parcelable?): String {
        return remoteDataSource.insertProduct(product, imageModel)
    }

    override suspend fun reportProduct(productId: String, userId: String) {
        remoteDataSource.reportProduct(productId, userId)
    }

    override suspend fun editProduct(productId: String, userId: String, edition: String) {
        remoteDataSource.editProduct(productId, userId, edition)
    }

    override suspend fun deleteProductById(id: String) {
        return remoteDataSource.deleteProductById(id)
    }

    override suspend fun getProductById(id: String): Product? {
        return remoteDataSource.getProductById(id)
    }

    override suspend fun getProductListById(ids: List<String>): List<Product> {
        return remoteDataSource.getProductListById(ids)
    }

    override fun queryProductsPagingDataFlow(params: ProductQueryParams): Flow<PagingData<Product>> {
        return remoteDataSource.queryProductsPagingDataFlow(params)
    }

    override fun queryProductsById(ids: List<String>): Flow<PagingData<Product>> {
        return remoteDataSource.queryProductsById(ids)
    }

    override suspend fun queryProducts(params: ProductQueryParams): List<Product> {
        return remoteDataSource.queryProducts(params)
    }

    override suspend fun validateProduct(id: String) {
        return remoteDataSource.validateProduct(id)
    }
}