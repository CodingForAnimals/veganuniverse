package org.codingforanimals.veganuniverse.recipe.data.remote.di

import com.google.firebase.firestore.FirebaseFirestore
import org.codingforanimals.veganuniverse.firebase.storage.di.KOIN_STORAGE_BUCKET
import org.codingforanimals.veganuniverse.recipe.data.remote.RecipeFirestoreDataSource
import org.codingforanimals.veganuniverse.recipe.data.remote.RecipeRemoteDataSource
import org.codingforanimals.veganuniverse.recipe.data.remote.mapper.RecipeFirestoreEntityMapper
import org.codingforanimals.veganuniverse.recipe.data.remote.mapper.RecipeFirestoreEntityMapperImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

internal val recipeDataRemoteModule = module {
    factoryOf(::RecipeFirestoreEntityMapperImpl) bind RecipeFirestoreEntityMapper::class

    factory<RecipeRemoteDataSource> {
        val collectionPath = RecipeFirestoreDataSource.RECIPES_COLLECTION
        RecipeFirestoreDataSource(
            recipeCollection = FirebaseFirestore.getInstance().collection(collectionPath),
            firestoreEntityMapper = get(),
            uploadPictureUseCase = get(),
        )
    }
}
