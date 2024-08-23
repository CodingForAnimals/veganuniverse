package org.codingforanimals.veganuniverse.recipe.data.storage.di

import android.content.Context
import androidx.room.Room
import org.codingforanimals.veganuniverse.recipe.data.storage.RecipeLocalDataSource
import org.codingforanimals.veganuniverse.recipe.data.storage.RecipeRoomDataSource
import org.codingforanimals.veganuniverse.recipe.data.storage.database.RecipeDatabase
import org.codingforanimals.veganuniverse.recipe.data.storage.model.RecipeRoomEntityMapper
import org.codingforanimals.veganuniverse.recipe.data.storage.model.RecipeRoomEntityMapperImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val recipeDataStorageModule = module {
    factory {
        val appContext: Context = get()
        Room.databaseBuilder(
            context = appContext,
            klass = RecipeDatabase::class.java,
            name = RecipeDatabase.DATABASE_NAME,
        ).build()
    }

    factory {
        val recipeDatabase: RecipeDatabase = get()
        recipeDatabase.recipeDao()
    }

    factoryOf(::RecipeRoomEntityMapperImpl) bind RecipeRoomEntityMapper::class

    factoryOf(::RecipeRoomDataSource) bind RecipeLocalDataSource::class
}