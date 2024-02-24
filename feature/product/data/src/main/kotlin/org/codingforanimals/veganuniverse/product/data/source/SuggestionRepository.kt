package org.codingforanimals.veganuniverse.product.data.source

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.product.data.model.Suggestion

interface SuggestionRepository {
    suspend fun sendSuggestion(suggestion: Suggestion): String
}

internal class SuggestionFirestoreRepository(
    private val suggestionsCollection: CollectionReference,
) : SuggestionRepository {
    override suspend fun sendSuggestion(suggestion: Suggestion): String {
        return suggestionsCollection.add(suggestion).await().id
    }
}