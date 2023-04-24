package org.codingforanimals.veganuniverse.create.domain

import org.codingforanimals.veganuniverse.create.domain.place.PlaceFormDomainEntity

interface ContentCreator {
    suspend fun createPlace(form: PlaceFormDomainEntity): Result<Unit>
}