package org.codingforanimals.veganuniverse.places.domain

import org.codingforanimals.veganuniverse.entity.PaginatedResponse
import org.codingforanimals.veganuniverse.places.entity.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.PlaceCard
import org.codingforanimals.veganuniverse.places.entity.PlaceReview
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm
import org.codingforanimals.veganuniverse.places.services.firebase.FetchPlaceService
import org.codingforanimals.veganuniverse.places.services.firebase.PlaceReviewsService
import org.codingforanimals.veganuniverse.places.services.firebase.PlacesService
import org.codingforanimals.veganuniverse.profile.model.SaveableContentType
import org.codingforanimals.veganuniverse.profile.model.SaveableType
import org.codingforanimals.veganuniverse.profile.services.firebase.ProfileLookupsService

class PlacesRepositoryImpl(
    private val placesService: PlacesService,
    private val placeReviewsService: PlaceReviewsService,
    private val profileLookupsService: ProfileLookupsService,
    private val fetchPlaceService: FetchPlaceService,
) : PlacesRepository {

    override suspend fun getPlaces(params: GeoLocationQueryParams): List<PlaceCard> {
        return placesService.fetchPlacesCards(params)
    }

    override suspend fun getPlace(id: String): Place? {
        return fetchPlaceService.byId(id)
    }

    override suspend fun getReview(placeId: String, userId: String): PlaceReview? {
        return placeReviewsService.fetchReview(placeId, userId)
    }

    override suspend fun getReviews(placeId: String): PaginatedResponse<PlaceReview> {
        val response = placeReviewsService.fetchReviews(placeId)
        return PaginatedResponse(
            content = response.content,
            hasMoreItems = response.hasMoreItems,
        )
    }

    override suspend fun submitReview(
        placeId: String,
        placeReviewForm: PlaceReviewForm,
    ): PlaceReview {
        return placeReviewsService.submitReview(placeId, placeReviewForm)
    }

    override suspend fun deleteReview(placeId: String, placeReview: PlaceReview) {
        placeReviewsService.deleteReview(placeId, placeReview)
    }

    override suspend fun bookmarkPlaceReturningCurrent(placeId: String, userId: String): Boolean {
        profileLookupsService.saveContent(
            contentId = placeId,
            saveableType = SaveableType.BOOKMARK,
            contentType = SaveableContentType.PLACE,
            userId = userId,
        )
        return true
    }

    override suspend fun unbookmarkPlaceReturningCurrent(placeId: String, userId: String): Boolean {
        profileLookupsService.removeContent(
            contentId = placeId,
            saveableType = SaveableType.BOOKMARK,
            contentType = SaveableContentType.PLACE,
            userId = userId,
        )
        return false
    }

    override suspend fun isPlaceBookmarkedByUser(placeId: String, userId: String): Boolean {
        return profileLookupsService.isContentSavedByUser(
            contentId = placeId,
            saveableType = SaveableType.BOOKMARK,
            contentType = SaveableContentType.PLACE,
            userId = userId,
        )
    }
}