package org.codingforanimals.veganuniverse.commons.recipe.data.remote

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
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.commons.network.mapFirestoreExceptions
import org.codingforanimals.veganuniverse.firebase.storage.model.ResizeResolution
import org.codingforanimals.veganuniverse.firebase.storage.usecase.UploadPictureUseCase
import org.codingforanimals.veganuniverse.commons.recipe.data.model.getSortingField
import org.codingforanimals.veganuniverse.commons.recipe.data.paging.RecipePagingSource
import org.codingforanimals.veganuniverse.commons.recipe.data.remote.entity.RecipeFirestoreEntity
import org.codingforanimals.veganuniverse.commons.recipe.data.remote.mapper.RecipeFirestoreEntityMapper
import org.codingforanimals.veganuniverse.commons.recipe.data.remote.mapper.toNewFirestoreEntity
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeQueryParams

internal class RecipeFirestoreDataSource(
    private val recipeCollection: CollectionReference,
    private val reportsReference: DatabaseReference,
    private val editsReference: DatabaseReference,
    private val firestoreEntityMapper: RecipeFirestoreEntityMapper,
    private val uploadPictureUseCase: UploadPictureUseCase,
) : RecipeRemoteDataSource {

    override suspend fun getRecipeById(id: String): Recipe? {
        return runCatching {
            val firestoreEntity = recipeCollection
                .document(id)
                .get().await()
                .toObject(RecipeFirestoreEntity::class.java)
                ?: return null
            return firestoreEntityMapper.mapToDataModel(firestoreEntity)
        }.onFailure { Log.e(TAG, it.stackTraceToString()) }.getOrNull()
    }

    override suspend fun getRecipesByIdList(ids: List<String>): List<Recipe> =
        coroutineScope {
            val dataModelsDeferred = ids.map { async { getRecipeById(it) } }
            awaitAll(*dataModelsDeferred.toTypedArray()).filterNotNull()
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

        query = title?.lowercase()?.let { lowercaseTitle ->
            query
                .orderBy(FIELD_LOWERCASE_TITLE)
                .whereGreaterThanOrEqualTo(FIELD_LOWERCASE_TITLE, lowercaseTitle)
                .whereLessThanOrEqualTo(FIELD_LOWERCASE_TITLE, "$lowercaseTitle\uf8ff")
        } ?: query.orderBy(sorter.getSortingField, Query.Direction.DESCENDING)

        tag?.let { tag ->
            query = query.whereArrayContains(FIELD_TAGS, tag.name)
        }

        return query
    }

    override suspend fun insertRecipe(recipe: Recipe, model: Parcelable): Recipe? {
        return try {
            val pictureId = uploadPictureUseCase(
                fileFolderPath = BASE_RECIPE_PICTURE_PATH,
                model = model,
            )
            val resizedPictureId = pictureId + ResizeResolution.MEDIUM.suffix

            val entity = recipe.toNewFirestoreEntity(resizedPictureId)
            val docRef = recipeCollection.document()
            val docId = docRef.id
            docRef.set(entity).await()
            val newEntity = entity.copy(id = docId)
            firestoreEntityMapper.mapToDataModel(newEntity)
        } catch (e: FirebaseFirestoreException) {
            throw mapFirestoreExceptions(e)
        }
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
        private const val FIELD_LOWERCASE_TITLE = "lowercaseTitle"
        private const val FIELD_TAGS = "tags"
        private const val FIELD_LIKES = "likes"
    }
}