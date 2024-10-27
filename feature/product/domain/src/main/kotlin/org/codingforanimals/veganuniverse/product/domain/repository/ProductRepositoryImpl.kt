package org.codingforanimals.veganuniverse.product.domain.repository

import android.os.Parcelable
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.product.data.source.local.ProductLocalDataSource
import org.codingforanimals.veganuniverse.product.data.source.remote.ProductRemoteDataSource
import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.model.ProductEdit
import org.codingforanimals.veganuniverse.product.domain.model.toDTO
import org.codingforanimals.veganuniverse.product.domain.model.toDomain
import org.codingforanimals.veganuniverse.product.domain.model.toEntity

internal class ProductRepositoryImpl(
    private val remoteDataSource: ProductRemoteDataSource,
    private val localDataSource: ProductLocalDataSource,
) : ProductRepository {

    override suspend fun getValidatedProductsFromRemote(): List<Product> {
        return remoteDataSource.getValidatedProducts().mapNotNull { dto ->
            runCatching { dto.toDomain() }
                .onFailure { Analytics.logNonFatalException(it) }
                .getOrNull()
        }
    }

    override suspend fun getValidatedProductByIdFromRemote(id: String): Product {
        return remoteDataSource.getValidatedProductById(id).toDomain()
    }

    override suspend fun deleteValidatedProductFromRemote(id: String) {
        remoteDataSource.deleteValidatedProduct(id)
    }

    override suspend fun uploadValidatedProductsBatch(products: List<Product>) {
        remoteDataSource.uploadValidatedProductBatch(products.map { it.toDTO() })
    }

    override suspend fun getUnvalidatedProductsFromRemote(): List<Product> {
        return remoteDataSource.getUnvalidatedProducts().mapNotNull { dto ->
            runCatching { dto.toDomain() }
                .onFailure { Analytics.logNonFatalException(it) }
                .getOrNull()
        }
    }

    override suspend fun getUnvalidatedProductByIdFromRemote(id: String): Product {
        return remoteDataSource.getUnvalidatedProductById(id).toDomain()
    }

    override suspend fun deleteUnvalidatedProductFromRemote(id: String) {
        remoteDataSource.deleteUnvalidatedProduct(id)
    }

    override suspend fun uploadUnvalidatedProductToRemote(product: Product, imageModel: Parcelable?): String {
        return remoteDataSource.uploadUnvalidatedProduct(product.toDTO(), imageModel)
    }

    override suspend fun validateProduct(product: Product): String {
        return remoteDataSource.validateProduct(product.toDTO())
    }

    override suspend fun getProductEditsFromRemote(): List<ProductEdit> {
        return remoteDataSource.getProductEdits().mapNotNull {
            runCatching { it.toDomain() }
                .onFailure { Analytics.logNonFatalException(it) }
                .getOrNull()
        }
    }

    override suspend fun getProductEditByIdFromRemote(id: String): ProductEdit {
        return remoteDataSource.getProductEditById(id).toDomain()
    }

    override suspend fun uploadProductEdit(product: ProductEdit, imageModel: Parcelable?) {
        remoteDataSource.uploadProductEdit(product.toDTO(), imageModel)
    }

    override suspend fun validateProductEdit(edit: ProductEdit) {
        remoteDataSource.validateProductEdit(edit.toDTO())
    }

    override suspend fun deleteProductEdit(id: String) {
        remoteDataSource.deleteProductEdit(id)
    }

    override suspend fun saveProductReport(productId: String, userId: String) {
        remoteDataSource.saveProductReport(productId, userId)
    }

    override suspend fun queryProductsFromLocal(
        query: String,
        type: String?,
        category: String?
    ): List<Product> = when {
        type == null && category == null -> {
            localDataSource.queryProducts(query)
        }

        type != null && category == null -> {
            localDataSource.queryProductsByType(query, type)
        }

        type == null && category != null -> {
            localDataSource.queryProductsByCategory(query, category)
        }

        type != null && category != null -> {
            localDataSource.queryProductsByTypeAndCategory(query, type, category)
        }

        else -> throw Exception("Unknown query parameters $query, $type, $category")
    }.mapNotNull {
        runCatching { it.toDomain() }
            .onFailure { Analytics.logNonFatalException(it) }
            .getOrNull()
    }

    override suspend fun getByBrandAndName(brand: String, name: String): Product? {
        return localDataSource.getByBrandAndName(brand, name)?.toDomain()
    }

    override suspend fun getProductByIdFromLocal(id: String): Product {
        return localDataSource.getById(id).toDomain()
    }


    override suspend fun saveProductsToLocal(products: List<Product>) {
        localDataSource.insertProduct(
            *products.mapNotNull {
                runCatching { it.toEntity() }
                    .onFailure { Analytics.logNonFatalException(it) }
                    .getOrNull()
            }.toTypedArray()
        )
    }

    override suspend fun clearProductsFromLocal() {
        localDataSource.clearProducts()
    }
}
