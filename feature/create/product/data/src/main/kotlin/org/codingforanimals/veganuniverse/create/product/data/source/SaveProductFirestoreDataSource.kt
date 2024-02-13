package org.codingforanimals.veganuniverse.create.product.data.source

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.create.product.data.dto.ProductFormDTO
import org.codingforanimals.veganuniverse.create.product.data.mapper.toFirebaseModel
import org.codingforanimals.veganuniverse.create.product.data.model.SaveProductResult
import org.codingforanimals.veganuniverse.services.firebase.storageImageMetadata
import java.io.ByteArrayOutputStream

internal class SaveProductFirestoreDataSource(
    firestore: FirebaseFirestore,
    storage: FirebaseStorage,
) : SaveProductRemoteDataSource {

    private val productsCollection = firestore.collection(PRODUCTS_COLLECTION)
    private val productsStorage = storage.getReference(PRODUCTS_STORAGE)

    override suspend fun saveProduct(product: ProductFormDTO): SaveProductResult {
        return runCatching {
            productsCollection
                .whereEqualTo(PRODUCT_NAME, product.name)
                .whereEqualTo(PRODUCT_BRAND, product.brand)
                .get().await().documents.let { documents ->
                    if (documents.isEmpty()) {
                        val imageStorageRef = storeImage(product.imageModel)
                        val model = product.toFirebaseModel(imageStorageRef)
                        val docId = productsCollection.add(model).await().id
                        SaveProductResult.Success(docId)
                    } else {
                        SaveProductResult.Error.ConflictingEntityError(documents.first().id)
                    }
                }
        }.getOrElse {
            SaveProductResult.Error.UnexpectedError(it)
        }
    }

    private suspend fun storeImage(model: Parcelable): String {
        val imageId = getRandomString()
        val imageStorageRef = productsStorage.child(imageId)
        when (model) {
            is Bitmap -> {
                val bytes = ByteArrayOutputStream()
                model.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                imageStorageRef.putBytes(
                    bytes.toByteArray(),
                    storageImageMetadata,
                )
            }

            is Uri -> {
                imageStorageRef.putFile(
                    model,
                    storageImageMetadata
                )
            }

            else -> {
                throw RuntimeException("Unsupported image format")
            }
        }.await()
        return imageStorageRef.path + RESIZE_SUFFIX
    }

    private fun getRandomString(): String {
        val allowedChars = ('a' .. 'z') + ('A' .. 'Z') + ('0' .. '9')
        return List(20) { allowedChars.random() }.joinToString("")
    }

    companion object {
        private const val PRODUCTS_COLLECTION = "content/products/items"
        private const val PRODUCTS_STORAGE = "content/products/picture"
        private const val PRODUCT_NAME = "name"
        private const val PRODUCT_BRAND = "brand"
        private const val RESIZE_SUFFIX = "_800x800"
    }
}
