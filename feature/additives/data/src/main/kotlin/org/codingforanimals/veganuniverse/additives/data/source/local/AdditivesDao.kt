package org.codingforanimals.veganuniverse.additives.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.codingforanimals.veganuniverse.additives.data.source.local.model.AdditiveEntity

@Dao
interface AdditivesDao {
    @Insert
    suspend fun insertAdditive(vararg additive: AdditiveEntity)

    @Query("DELETE FROM AdditiveEntity")
    suspend fun clearAdditives()

    @Query("SELECT * FROM AdditiveEntity")
    suspend fun getAdditives(): List<AdditiveEntity>

    @Query("SELECT * FROM AdditiveEntity WHERE codeAccentInsensitive LIKE '%' || :query || '%' OR nameAccentInsensitive LIKE '%' || :query || '%'")
    suspend fun queryAdditives(query: String): List<AdditiveEntity>

    @Query("SELECT * FROM AdditiveEntity WHERE id = :id")
    suspend fun getById(id: String): AdditiveEntity
}
