package org.codingforanimals.veganuniverse.commons.recipe.data

import android.os.Parcelable
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.commons.data.paging.ContentListingPagingSource
import org.codingforanimals.veganuniverse.commons.recipe.data.mapper.RecipeFirestoreEntityMapper
import org.codingforanimals.veganuniverse.commons.recipe.data.mapper.toNewFirestoreEntity
import org.codingforanimals.veganuniverse.commons.recipe.data.model.RecipeFirestoreEntity
import org.codingforanimals.veganuniverse.commons.recipe.data.model.getSortingDirection
import org.codingforanimals.veganuniverse.commons.recipe.data.model.getSortingField
import org.codingforanimals.veganuniverse.commons.recipe.data.paging.RecipePagingSource
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeQueryParams
import org.codingforanimals.veganuniverse.firebase.storage.model.ResizeResolution
import org.codingforanimals.veganuniverse.firebase.storage.usecase.UploadPictureUseCase

internal class RecipeFirestoreDataSource(
    private val recipeCollection: CollectionReference,
    private val reportsReference: DatabaseReference,
    private val editsReference: DatabaseReference,
    private val firestoreEntityMapper: RecipeFirestoreEntityMapper,
    private val uploadPictureUseCase: UploadPictureUseCase,
) : RecipeRemoteDataSource {

    override suspend fun getRecipeById(id: String): Recipe? {
        val firestoreEntity = recipeCollection
            .document(id)
            .get().await()
            .toObject(RecipeFirestoreEntity::class.java)
            ?: return null
        return firestoreEntityMapper.mapToDataModel(firestoreEntity)
    }

    override suspend fun queryRecipesPagingDataByIds(ids: List<String>): Flow<PagingData<Recipe>> {
        suspend fun query(ids: List<String>): List<RecipeFirestoreEntity> = coroutineScope {
            val deferreds = mutableListOf<Deferred<RecipeFirestoreEntity?>>()
            ids.forEach { recipeId ->
                val deferred = async {
                    recipeCollection.document(recipeId).get().await()
                        .toObject(RecipeFirestoreEntity::class.java)
                }
                deferreds.add(deferred)
            }
            return@coroutineScope deferreds.awaitAll().filterNotNull()
        }
        return Pager(
            config = PagingConfig(pageSize = 2),
            pagingSourceFactory = {
                ContentListingPagingSource(
                    ids = ids,
                    pageSize = 2,
                    query = { query(it) },
                )
            }
        ).flow.map { pagingData -> pagingData.map { firestoreEntityMapper.mapToDataModel(it) } }
    }

    override fun queryRecipesPagingData(params: RecipeQueryParams): Flow<PagingData<Recipe>> {
        val query = params.getQuery()
        val pagingSource = RecipePagingSource(query)
        return Pager(
            config = PagingConfig(
                pageSize = params.pageSize,
                maxSize = params.maxSize,
            ),
            pagingSourceFactory = { pagingSource },
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                firestoreEntityMapper.mapToDataModel(entity)
            }
        }
    }

    override suspend fun getRecipeByQueryParams(params: RecipeQueryParams): List<Recipe> {
        return params.getQuery().get().await().toObjects(RecipeFirestoreEntity::class.java)
            .map { firestoreEntityMapper.mapToDataModel(it) }
    }

    private fun RecipeQueryParams.getQuery(): Query {
        var query = recipeCollection.limit(pageSize.toLong())

        query = name?.lowercase()?.let { nameLowercase ->
            query
                .orderBy(FIELD_NAME_LOWERCASE)
                .whereGreaterThanOrEqualTo(FIELD_NAME_LOWERCASE, nameLowercase)
                .whereLessThanOrEqualTo(FIELD_NAME_LOWERCASE, "$nameLowercase\uf8ff")
        } ?: query.orderBy(sorter.getSortingField, sorter.getSortingDirection)

        tag?.let { tag ->
            query = query.whereArrayContains(FIELD_TAGS, tag.name)
        }

        query = query.whereEqualTo(FIELD_VALIDATED, true)

        return query
    }

    override suspend fun insertRecipe(recipe: Recipe, model: Parcelable): String {
        val pictureId = uploadPictureUseCase(
            fileFolderPath = BASE_RECIPE_PICTURE_PATH,
            model = model,
        )
        val resizedPictureId = pictureId + ResizeResolution.MEDIUM.suffix

        val entity = recipe.toNewFirestoreEntity(resizedPictureId)
        val docRef = recipeCollection.document()
        val docId = docRef.id
        docRef.set(entity).await()
        return docId
    }

    override suspend fun deleteRecipeById(id: String): Boolean {
        return runCatching {
            recipeCollection.document(id).delete().await()
            true
        }.onFailure { Log.e(TAG, it.stackTraceToString()) }.getOrElse { false }
    }

    override suspend fun increaseOrDecreaseLike(recipeId: String, shouldIncrease: Boolean) {
        val recipeDocRef = recipeCollection.document(recipeId)
        FirebaseFirestore.getInstance().runTransaction { transaction ->
            val recipe = transaction.get(recipeDocRef).toObject(RecipeFirestoreEntity::class.java)
            val currentLikes = recipe!!.likes
            val incrementValue = if (shouldIncrease) {
                1L
            } else {
                if (currentLikes <= 0) throw IllegalStateException("Trying to decrease likes when current likes are '$currentLikes'")
                else -1L
            }
            transaction.update(recipeDocRef, FIELD_LIKES, FieldValue.increment(incrementValue))
        }.await()
    }

    override suspend fun reportRecipe(recipeId: String, userId: String) {
        reportsReference.child(recipeId).child(userId).setValue(true).await()
    }

    override suspend fun editRecipe(recipeId: String, userId: String, suggestion: String) {
        editsReference.child(recipeId).child(userId).setValue(suggestion).await()
    }

    companion object {
        internal const val RECIPES_COLLECTION = "content/recipes/items"
        internal const val RECIPE_REPORTS_REFERENCE = "content/recipes/reports"
        internal const val RECIPE_EDITS_REFERENCE = "content/recipes/edits"
        internal const val BASE_RECIPE_PICTURE_PATH = "content/recipes/picture"
        private const val TAG = "RecipeFirestoreDataSour"
        private const val FIELD_TAGS = "tags"
        internal const val FIELD_NAME_LOWERCASE = "nameLowercase"
        internal const val FIELD_LIKES = "likes"
        internal const val FIELD_CREATED_AT = "createdAt"
        internal const val FIELD_VALIDATED = "validated"
    }
}