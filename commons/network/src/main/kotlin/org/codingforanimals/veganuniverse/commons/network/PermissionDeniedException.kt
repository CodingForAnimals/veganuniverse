package org.codingforanimals.veganuniverse.commons.network

import com.google.firebase.firestore.FirebaseFirestoreException

class PermissionDeniedException(e: Throwable) : Exception(e)

fun mapFirestoreExceptions(e: FirebaseFirestoreException): Exception {
    return if (e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
        PermissionDeniedException(e)
    } else {
        e
    }
}
