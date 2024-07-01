package org.codingforanimals.veganuniverse.commons.recipe.data.remote.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.codingforanimals.veganuniverse.commons.recipe.data.remote.RecipeFirestoreDataSource
import org.codingforanimals.veganuniverse.commons.recipe.data.remote.RecipeFirestoreDataSource.Companion.RECIPE_EDITS_REFERENCE
import org.codingforanimals.veganuniverse.commons.recipe.data.remote.RecipeFirestoreDataSource.Companion.RECIPE_REPORTS_REFERENCE
import org.codingforanimals.veganuniverse.commons.recipe.data.remote.RecipeRemoteDataSource
import org.codingforanimals.veganuniverse.commons.recipe.data.remote.mapper.RecipeFirestoreEntityMapper
import org.codingforanimals.veganuniverse.commons.recipe.data.remote.mapper.RecipeFirestoreEntityMapperImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val recipeDataRemoteModule = module {
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
