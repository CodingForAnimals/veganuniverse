package org.codingforanimals.veganuniverse.services.firebase.places.model

import com.google.firebase.firestore.DocumentSnapshot

sealed class PaginationCursor {
    object FreshStart : PaginationCursor()
    data class Current(val cursor: DocumentSnapshot) : PaginationCursor()
    object Finished : PaginationCursor()
}