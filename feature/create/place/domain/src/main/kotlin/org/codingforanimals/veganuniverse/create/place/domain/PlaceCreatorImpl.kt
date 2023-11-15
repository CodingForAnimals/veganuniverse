package org.codingforanimals.veganuniverse.create.place.domain

import android.util.Log
import org.codingforanimals.veganuniverse.create.place.domain.model.CreatePlaceResult
import org.codingforanimals.veganuniverse.places.entity.PlaceForm
import org.codingforanimals.veganuniverse.places.services.firebase.PlacesService
import org.codingforanimals.veganuniverse.profile.model.SaveableContentType
import org.codingforanimals.veganuniverse.profile.model.SaveableType
import org.codingforanimals.veganuniverse.profile.services.firebase.ProfileLookupsService

private const val TAG = "PlaceCreatorImpl"

internal class PlaceCreatorImpl(
    private val api: PlacesService,
    private val profileLookupsService: ProfileLookupsService,
) : PlaceCreator {

    override suspend fun submitPlace(form: PlaceForm): CreatePlaceResult {
        return try {
            if (api.fetchPlace(form.latitude, form.longitude) != null) {
                CreatePlaceResult.Exception.PlaceAlreadyExists
            } else {
                val geoHash = api.uploadPlace(form)
                profileLookupsService.saveContent(
                    contentId = geoHash,
                    saveableType = SaveableType.CONTRIBUTION,
                    contentType = SaveableContentType.PLACE,
                    userId = form.userId,
                )
                CreatePlaceResult.Success
            }
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            CreatePlaceResult.Exception.UnknownException
        }
    }
}