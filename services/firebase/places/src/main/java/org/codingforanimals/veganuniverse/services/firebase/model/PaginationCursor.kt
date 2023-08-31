package org.codingforanimals.veganuniverse.services.firebase.model

import com.google.firebase.firestore.DocumentSnapshot

sealed class PaginationCursor {
    data object FreshStart : PaginationCursor()
    data class Current(val cursor: DocumentSnapshot) : PaginationCursor()
    data object Finished : PaginationCursor()
}