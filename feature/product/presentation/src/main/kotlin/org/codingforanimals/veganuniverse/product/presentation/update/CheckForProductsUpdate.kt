package org.codingforanimals.veganuniverse.product.presentation.update

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.codingforanimals.veganuniverse.product.domain.usecase.CheckForProductsUpdate
import org.koin.androidx.compose.get

@Composable
fun CheckForProductsUpdate() {
    val checkForProductsUpdate = get<CheckForProductsUpdate>()
    LaunchedEffect(Unit) {
        checkForProductsUpdate()
    }
}
