package org.codingforanimals.veganuniverse.app.content

import androidx.compose.runtime.Composable
import org.codingforanimals.veganuniverse.additives.presentation.update.CheckForAdditivesUpdate
import org.codingforanimals.veganuniverse.product.presentation.update.CheckForProductsUpdate

@Composable
fun CheckForContentUpdates() {
    CheckForAdditivesUpdate()
    CheckForProductsUpdate()
}