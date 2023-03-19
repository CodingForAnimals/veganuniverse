@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.components.VUTopAppBar

@Composable
internal fun SettingsScreen(
    onBackClick: () -> Unit,
) {
    VUTopAppBar(
        title = "Configuración",
        onBackClick = onBackClick,
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Settings Screen")
    }
}