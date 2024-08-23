package org.codingforanimals.veganuniverse.product.domain

import android.os.Parcelable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.product.data.source.ProductRemoteDataSource
import org.codingforanimals.veganuniverse.product.model.Product
import org.codingforanimals.veganuniverse.product.model.ProductQueryParams

internal class ProductRepositoryImpl(
    private val remoteDataSource: ProductRemoteDataSource,
) : ProductRepository {
    override suspend fun insertProduct(product: Product, imageModel: Parcelable): String {
        return remoteDataSource.insertProduct(product, imageModel)
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

    override suspend fun queryProducts(params: ProductQueryParams): List<Product> {
        return remoteDataSource.queryProducts(params)
    }

}