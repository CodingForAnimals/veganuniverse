package org.codingforanimals.veganuniverse.ui.cards

import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

object VUCardDefaults {

    @Composable
    fun elevatedCardElevation() = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
}