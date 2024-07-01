package org.codingforanimals.veganuniverse.commons.product.data.source

import android.os.Parcelable
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.commons.network.mapFirestoreExceptions
import org.codingforanimals.veganuniverse.commons.product.data.model.ProductFirestoreEntity
import org.codingforanimals.veganuniverse.commons.product.data.model.ProductFirestoreEntityMapper
import org.codingforanimals.veganuniverse.commons.product.data.paging.ProductPagingSource
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductQueryParams
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductSorter
import org.codingforanimals.veganuniverse.firebase.storage.model.ResizeResolution
import org.codingforanimals.veganuniverse.firebase.storage.usecase.UploadPictureUseCase

data class ProductFirebaseReferences(
    val items: CollectionReference,
    val reports: DatabaseReference,
    val suggestions: DatabaseReference,
)

internal class ProductFirestoreDataSource(
    private val references: ProductFirebaseReferences,
    private val firestoreEntityMapper: ProductFirestoreEntityMapper,
    private val uploadPictureUseCase: UploadPictureUseCase,
) : ProductRemoteDataSource {
    override suspend fun insertProduct(product: Product, imageModel: Parcelable): String {
        val pictureId = uploadPictureUseCase(
            fileFolderPath = BASE_PRODUCT_PICTURE_PATH,
            model = imageModel,
        )
        val resizedPictureId = pictureId + ResizeResolution.MEDIUM.suffix

        val entity = product.toNewFirestoreEntity(resizedPictureId)
        val docRef = references.items.document()
        try {
            docRef.set(entity).await()
        } catch (e: FirebaseFirestoreException) {
            throw mapFirestoreExceptions(e)
        }
        return docRef.id
    }

    override suspend fun reportProduct(productId: String, userId: String) {
        references.reports.child(productId)
            .child(userId)
            .setValue(true)
            .await()
    }

    override suspend fun editProduct(productId: String, userId: String, edition: String) {
        references.suggestions.child(productId).child(userId).setValue(edition).await()
    }

    override suspend fun deleteProductById(id: String) {
        references.items.document(id).delete().await()
    }

    override suspend fun getProductById(id: String): Product? {
        return references.items.document(id)
            .get().await()
            .toObject(ProductFirestoreEntity::class.java)
            ?.let { firestoreEntityMapper.mapToModel(it) }
    }

    override suspend fun getProductListById(ids: List<String>): List<Product> {
        return coroutineScope {
            ids.map { async { getProductById(it) } }.awaitAll().filterNotNull()
        }
    }

    override fun queryProductsPagingDataFlow(params: ProductQueryParams): Flow<PagingData<Product>> {
        val query = params.getQuery()
        val pagingSource = ProductPagingSource(query)
        return Pager(
            config = PagingConfig(
                pageSize = params.pageSize,
                maxSize = params.maxSize,
            ),
            pagingSourceFactory = { pagingSource },
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                firestoreEntityMapper.mapToModel(entity)
            }
        }
    }

    override suspend fun queryProducts(params: ProductQueryParams): List<Product> {
        return params.getQuery().get().await()
            .toObjects(ProductFirestoreEntity::class.java)
            .map { firestoreEntityMapper.mapToModel(it) }
    }

    private fun ProductSorter.getSortingField(): String {
        return when (this) {
            ProductSorter.NAME -> FIELD_NAME_LOWERCASE
            ProductSorter.DATE -> FIELD_CREATED_AT
        }
    }

    private fun ProductSorter.getSortingDirection(): Query.Direction {
        return when (this) {
            ProductSorter.NAME -> Query.Direction.ASCENDING
            ProductSorter.DATE -> Query.Direction.DESCENDING
        }
    }

    private fun ProductQueryParams.getQuery(): Query {
        var query = references.items
            .orderBy(sorter.getSortingField(), sorter.getSortingDirection())
            .limit(pageSize.toLong())

        keyword?.lowercase()?.trim()?.takeIf { it.isNotBlank() }?.let {
            query = query.whereArrayContains(FIELD_KEYWORDS, it)
        }

        name?.lowercase()?.trim()?.takeIf { it.isNotBlank() }?.let {
            query = query.whereEqualTo(FIELD_NAME_LOWERCASE, it)
        }

        brand?.lowercase()?.trim()?.takeIf { it.isNotBlank() }?.let {
            query = query.whereEqualTo(FIELD_BRAND_LOWERCASE, it)
        }

        validated?.let {
            query = query.whereEqualTo(FIELD_VALIDATED, validated)
        }

        type?.let {
            query = query.whereEqualTo(FIELD_TYPE, it.name)
        }

        category?.let {
            query = query.whereEqualTo(FIELD_CATEGORY, it.name)
        }

        return query
    }

    private fun Product.toNewFirestoreEntity(imageId: String): ProductFirestoreEntity {
        return ProductFirestoreEntity(
            id = id,
            userId = userId,
            username = username,
            name = name,
            nameLowercase = name.lowercase(),
            brand = brand,
            brandLowercase = brand.lowercase(),
            keywords = createKeywords(name, brand),
            comment = comment,
            type = type.name,
            category = category.name,
            imageId = imageId,
            validated = false,
            createdAt = null,
        )
    }

    private fun createKeywords(vararg searchableFields: String): List<String> {
        val keywords = mutableListOf<String>()
        fun addToKeywordsIfLongEnough(string: String, index: Int) {
            if (index >= 2) {
                keywords.add(string.substring(0, index + 1).lowercase())
            }
        }
        searchableFields.forEach { string ->

            string.forEachIndexed { index, _ ->
                addToKeywordsIfLongEnough(string, index)
            }
            string.split(" ").takeIf { it.size > 1 }?.forEachIndexed { index, word ->
                if (index >= 1) {
                    word.forEachIndexed { wordIndex, _ ->
                        addToKeywordsIfLongEnough(word, wordIndex)
                    }
                }
            }
        }
        return keywords
    }

    companion object {
        internal const val PRODUCTS_COLLECTION = "content/products/items"
        internal const val PRODUCTS_REPORTS_REFERENCE = "content/products/reports"
        internal const val PRODUCTS_SUGGESTIONS_REFERENCE = "content/products/suggestions"
        internal const val BASE_PRODUCT_PICTURE_PATH = "content/products/picture"
        private const val FIELD_NAME_LOWERCASE = "nameLowercase"
        private const val FIELD_BRAND_LOWERCASE = "brandLowercase"
        private const val FIELD_KEYWORDS = "keywords"
        private const val FIELD_TYPE = "type"
        private const val FIELD_CATEGORY = "category"
        private const val FIELD_VALIDATED = "validated"
        private const val FIELD_CREATED_AT = "createdAt"
    }
}
