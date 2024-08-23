package org.codingforanimals.veganuniverse.commons.recipe.data.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.codingforanimals.veganuniverse.commons.recipe.data.RecipeFirestoreDataSource
import org.codingforanimals.veganuniverse.commons.recipe.data.RecipeFirestoreDataSource.Companion.RECIPE_EDITS_REFERENCE
import org.codingforanimals.veganuniverse.commons.recipe.data.RecipeFirestoreDataSource.Companion.RECIPE_REPORTS_REFERENCE
import org.codingforanimals.veganuniverse.commons.recipe.data.RecipeRemoteDataSource
import org.codingforanimals.veganuniverse.firebase.storage.di.firebaseStorageModule
import org.codingforanimals.veganuniverse.commons.recipe.data.mapper.RecipeFirestoreEntityMapper
import org.codingforanimals.veganuniverse.commons.recipe.data.mapper.RecipeFirestoreEntityMapperImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val recipeDataModule = module {
    includes(
        firebaseStorageModule,
    )

    factoryOf(::RecipeFirestoreEntityMapperImpl) bind RecipeFirestoreEntityMapper::class

    factory<RecipeRemoteDataSource> {
        val collectionPath = RecipeFirestoreDataSource.RECIPES_COLLECTION
        RecipeFirestoreDataSource(
            recipeCollection = FirebaseFirestore.getInstance().collection(collectionPath),
            reportsReference = FirebaseDatabase.getInstance().getReference(RECIPE_REPORTS_REFERENCE),
            editsReference = FirebaseDatabase.getInstance().getReference(RECIPE_EDITS_REFERENCE),
            firestoreEntityMapper = get(),
            uploadPictureUseCase = get(),
        )
    }
}
