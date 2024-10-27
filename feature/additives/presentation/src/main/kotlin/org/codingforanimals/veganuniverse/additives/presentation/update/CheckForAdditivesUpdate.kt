package org.codingforanimals.veganuniverse.additives.presentation.update

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.codingforanimals.veganuniverse.additives.domain.usecase.CheckForAdditivesUpdate
import org.koin.androidx.compose.get

@Composable
fun CheckForAdditivesUpdate() {
    val checkForUpdates = get<CheckForAdditivesUpdate>()
    LaunchedEffect(Unit) {
        checkForUpdates()
    }
}