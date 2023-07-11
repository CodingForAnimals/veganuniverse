package org.codingforanimals.veganuniverse.services.firebase.places.model

import org.codingforanimals.veganuniverse.services.firebase.places.model.dto.ReviewFormDTO

internal fun ReviewFormDTO.toFirebaseEntity(): ReviewFormFirebaseEntity {
    return ReviewFormFirebaseEntity(
        userId = userId,
        username = username,
        rating = rating,
        title = title,
        description = description,
    )
}