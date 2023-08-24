package org.codingforanimals.veganuniverse.create.domain.places

import android.util.Log
import org.codingforanimals.veganuniverse.create.domain.places.model.CreatePlaceResult
import org.codingforanimals.veganuniverse.places.entity.PlaceForm
import org.codingforanimals.veganuniverse.services.firebase.api.PlacesApi

private const val TAG = "PlaceCreatorImpl"

internal class PlaceCreatorImpl(
    private val api: PlacesApi,
) : PlaceCreator {

    override suspend fun submitPlace(form: PlaceForm): CreatePlaceResult {
        return try {
            if (api.fetchPlace(form.latitude, form.longitude) != null) {
                CreatePlaceResult.Exception.PlaceAlreadyExists
            } else {
                api.uploadPlace(form)
                CreatePlaceResult.Success
            }
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            CreatePlaceResult.Exception.UnknownException
        }
    }
}