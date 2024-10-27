package org.codingforanimals.veganuniverse.place.presentation.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.codingforanimals.veganuniverse.place.model.DayOfWeek
import org.codingforanimals.veganuniverse.place.shared.model.OpeningHours
import org.codingforanimals.veganuniverse.place.shared.model.Period

data class OpeningHoursUI(
    val dayOfWeek: DayOfWeek,
    val mainPeriod: Period? = null,
    val secondaryPeriod: Period? = null,
)

@Composable
fun OpeningHours.toUI(): OpeningHoursUI? {
    return remember {
        OpeningHoursUI(
            dayOfWeek = DayOfWeek.fromString(dayOfWeek) ?: return@remember null,
            mainPeriod = mainPeriod,
            secondaryPeriod = secondaryPeriod,
        )
    }
}
