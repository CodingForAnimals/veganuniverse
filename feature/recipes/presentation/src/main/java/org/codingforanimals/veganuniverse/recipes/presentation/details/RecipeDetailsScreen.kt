package org.codingforanimals.veganuniverse.recipes.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.codingforanimals.veganuniverse.core.ui.components.VUTopAppBar

@Composable
internal fun RecipeDetailsScreen(
    onBackClick: () -> Unit,
) {
    VUTopAppBar(title = "Detalles", onBackClick = onBackClick)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Recipe Details Screen")
    }
}