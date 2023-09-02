package org.codingforanimals.veganuniverse.services.firebase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

/**
 * Ip address for Firebase emulators when running an Android emulator.
 * If running on real device instead, you need to do port-forwarding.
 */

object FirebaseConfig {
    private const val FIREBASE_EMULATORS_IP = "10.0.2.2"
    private const val PORT_RTDB = 9000
    private const val PORT_FIRESTORE = 8080
    private const val PORT_AUTH = 9099
    private const val PORT_STORAGE = 9199
    fun useEmulators() {
        Firebase.database.useEmulator(FIREBASE_EMULATORS_IP, PORT_RTDB)
        Firebase.firestore.useEmulator(FIREBASE_EMULATORS_IP, PORT_FIRESTORE)
        Firebase.auth.useEmulator(FIREBASE_EMULATORS_IP, PORT_AUTH)
        Firebase.storage.useEmulator(FIREBASE_EMULATORS_IP, PORT_STORAGE)
    }
}