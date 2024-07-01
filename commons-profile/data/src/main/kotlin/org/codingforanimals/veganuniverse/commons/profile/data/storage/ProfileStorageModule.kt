package org.codingforanimals.veganuniverse.commons.profile.data.storage

import android.content.Context
import androidx.room.Room
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val profileStorageModule = module {
    factory {
        val appContext = get<Context>()
        Room.databaseBuilder(
            context = appContext,
            klass = ProfileDatabase::class.java,
            name = ProfileDatabase.DATABASE_NAME,
        ).build()
    }

    factory {
        val profileDatabase = get<ProfileDatabase>()
        profileDatabase.profileDao()
    }

    factoryOf(::ProfileRoomLocalDataSource) bind ProfileLocalDataSource::class
}
