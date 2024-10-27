package org.codingforanimals.veganuniverse.product.data.source.remote

import android.os.Parcelable
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.firebase.storage.model.PublicImageApi
import org.codingforanimals.veganuniverse.firebase.storage.model.ResizeResolution
import org.codingforanimals.veganuniverse.firebase.storage.usecase.UploadPictureUseCase
import org.codingforanimals.veganuniverse.product.data.source.remote.model.ProductDTO
import org.codingforanimals.veganuniverse.product.data.source.remote.model.ProductEditDTO

internal class ProductFirebaseDataSource(
    database: FirebaseDatabase,
    private val publicImageApi: PublicImageApi,
    private val uploadPictureUseCase: UploadPictureUseCase,
) : ProductRemoteDataSource {

    private val productsReference = database.getReference(PRODUCTS_PATH)
    private val editsReference = database.getReference(EDITS_PATH)
    private val unvalidatedReference = database.getReference(UNVALIDATED_PATH)
    private val reportsReference = database.getReference(REPORTS_PATH)

    override suspend fun getValidatedProducts(): List<ProductDTO> {
        return productsReference
            .get().await()
            .children.mapNotNull { snapshot ->
                val product = snapshot.getValue(ProductDTO::class.java)

                if (product == null) {
                    Log.e(TAG, "ProductDTO is null at key ${snapshot.key}")
                }

                product?.copy(
                    objectKey = snapshot.key,
                    imageId = product.imageId?.let { imageId ->
                        publicImageApi.getFilePath(
                            path = BASE_PRODUCT_PICTURE_PATH,
                            imageId = imageId,
                        )
                    }
                )
            }
    }

    override suspend fun getValidatedProductById(id: String): ProductDTO {
        return productsReference.child(id).get().await().let {
            val product = it.getValue(ProductDTO::class.java)!!
            product.copy(
                objectKey = id,
                imageId = product.imageId?.let { imageId ->
                    publicImageApi.getFilePath(
                        path = BASE_PRODUCT_PICTURE_PATH,
                        imageId = imageId,
                    )
                }
            )
        }
    }

    override suspend fun deleteValidatedProduct(id: String) {
        productsReference.child(id).removeValue().await()
    }

    override suspend fun uploadValidatedProductBatch(products: List<ProductDTO>) = coroutineScope {
        products.map {
            launch {
                val idReference = productsReference.push()
                val product = it.copy(
                    username = "Coding for Animals"
                )
                idReference.setValue(product).await()

                idReference.updateChildren(
                    mapOf(
                        ProductDTO.TIMESTAMP to ServerValue.TIMESTAMP,
                        ProductDTO.LAST_UPDATED to ServerValue.TIMESTAMP,
                    )
                ).await()
            }
        }.joinAll()
    }

    override suspend fun getUnvalidatedProducts(): List<ProductDTO> {
        return unvalidatedReference
            .get().await()
            .children.mapNotNull { snapshot ->
                val product = snapshot.getValue(ProductDTO::class.java)

                if (product == null) {
                    Log.e(TAG, "ProductDTO is null at key ${snapshot.key}")
                }

                product?.copy(
                    objectKey = snapshot.key,
                    imageId = product.imageId?.let { imageId ->
                        publicImageApi.getFilePath(
                            path = BASE_PRODUCT_PICTURE_PATH,
                            imageId = imageId,
                        )
                    }
                )
            }
    }

    override suspend fun getUnvalidatedProductById(id: String): ProductDTO {
        return unvalidatedReference.child(id).get().await().let {
            val product = it.getValue(ProductDTO::class.java)!!
            product.copy(
                objectKey = id,
                imageId = product.imageId?.let { imageId ->
                    publicImageApi.getFilePath(
                        path = BASE_PRODUCT_PICTURE_PATH,
                        imageId = imageId,
                    )
                }
            )
        }
    }

    override suspend fun deleteUnvalidatedProduct(id: String) {
        unvalidatedReference.child(id).removeValue().await()
    }

    override suspend fun uploadUnvalidatedProduct(
        product: ProductDTO,
        imageModel: Parcelable?
    ): String {
        val idReference = unvalidatedReference.push()
        idReference.setValue(product).await()

        val resizedPictureId = imageModel?.let {
            val pictureId = uploadPictureUseCase(
                fileFolderPath = BASE_PRODUCT_PICTURE_PATH,
                model = imageModel,
            )
            pictureId + ResizeResolution.MEDIUM.suffix
        }

        idReference.updateChildren(
            mapOf(
                ProductDTO.TIMESTAMP to ServerValue.TIMESTAMP,
                ProductDTO.LAST_UPDATED to ServerValue.TIMESTAMP,
                ProductDTO.PRODUCT_IMAGE to resizedPictureId,
            )
        ).await()
        return idReference.key!!
    }

    override suspend fun validateProduct(product: ProductDTO): String {
        val unvalidatedProductId = checkNotNull(product.objectKey) {
            "Unvalidated product objectKey cannot be null"
        }
        val unvalidatedProductReference = unvalidatedReference.child(unvalidatedProductId)
        val validatedProductReference = productsReference.push()

        val newProduct = product.copy(
            objectKey = null,
            timestamp = null,
            lastUpdated = null,
            imageId = product.imageId
        )

        validatedProductReference.setValue(newProduct).await()
        validatedProductReference.updateChildren(
            mapOf(
                ProductDTO.TIMESTAMP to ServerValue.TIMESTAMP,
                ProductDTO.LAST_UPDATED to ServerValue.TIMESTAMP,
            )
        ).await()

        unvalidatedProductReference.removeValue().await()



        return validatedProductReference.key!!
    }

    override suspend fun getProductEdits(): List<ProductEditDTO> {
        return editsReference
            .get().await()
            .children.mapNotNull { snapshot ->
                val edit = snapshot.getValue(ProductEditDTO::class.java) ?: run {
                    Log.e(TAG, "ProductDTO is null at key ${snapshot.key}")
                    return@mapNotNull null
                }

                val objectKey = snapshot.key ?: run {
                    Log.e(TAG, "ProductEditDTO objectKey cannot be null")
                    return@mapNotNull null
                }

                edit.copy(
                    objectKey = objectKey,
                    imageId = edit.imageId?.let { imageId ->
                        publicImageApi.getFilePath(
                            path = BASE_PRODUCT_PICTURE_PATH,
                            imageId = imageId,
                        )
                    }
                )
            }
    }

    override suspend fun getProductEditById(id: String): ProductEditDTO {
        return editsReference.child(id).get().await().let {
            val product = it.getValue(ProductEditDTO::class.java)!!
            product.copy(
                objectKey = id,
                imageId = product.imageId?.let { imageId ->
                    publicImageApi.getFilePath(
                        path = BASE_PRODUCT_PICTURE_PATH,
                        imageId = imageId,
                    )
                }
            )
        }
    }

    override suspend fun uploadProductEdit(product: ProductEditDTO, imageModel: Parcelable?) {
        val editIdReference = editsReference.push()

        editIdReference.setValue(product).await()

        imageModel?.let {
            val imageId = uploadPictureUseCase(
                fileFolderPath = BASE_PRODUCT_PICTURE_PATH,
                model = imageModel,
            ) + ResizeResolution.MEDIUM.suffix
            editIdReference.updateChildren(
                mapOf(
                    ProductDTO.PRODUCT_IMAGE to imageId,
                )
            ).await()
        }
        editIdReference.updateChildren(
            mapOf(
                ProductDTO.LAST_UPDATED to ServerValue.TIMESTAMP,
            )
        ).await()
    }

    override suspend fun validateProductEdit(edit: ProductEditDTO) {
        val originalKey = checkNotNull(edit.originalKey) {
            "ProductEditDTO originalKey cannot be null"
        }

        val editKey = checkNotNull(edit.objectKey) {
            "ProductEditDTO objectKey cannot be null"
        }

        val originalReference = productsReference.child(originalKey)
        val editReference = editsReference.child(editKey)

        originalReference.setValue(edit.toProductDTO()).await()
        editReference.removeValue().await()
    }

    override suspend fun deleteProductEdit(id: String) {
        editsReference.child(id).removeValue().await()
    }

    override suspend fun saveProductReport(productId: String, userId: String) {
        reportsReference.child(productId).child(userId).setValue(true).await()
    }

    private fun ProductEditDTO.toProductDTO(): ProductDTO {
        return ProductDTO(
            objectKey = null,
            userId = userId,
            username = username,
            name = name,
            brand = brand,
            type = type,
            category = category,
            imageId = imageId,
            description = description,
            timestamp = timestamp,
            lastUpdated = lastUpdated,
            sourceUrl = sourceUrl
        )
    }

    companion object {
        private const val TAG = "ProductFirebaseDataSource"
        private const val PRODUCTS_PATH = "content/products/items"
        private const val REPORTS_PATH = "content/products/reports"
        private const val UNVALIDATED_PATH = "content/products/unvalidated"
        private const val EDITS_PATH = "content/products/edits"
        internal const val BASE_PRODUCT_PICTURE_PATH = "content/products/picture"
    }
}
