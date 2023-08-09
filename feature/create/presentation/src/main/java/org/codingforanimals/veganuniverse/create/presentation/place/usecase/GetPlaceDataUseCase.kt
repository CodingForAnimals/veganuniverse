package org.codingforanimals.veganuniverse.create.presentation.place.usecase

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.StringRes
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.model.DayOfWeek
import com.google.android.libraries.places.api.model.Period
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import java.util.concurrent.ExecutionException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.common.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_friday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_monday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_saturday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_sunday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_thursday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_tuesday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_wednesday
import org.codingforanimals.veganuniverse.create.presentation.place.model.OpeningHours

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
            val primaryAdminArea = first { it.types.contains(ADMIN_AREA_1) }.name
            val secondaryAdminArea = first { it.types.contains(ADMIN_AREA_2) }.name
            val country = first { it.types.contains(COUNTRY) }.name
            val latLng = place.latLng!!
            val name = place.name!!

            val establishmentData = GetPlaceDataStatus.EstablishmentData(
                latLng = latLng,
                name = name,
                streetAddress = "$streetName $streetNumber, $locality",
                province = secondaryAdminArea,
                locality = locality,
                country = country,
                openingHours = place.openingHours?.periods?.toOpeningHours() ?: emptyList(),
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

    private fun List<Period>.toOpeningHours(): List<OpeningHours> {
        val googlePeriods = sortedBy { it.open?.time?.hours }.groupBy { it.open?.day }
        val result = googlePeriods.mapNotNullTo(mutableListOf()) { entry ->
            try {
                val day = entry.key!!
                val mainPeriod = with(entry.value.first()) {
                    OpeningHours.Period(
                        openingHour = open!!.time.hours,
                        openingMinute = open!!.time.minutes,
                        closingHour = close!!.time.hours,
                        closingMinute = close!!.time.minutes,
                    )
                }
                val second = entry.value.getOrNull(1)
                return@mapNotNullTo if (second == null) {
                    OpeningHours(
                        dayOfWeek = day,
                        mainPeriod = mainPeriod,
                    )
                } else {
                    val secondPeriod = OpeningHours.Period(
                        openingHour = second.open!!.time.hours,
                        openingMinute = second.open!!.time.minutes,
                        closingHour = second.close!!.time.hours,
                        closingMinute = second.close!!.time.minutes,
                    )
                    OpeningHours(
                        dayOfWeek = day,
                        mainPeriod = mainPeriod,
                        isSplit = true,
                        secondaryPeriod = secondPeriod,
                    )
                }
            } catch (e: NullPointerException) {
                return@mapNotNullTo null
            }
        }

        DayOfWeek.values().forEach { day ->
            if (googlePeriods[day] == null) {
                val closedDay = OpeningHours(
                    dayOfWeek = day,
                    isClosed = true,
                )
                result.add(closedDay)
            }
        }
        return result
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

@StringRes
private fun DayOfWeek.getDayName(): Int {
    val dayOfWeekStringRes = when (this) {
        DayOfWeek.SUNDAY -> day_of_week_sunday
        DayOfWeek.MONDAY -> day_of_week_monday
        DayOfWeek.TUESDAY -> day_of_week_tuesday
        DayOfWeek.WEDNESDAY -> day_of_week_wednesday
        DayOfWeek.THURSDAY -> day_of_week_thursday
        DayOfWeek.FRIDAY -> day_of_week_friday
        DayOfWeek.SATURDAY -> day_of_week_saturday
    }
    return dayOfWeekStringRes
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
        val openingHours: List<OpeningHours>,
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