package org.codingforanimals.veganuniverse.search.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.components.VeganUniverseTopAppBar

@Composable
internal fun SearchScreen(
    onBackClick: () -> Unit,
) {
    VeganUniverseTopAppBar(title = "Buscar", onBackClick = onBackClick)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Text("Search screen")
    }
}