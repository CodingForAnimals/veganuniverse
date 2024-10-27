package org.codingforanimals.veganuniverse.product.data.source.remote

import android.os.Parcelable
import org.codingforanimals.veganuniverse.product.data.source.remote.model.ProductDTO
import org.codingforanimals.veganuniverse.product.data.source.remote.model.ProductEditDTO

interface ProductRemoteDataSource {
    suspend fun getValidatedProducts(): List<ProductDTO>
    suspend fun getValidatedProductById(id: String): ProductDTO
    suspend fun deleteValidatedProduct(id: String)
    suspend fun uploadValidatedProductBatch(products: List<ProductDTO>)

    suspend fun getUnvalidatedProducts(): List<ProductDTO>
    suspend fun getUnvalidatedProductById(id: String): ProductDTO
    suspend fun deleteUnvalidatedProduct(id: String)
    suspend fun uploadUnvalidatedProduct(product: ProductDTO, imageModel: Parcelable?): String
    suspend fun validateProduct(product: ProductDTO): String

    suspend fun getProductEdits(): List<ProductEditDTO>
    suspend fun getProductEditById(id: String): ProductEditDTO
    suspend fun uploadProductEdit(product: ProductEditDTO, imageModel: Parcelable?)
    suspend fun validateProductEdit(edit: ProductEditDTO)
    suspend fun deleteProductEdit(id: String)

    suspend fun saveProductReport(productId: String, userId: String)
}
