package org.codingforanimals.veganuniverse.services.firebase.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

val firebaseServiceModule = module {
    factory { FirebaseFirestore.getInstance() }
    factory { Firebase.auth }
}