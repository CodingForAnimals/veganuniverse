package org.codingforanimals.veganuniverse.profile.domain.impl

import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.services.PlacesService
import org.codingforanimals.veganuniverse.profile.domain.ContributionsRepository
import org.codingforanimals.veganuniverse.profile.domain.model.Contributions
import org.codingforanimals.veganuniverse.profile.services.firebase.ProfileLookupsService
import org.codingforanimals.veganuniverse.profile.services.firebase.model.SaveableContentType
import org.codingforanimals.veganuniverse.profile.services.firebase.model.SaveableType

internal class ContributionsRepositoryImpl(
    private val profileLookupsService: ProfileLookupsService,
    private val placesService: PlacesService,
) : ContributionsRepository {

    override suspend fun getContributions(userId: String): Contributions {
        val placesIds = profileLookupsService.getContentSavedByUser(
            saveableType = SaveableType.CONTRIBUTION,
            contentType = SaveableContentType.PLACE,
            userId = userId,
        )
        return Contributions(
            placesIds = placesIds
        )
    }

    override suspend fun getContributedPlaces(placesIds: List<String>): List<Place> {
        return if (placesIds.isNotEmpty()) placesService.fetchPlaces(placesIds) else emptyList()
    }
}