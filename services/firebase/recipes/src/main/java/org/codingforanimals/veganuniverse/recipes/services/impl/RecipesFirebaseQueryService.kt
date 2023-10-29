package org.codingforanimals.veganuniverse.recipes.services.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.entity.RecipeQueryParams
import org.codingforanimals.veganuniverse.recipes.entity.RecipeSorter
import org.codingforanimals.veganuniverse.recipes.services.RecipesQueryService
import org.codingforanimals.veganuniverse.recipes.services.entity.RecipeFirebaseEntity
import org.codingforanimals.veganuniverse.services.firebase.FirestoreCollection
import org.codingforanimals.veganuniverse.services.firebase.FirestoreFields
import org.codingforanimals.veganuniverse.storage.firestore.DocumentSnapshotCache

private const val TAG = "FetchRecipesFirebaseSer"

internal class RecipesFirebaseQueryService(
    private val firestore: FirebaseFirestore,
    private val recipeMapper: OneWayEntityMapper<RecipeFirebaseEntity, Recipe>,
    private val recipeCache: DocumentSnapshotCache,
) : RecipesQueryService {
    override suspend fun invoke(params: RecipeQueryParams): List<Recipe> {
        var query = firestore
            .collection(FirestoreCollection.Content.Recipes.ITEMS)
            .limit(params.limit)

        query = params.title?.let { title ->
            query
                .orderBy(FirestoreFields.Recipes.TITLE)
                .whereGreaterThanOrEqualTo(FirestoreFields.Recipes.TITLE, title)
                .whereLessThanOrEqualTo(FirestoreFields.Recipes.TITLE, "$title\uf8ff")
        } ?: query
            .orderBy(getOrderByField(params.sorter), Query.Direction.DESCENDING)

        params.tag?.let { tag ->
            query = query.whereArrayContains(FirestoreFields.TAGS, tag)
        }

        params.lastRecipe?.id?.let { lastRecipeId ->
            recipeCache.getRecipe(lastRecipeId)?.let { lastRecipeDocumentSnapshot ->
                query = query.startAfter(lastRecipeDocumentSnapshot)
            }
        }

        val result = query.get().await()
            .mapNotNull { queryDocumentSnapshot ->
                try {
                    val entity = queryDocumentSnapshot.toObject(RecipeFirebaseEntity::class.java)
                    val mapped = recipeMapper.map(entity)
                    recipeCache.putRecipe(queryDocumentSnapshot)
                    mapped
                } catch (e: Throwable) {
                    Log.e(TAG, e.stackTraceToString())
                    null
                }
            }
        return result
    }

    private fun getOrderByField(orderBy: RecipeSorter): String {
        return when (orderBy) {
            RecipeSorter.DATE -> FirestoreFields.CREATED_AT
            RecipeSorter.LIKES -> FirestoreFields.Recipes.LIKES
        }
    }
}