package org.codingforanimals.veganuniverse.places.services.firebase.model

import com.google.firebase.firestore.DocumentSnapshot

sealed class PaginationCursor {
    object FreshStart : PaginationCursor()
    data class Current(val cursor: DocumentSnapshot) : PaginationCursor()
    object Finished : PaginationCursor()
}