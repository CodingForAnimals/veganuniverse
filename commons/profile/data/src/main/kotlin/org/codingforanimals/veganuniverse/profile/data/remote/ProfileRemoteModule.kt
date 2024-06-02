package org.codingforanimals.veganuniverse.profile.data.remote

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.koin.dsl.module

internal val profileRemoteModule = module {
    factory { FirebaseDatabase.getInstance().getReference(FIREBASE_PROFILES_PATH) }

    factory<ProfileRemoteDataSource> {
        val profilesReference: DatabaseReference = get()
        ProfileFirebaseRemoteDataSource(
            profilesReference = profilesReference,
        )
    }
}

private const val FIREBASE_PROFILES_PATH = "profiles"
