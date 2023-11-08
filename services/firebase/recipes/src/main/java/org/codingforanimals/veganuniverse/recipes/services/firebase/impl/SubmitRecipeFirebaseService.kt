package org.codingforanimals.veganuniverse.recipes.services.firebase.impl

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.asDeferred
import org.codingforanimals.veganuniverse.recipes.entity.RecipeForm
import org.codingforanimals.veganuniverse.recipes.services.firebase.SubmitRecipeService
import org.codingforanimals.veganuniverse.recipes.services.firebase.entity.RecipeFirebaseEntity
import org.codingforanimals.veganuniverse.services.firebase.DatabasePath
import org.codingforanimals.veganuniverse.services.firebase.FirebaseImageResizer
import org.codingforanimals.veganuniverse.services.firebase.FirestoreCollection
import org.codingforanimals.veganuniverse.services.firebase.storageImageMetadata

internal class SubmitRecipeFirebaseService(
    private val firestore: FirebaseFirestore,
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage,
) : SubmitRecipeService {
    override suspend fun invoke(recipeForm: RecipeForm): String {
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
        return documentReference.id
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