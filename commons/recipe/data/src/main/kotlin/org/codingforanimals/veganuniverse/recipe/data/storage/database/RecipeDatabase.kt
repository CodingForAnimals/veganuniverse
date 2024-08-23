package org.codingforanimals.veganuniverse.recipe.data.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RecipeRoomEntity::class], version = 1)
@TypeConverters(ListConverter::class, DateConverter::class)
internal abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeRoomDao

    companion object {
        internal const val DATABASE_NAME = "recipe-database"
    }
}