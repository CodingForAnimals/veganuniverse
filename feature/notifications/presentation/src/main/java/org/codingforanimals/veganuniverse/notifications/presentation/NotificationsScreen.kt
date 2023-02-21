@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.notifications.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.icons.VeganUniverseIcons

@Composable
internal fun NotificationsScreen(
    onBackClick: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = VeganUniverseIcons.ArrowBack,
                    contentDescription = "Atrás"
                )
            }
        },
        title = { Text(text = "Notificaciones") }
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Text(text = "This is Notifications Screen")
    }
}