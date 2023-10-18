package org.codingforanimals.veganuniverse.recipes.api

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.util.LruCache
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.recipes.api.entity.RecipeFirebaseEntity
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.entity.RecipeForm
import org.codingforanimals.veganuniverse.recipes.entity.RecipeQueryParams
import org.codingforanimals.veganuniverse.recipes.entity.RecipeSorter
import org.codingforanimals.veganuniverse.services.firebase.DatabasePath
import org.codingforanimals.veganuniverse.services.firebase.FirebaseImageResizer
import org.codingforanimals.veganuniverse.services.firebase.FirestoreCollection
import org.codingforanimals.veganuniverse.services.firebase.FirestoreFields
import org.codingforanimals.veganuniverse.services.firebase.storageImageMetadata

interface DocumentSnapshotCache {
    fun getRecipe(id: String): DocumentSnapshot?
    fun putRecipe(recipe: DocumentSnapshot): Boolean
}

internal class DocumentSnapshotLruCache : LruCache<String, DocumentSnapshot>(4 * 1024 * 1024),
    DocumentSnapshotCache {
    override fun getRecipe(id: String): DocumentSnapshot? {
        return try {
            get(id)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            null
        }
    }

    override fun putRecipe(recipe: DocumentSnapshot): Boolean {
        return try {
            put(recipe.id, recipe)
            true
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            false
        }
    }
}

private const val TAG = "RecipesFirebaseApi"

internal class RecipesFirebaseApi(
    private val firestore: FirebaseFirestore,
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage,
    private val recipeMapper: OneWayEntityMapper<RecipeFirebaseEntity, Recipe>,
    private val recipeCache: DocumentSnapshotCache,
) : RecipesApi {

    override suspend fun submitRecipe(recipeForm: RecipeForm) {
        val documentReference = firestore
            .collection(FirestoreCollection.Content.Recipes.ITEMS)
            .document()

        val recipeDef = documentReference
            .set(recipeForm.toFirebaseEntity())
            .asDeferred()

        val userRecipeLookupDef = database
            .getReference(DatabasePath.Content.Recipes.userRecipesLookup(recipeForm.userId))
            .child(documentReference.id)
            .setValue(true)
            .asDeferred()

        val pictureRef = storage.getReference(
            FirebaseImageResizer.getRecipePictureToResizePath(documentReference.id),
        )
        val recipePictureDef = when (val model = recipeForm.image) {
            is Bitmap -> {
                val bytes = ByteArrayOutputStream()
                model.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                pictureRef.putBytes(
                    bytes.toByteArray(),
                    storageImageMetadata,
                ).asDeferred()
            }

            is Uri -> {
                pictureRef.putFile(
                    model,
                    storageImageMetadata
                ).asDeferred()
            }

            else -> {
                throw RuntimeException("Unsupported image format")
            }
        }

        awaitAll(recipeDef, userRecipeLookupDef, recipePictureDef)
    }

    override suspend fun fetchRecipes(params: RecipeQueryParams): List<Recipe> {
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

    override suspend fun fetchRecipe(id: String): Recipe? {
        val docSnap = firestore
            .collection(FirestoreCollection.Content.Recipes.ITEMS)
            .document(id)
            .get().await()
        return try {
            recipeMapper.map(docSnap.toObject(RecipeFirebaseEntity::class.java)!!)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            null
        }
    }

    private fun getOrderByField(orderBy: RecipeSorter): String {
        return when (orderBy) {
            RecipeSorter.DATE -> FirestoreFields.CREATED_AT
            RecipeSorter.LIKES -> FirestoreFields.Recipes.LIKES
        }
    }

    private fun RecipeForm.toFirebaseEntity(): RecipeFirebaseEntity {
        return RecipeFirebaseEntity(
            userId = userId,
            username = username,
            title = title,
            description = description,
            tags = tags,
            ingredients = ingredients,
            steps = steps,
            prepTime = prepTime,
            servings = servings,
        )
    }
}

