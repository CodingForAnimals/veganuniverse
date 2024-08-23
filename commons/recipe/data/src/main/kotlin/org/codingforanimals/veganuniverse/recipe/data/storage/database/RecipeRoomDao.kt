package org.codingforanimals.veganuniverse.recipe.data.storage.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface RecipeRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(vararg recipe: RecipeRoomEntity)

    @Query("SELECT * FROM RecipeRoomEntity WHERE id = :id")
    suspend fun getById(id: String): RecipeRoomEntity?

    @Query("SELECT * FROM RecipeRoomEntity WHERE id IN (:ids)")
    suspend fun getByIdList(ids: List<String>): List<RecipeRoomEntity>

    @Query("DELETE FROM RecipeRoomEntity WHERE id = :id")
    suspend fun deleteById(id: String): Int
}
