package org.codingforanimals.veganuniverse.create.presentation.place.usecase

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.model.DayOfWeek
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
import org.codingforanimals.veganuniverse.create.presentation.R

private const val TAG = "GetPlaceDataUseCase"

class GetPlaceDataUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val context: Context,
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

    private suspend fun FlowCollector<GetPlaceDataStatus>.getEstablishmentData(place: Place) = try {
        place.addressComponents!!.asList().map { it.name }
        with(place.addressComponents!!.asList()) {
            val streetName = first { it.types.contains(STREET_NAME) }.name
            val streetNumber = first { it.types.contains(STREET_NUMBER) }.name
            val locality = first { it.types.contains(LOCALITY) }.name
            val province = first { it.types.contains("administrative_area_level_2") }.name
            val country = first { it.types.contains(COUNTRY) }.name
            val latLng = place.latLng!!
            val name = place.name!!

            //TODO this needs correction when the design for opening hours are done
            val openingHours = mutableListOf<String>()
            place.openingHours?.periods?.forEach { period ->
                Log.e("pepe", "$period")
                val day = period.open?.day?.getDayName(context) ?: return@forEach
                val openingHour = period.open?.time?.hours ?: return@forEach
                var openingMinutes = period.open?.time?.minutes?.toString() ?: return@forEach
                if (openingMinutes == "0") openingMinutes = "00"
                val closingHour = period.close?.time?.hours ?: return@forEach
                var closingMinutes = period.close?.time?.minutes?.toString() ?: return@forEach
                if (closingMinutes == "0") closingMinutes = "00"
                openingHours.add("$day $openingHour:$openingMinutes - $closingHour:$closingMinutes")
            }

            val establishmentData = GetPlaceDataStatus.EstablishmentData(
                latLng = latLng,
                name = name,
                streetAddress = "$streetName $streetNumber, $locality",
                province = province,
                locality = locality,
                country = country,
                openingHours = openingHours.joinToString("\n")
            )
            emit(establishmentData)
        }

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
            is InterruptedException,
            -> emit(GetPlaceDataStatus.EstablishmentPictureException)
            is NoSuchElementException,
            is NullPointerException,
            -> emit(GetPlaceDataStatus.MissingCriticalFieldException)
            else -> emit(GetPlaceDataStatus.UnknownException)
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

        /**
         * Locality in Argentina, i.e. Monte Grande
         */
        private const val LOCALITY = "locality"

        /**
         * Municipality in Argentina, i.e. Esteban Echeverría
         */
        private const val ADMIN_AREA_2 = "administrative_area_level_2"

        /**
         * Province in Argentina, i.e. Mendoza
         */
        private const val ADMIN_AREA_1 = "administrative_area_level_1"

        private const val COUNTRY = "country"
    }
}

private fun DayOfWeek.getDayName(context: Context): String {
    val dayOfWeekStringRes = when (this) {
        DayOfWeek.SUNDAY -> R.string.day_of_week_sunday
        DayOfWeek.MONDAY -> R.string.day_of_week_monday
        DayOfWeek.TUESDAY -> R.string.day_of_week_tuesday
        DayOfWeek.WEDNESDAY -> R.string.day_of_week_wednesday
        DayOfWeek.THURSDAY -> R.string.day_of_week_thursday
        DayOfWeek.FRIDAY -> R.string.day_of_week_friday
        DayOfWeek.SATURDAY -> R.string.day_of_week_saturday
    }
    return context.getString(dayOfWeekStringRes)
}

sealed class GetPlaceDataStatus {
    object Loading : GetPlaceDataStatus()
    data class EstablishmentData(
        val latLng: LatLng,
        val name: String,
        val streetAddress: String,
        val province: String,
        val locality: String,
        val country: String,
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