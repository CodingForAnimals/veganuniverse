package org.codingforanimals.veganuniverse.create.presentation.place.usecase

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import java.util.concurrent.ExecutionException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetPlaceDataStatus.EstablishmentData

private const val TAG = "GetPlaceDataUseCase"

class GetPlaceDataUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val placesClient: PlacesClient,
) {

    private val ioDispatcher = coroutineDispatcherProvider.io()

    operator fun invoke(intent: Intent): Flow<GetPlaceDataStatus> = flow {
        emit(GetPlaceDataStatus.Loading)
        val place = withContext(ioDispatcher) { Autocomplete.getPlaceFromIntent(intent) }
        when {
            place.isEstablishment -> getEstablishmentData(place)
            place.isStreetAddress -> getStreetAddressData(place)
            else -> emit(GetPlaceDataStatus.PlaceTypeException)
        }
    }

    private suspend fun FlowCollector<GetPlaceDataStatus>.getEstablishmentData(place: Place) {
        try {
            val address = getAddress(place)!!
            val latLng = place.latLng!!
            val name = place.name!!
            val openingHours = place.openingHours?.weekdayText?.joinToString("\n").orEmpty()
            val establishmentData = EstablishmentData(
                latLng = latLng,
                name = name,
                address = address,
                openingHours = openingHours
            )
            emit(establishmentData)

            place.photoMetadatas?.firstOrNull()?.let { photoMetadata ->
                val photoRequest = FetchPhotoRequest.builder(photoMetadata).build()
                val task = placesClient.fetchPhoto(photoRequest)
                val bitmap = withContext(ioDispatcher) { Tasks.await(task) }.bitmap
                emit(GetPlaceDataStatus.EstablishmentPicture(bitmap))
            }

        } catch (e: Throwable) {
            when (e) {
                is ApiException,
                is ExecutionException,
                is InterruptedException -> emit(GetPlaceDataStatus.EstablishmentPictureException)
                is NullPointerException -> emit(GetPlaceDataStatus.MissingCriticalFieldException)
                else -> emit(GetPlaceDataStatus.UnknownException)
            }
        }
    }

    private suspend fun FlowCollector<GetPlaceDataStatus>.getStreetAddressData(place: Place) {
        try {
            val address = getAddress(place)!!
            val latLng = place.latLng!!
            val streetAddressData = GetPlaceDataStatus.StreetAddressData(
                latLng = latLng,
                address = address
            )
            emit(streetAddressData)
        } catch (e: Throwable) {
            when (e) {
                is NullPointerException -> emit(GetPlaceDataStatus.MissingCriticalFieldException)
                else -> emit(GetPlaceDataStatus.UnknownException)
            }
        }
    }

    private fun getAddress(place: Place): String? {
        return try {
            val components = place.addressComponents!!.asList()
            val street = components.first { it.types.contains(STREET_NAME) }.name
            val number = components.first { it.types.contains(STREET_NUMBER) }.name
            val locality = components.first { it.types.contains(LOCALITY) }.name
            return "$street $number, $locality"
        } catch (e: Throwable) {
            Log.e(TAG, "Error getting address components. Msg: ${e.stackTraceToString()}")
            null
        }
    }

    private val Place.isEstablishment: Boolean
        get() = types?.contains(Place.Type.ESTABLISHMENT) == true

    private val Place.isStreetAddress: Boolean
        get() = types?.contains(Place.Type.STREET_ADDRESS) == true

    companion object AddressComponents {
        private const val STREET_NAME = "route"
        private const val STREET_NUMBER = "street_number"
        private const val LOCALITY = "locality"
    }
}

sealed class GetPlaceDataStatus {
    object Loading : GetPlaceDataStatus()
    data class EstablishmentData(
        val latLng: LatLng,
        val name: String,
        val address: String,
        val openingHours: String,
    ) : GetPlaceDataStatus()

    data class EstablishmentPicture(
        val bitmap: Bitmap,
    ) : GetPlaceDataStatus()

    data class StreetAddressData(
        val latLng: LatLng,
        val address: String,
    ) : GetPlaceDataStatus()

    object MissingCriticalFieldException : GetPlaceDataStatus()
    object EstablishmentPictureException : GetPlaceDataStatus()
    object PlaceTypeException : GetPlaceDataStatus()
    object UnknownException : GetPlaceDataStatus()
}