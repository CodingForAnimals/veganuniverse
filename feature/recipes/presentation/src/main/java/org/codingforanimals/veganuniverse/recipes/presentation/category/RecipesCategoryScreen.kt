@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.recipes.presentation.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.shared.Post
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04

@Composable
internal fun RecipesScreen(
    navigateToRecipeDetails: () -> Unit,
    onBackClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        VUTopAppBar(title = "Panificados", onBackClick = onBackClick)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
            contentPadding = PaddingValues(Spacing_04)
        ) {
            items(15) {
                Post(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    title = "Receta super piola",
                    subtitle = "@PizzaMuzza • 2 días",
                    description = "Super facil de hacer. Recomiendo para almuerzo en pareja",
                    onClick = navigateToRecipeDetails,
                    image = true,
                    details = {
                        VUIcon(icon = VUIcons.Clock, contentDescription = "")
                        Text(
                            modifier = Modifier.padding(start = Spacing_02, end = Spacing_04),
                            text = "20 min",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        VUIcon(icon = VUIcons.Profile, contentDescription = "")
                        Text(
                            modifier = Modifier.padding(start = Spacing_02),
                            text = "4 // 500gr",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                )
            }
        }
    }
}