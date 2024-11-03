package org.codingforanimals.veganuniverse.product.domain.repository

import android.os.Parcelable
import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.model.ProductEdit

interface ProductRepository {
    suspend fun updateProductsFromRemoteToLocal()

    suspend fun getValidatedProductsFromRemote(): List<Product>
    suspend fun getValidatedProductByIdFromRemote(id: String): Product
    suspend fun deleteValidatedProductFromRemote(id: String)
    suspend fun uploadValidatedProductsBatch(products: List<Product>)

    suspend fun getUnvalidatedProductsFromRemote(): List<Product>
    suspend fun getUnvalidatedProductByIdFromRemote(id: String): Product
    suspend fun deleteUnvalidatedProductFromRemote(product: Product)
    suspend fun uploadUnvalidatedProductToRemote(product: Product, imageModel: Parcelable?): String
    suspend fun validateProduct(product: Product): String

    suspend fun getProductEditsFromRemote(): List<ProductEdit>
    suspend fun getProductEditByIdFromRemote(id: String): ProductEdit
    suspend fun uploadProductEdit(product: ProductEdit, imageModel: Parcelable?)
    suspend fun validateProductEdit(edit: ProductEdit)
    suspend fun deleteProductEdit(edit: ProductEdit)

    suspend fun saveProductReport(productId: String, userId: String)

    suspend fun queryProductsFromLocal(
        query: String,
        type: String?,
        category: String?
    ): List<Product>

    suspend fun getProductByIdFromLocal(id: String): Product
    suspend fun getByBrandAndName(brand: String, name: String): Product?
    suspend fun saveProductsToLocal(products: List<Product>)
    suspend fun clearProductsFromLocal()
}
