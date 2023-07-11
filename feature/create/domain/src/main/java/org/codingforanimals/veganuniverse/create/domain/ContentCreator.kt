package org.codingforanimals.veganuniverse.create.domain

import org.codingforanimals.veganuniverse.create.domain.model.PlaceFormDomainEntity

interface ContentCreator {
    suspend fun createPlace(form: PlaceFormDomainEntity): Result<Unit>
}