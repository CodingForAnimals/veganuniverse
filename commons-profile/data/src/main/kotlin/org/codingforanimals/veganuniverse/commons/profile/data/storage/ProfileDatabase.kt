package org.codingforanimals.veganuniverse.commons.profile.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProfileContent::class], version = 1)
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao

    companion object {
        const val DATABASE_NAME = "profile-database"
    }
}