package org.codingforanimals.veganuniverse.services.google.places.api

import android.content.Context
import android.content.Intent
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.model.DayOfWeek
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import org.codingforanimals.veganuniverse.places.entity.OpeningHours
import org.codingforanimals.veganuniverse.places.entity.Period
import org.codingforanimals.veganuniverse.services.google.places.model.PlaceAutocompleteResult
import com.google.android.libraries.places.api.model.AddressComponents as GoogleAddressComponents
import com.google.android.libraries.places.api.model.Period as GooglePlacesPeriod
import com.google.android.libraries.places.api.net.PlacesClient as GooglePlacesClient

class PlacesClientImpl(
    private val context: Context,
    private val googlePlacesClient: GooglePlacesClient,
) : PlacesClient {

    override suspend fun getPlaceAutocompleteData(intent: Intent): PlaceAutocompleteResult {
        val place = Autocomplete.getPlaceFromIntent(intent)
        return try {
            when {
                place.isEstablishment -> {
                    val bitmap = place.photoMetadatas?.firstOrNull()?.let { photoMetadata ->
                        val photoRequest = FetchPhotoRequest.builder(photoMetadata).build()
                        val task = googlePlacesClient.fetchPhoto(photoRequest)
                        Tasks.await(task)
                    }?.bitmap

                    PlaceAutocompleteResult.Establishment(
                        name = place.name!!,
                        latitude = place.latLng!!.latitude,
                        longitude = place.latLng!!.longitude,
                        openingHours = place.openingHours?.periods!!.toOpeningHours(),
                        addressComponents = place.addressComponents!!.getAddressComponents(),
                        bitmap = bitmap,
                    )
                }

                place.isStreetAddress -> {
                    PlaceAutocompleteResult.StreetAddress(
                        latitude = place.latLng!!.latitude,
                        longitude = place.latLng!!.longitude,
                        addressComponents = place.addressComponents!!.getAddressComponents()
                    )
                }

                else -> {
                    PlaceAutocompleteResult.Location(
                        latitude = place.latLng!!.latitude,
                        longitude = place.latLng!!.longitude,
                    )
                }
            }
        } catch (e: Throwable) {
            PlaceAutocompleteResult.Error
        }
    }

    override fun getPlaceAutocompleteIntent(params: AutocompleteIntentParams): Intent {
        return Autocomplete
            .IntentBuilder(AutocompleteActivityMode.OVERLAY, params.placeFields)
            .setCountries(params.countries)
            .setLocationBias(params.locationBiasBounds?.let { RectangularBounds.newInstance(it) })
            .setTypesFilter(listOfNotNull(params.placeTypeFilter?.filter))
            .build(context)
    }

    private fun GoogleAddressComponents.getAddressComponents(): org.codingforanimals.veganuniverse.places.entity.AddressComponents {
        with(asList()) {
            val streetName = first { it.types.contains(STREET_NAME) }.name
            val streetNumber = first { it.types.contains(STREET_NUMBER) }.name
            val streetAddress = if (streetName.isBlank() && streetNumber.isBlank()) {
                emptyString
            } else {
                "$streetName $streetNumber"
            }
            val locality = first { it.types.contains(LOCALITY) }.name
            val primaryAdminArea = first { it.types.contains(ADMIN_AREA_1) }.name
            val secondaryAdminArea = first { it.types.contains(ADMIN_AREA_2) }.name
            val country = first { it.types.contains(COUNTRY) }.name
            return org.codingforanimals.veganuniverse.places.entity.AddressComponents(
                streetAddress, locality, primaryAdminArea, secondaryAdminArea, country
            )
        }
    }

    private val Place.isEstablishment: Boolean
        get() = types?.contains(Place.Type.ESTABLISHMENT) == true

    private val Place.isStreetAddress: Boolean
        get() = types?.contains(Place.Type.STREET_ADDRESS) == true

    private fun List<GooglePlacesPeriod>.toOpeningHours(): List<OpeningHours> {
        val googlePeriods = sortedBy { it.open?.time?.hours }.groupBy { it.open?.day }
        val result = googlePeriods.mapNotNullTo(mutableListOf()) { entry ->
            try {
                val day = entry.key!!
                val mainPeriod = with(entry.value.first()) {
                    Period(
                        openingHour = open!!.time.hours,
                        openingMinute = open!!.time.minutes,
                        closingHour = close!!.time.hours,
                        closingMinute = close!!.time.minutes,
                    )
                }
                val second = entry.value.getOrNull(1)
                return@mapNotNullTo if (second == null) {
                    OpeningHours(
                        dayOfWeek = day.name,
                        mainPeriod = mainPeriod,
                    )
                } else {
                    val secondPeriod = Period(
                        openingHour = second.open!!.time.hours,
                        openingMinute = second.open!!.time.minutes,
                        closingHour = second.close!!.time.hours,
                        closingMinute = second.close!!.time.minutes,
                    )
                    OpeningHours(
                        dayOfWeek = day.name,
                        mainPeriod = mainPeriod,
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
                    dayOfWeek = day.name,
                )
                result.add(closedDay)
            }
        }
        return result
    }

    companion object {
        private const val emptyString = ""
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