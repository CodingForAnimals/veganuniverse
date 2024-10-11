package org.codingforanimals.veganuniverse.additives.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AdditiveEntity::class], version = 3)
internal abstract class AdditivesDatabase : RoomDatabase() {
    abstract fun getAdditivesDao(): AdditivesDao

    companion object {
        const val DATABASE_NAME = "additives-database"
    }
}