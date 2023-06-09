package org.codingforanimals.veganuniverse.create.presentation.place.usecase

import android.content.Context
import android.content.Intent
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class GetAutoCompleteIntentUseCase(
    private val context: Context,
) {
    operator fun invoke(): Intent {
        return Autocomplete
            .IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(context)
    }

    private companion object {
        private val fields = listOf(
            Place.Field.NAME,
            Place.Field.TYPES,
            Place.Field.LAT_LNG,
            Place.Field.OPENING_HOURS,
            Place.Field.PHOTO_METADATAS,
            Place.Field.ADDRESS_COMPONENTS,
        )
    }
}