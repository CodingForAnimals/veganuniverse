package org.codingforanimals.veganuniverse.recipes.services.firebase

interface IncrementRecipeLikesService {
    suspend fun increase(id: String)
    suspend fun decrease(id: String)
}

