package org.codingforanimals.veganuniverse.product.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

abstract class Suggestion(
    open val userId: String,
    val type: String,
    open val userMessage: String,
    @ServerTimestamp val createdAt: Timestamp? = null,
) {
    data class Report(
        override val userId: String,
        override val userMessage: String,
    ) : Suggestion(
        userId = userId,
        userMessage = userMessage,
        type = REPORT_TYPE,
    )

    data class Edit(
        override val userId: String,
        override val userMessage: String,
    ) : Suggestion(
        userId = userId,
        userMessage = userMessage,
        type = EDIT_TYPE,
    )


    companion object {
        private const val REPORT_TYPE = "REPORT"
        private const val EDIT_TYPE = "EDIT"
    }
}