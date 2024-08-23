package org.codingforanimals.veganuniverse.user.data.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.codingforanimals.veganuniverse.user.data.source.UserFirestoreRemoteDataSource
import org.codingforanimals.veganuniverse.user.data.source.UserRemoteDataSource
import org.codingforanimals.veganuniverse.user.data.storage.UserDataStore
import org.codingforanimals.veganuniverse.user.data.storage.UserLocalStorage
import org.codingforanimals.veganuniverse.user.data.storage.userDataStore
import org.koin.dsl.module

val userDataModule = module {
    factory<UserRemoteDataSource> {
        UserFirestoreRemoteDataSource(
            firestore = FirebaseFirestore.getInstance(),
            auth = FirebaseAuth.getInstance(),
        )
    }

    factory<UserLocalStorage> {
        val appContext: Context = get()
        UserDataStore(dataStore = appContext.userDataStore)
    }
}