package org.codingforanimals.veganuniverse.recipe.data.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.codingforanimals.veganuniverse.firebase.storage.di.firebaseStorageModule
import org.codingforanimals.veganuniverse.recipe.data.mapper.RecipeFirestoreEntityMapper
import org.codingforanimals.veganuniverse.recipe.data.mapper.RecipeFirestoreEntityMapperImpl
import org.codingforanimals.veganuniverse.recipe.data.source.RecipeFirestoreDataSource
import org.codingforanimals.veganuniverse.recipe.data.source.RecipeFirestoreDataSource.Companion.RECIPE_EDITS_REFERENCE
import org.codingforanimals.veganuniverse.recipe.data.source.RecipeFirestoreDataSource.Companion.RECIPE_REPORTS_REFERENCE
import org.codingforanimals.veganuniverse.recipe.data.source.RecipeRemoteDataSource
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
            uploadPicture = get(),
        )
    }
}
