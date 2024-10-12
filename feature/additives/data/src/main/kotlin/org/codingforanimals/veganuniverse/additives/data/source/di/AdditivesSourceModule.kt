package org.codingforanimals.veganuniverse.additives.data.source.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.codingforanimals.veganuniverse.additives.data.source.local.AdditivesDao
import org.codingforanimals.veganuniverse.additives.data.source.local.AdditivesDatabase
import org.codingforanimals.veganuniverse.additives.data.source.remote.AdditivesFirebaseDataSource
import org.codingforanimals.veganuniverse.additives.data.source.remote.AdditivesRemoteDataSource
import org.koin.dsl.module

internal val additivesSourceModule = module {
    factory<AdditivesRemoteDataSource> {
        AdditivesFirebaseDataSource(
            additivesReference = Firebase.database.getReference(AdditivesFirebaseDataSource.ADDITIVES_PATH),
            editsReference = Firebase.database.getReference(AdditivesFirebaseDataSource.EDITS_PATH),
        )
    }
    single<AdditivesDatabase> {
        val context = get<Context>()
        Room.databaseBuilder(
            context = context,
            klass = AdditivesDatabase::class.java,
            name = AdditivesDatabase.DATABASE_NAME,
        ).fallbackToDestructiveMigration().build()
    }
    factory<AdditivesDao> {
        val database = get<AdditivesDatabase>()
        database.getAdditivesDao()
    }
}
