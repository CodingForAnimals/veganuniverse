package org.codingforanimals.veganuniverse.services.firebase.di

import com.google.firebase.firestore.FirebaseFirestore
import org.codingforanimals.veganuniverse.services.firebase.places.di.placesFirebaseModule
import org.koin.dsl.module

val firebaseServiceModule = module {
    factory { FirebaseFirestore.getInstance() }
    includes(placesFirebaseModule)
}