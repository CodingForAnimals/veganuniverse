package org.codingforanimals.veganuniverse.profile.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.profile.presentation.ProfileScreenViewModel.Action
import org.codingforanimals.veganuniverse.profile.presentation.ProfileScreenViewModel.UiState
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ProfileScreen(
    navigateToRegister: () -> Unit,
    viewModel: ProfileScreenViewModel = koinViewModel(),
) {
    HandleSideEffects(
        sideEffects = viewModel.sideEffect,
        navigateToRegister = navigateToRegister,
    )

    ProfileScreen(
        uiState = viewModel.uiState,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun ProfileScreen(
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    if (uiState.loggedIn) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Logged in, ${uiState.uid}, ${uiState.name}, ${uiState.email}")
            Button(onClick = { onAction(Action.LogOut) }) {
                Text(text = "Log out")
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            OutlinedCard(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .padding(Spacing_06)
                    .align(Alignment.Center)
                    .clickable { onAction(Action.OnCreateUserButtonClick) },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing_06),
                    verticalArrangement = Arrangement.spacedBy(Spacing_05),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        Text(
                            text = "¡Bienvenido a tu\nUniverso Vegano!",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = "Únete a nuestra comunidad y ayudemos juntos a los animales\n\nDescubre contenido exclusivo para usuarios como crear posteos, recetas, lugares, y acceder a tu contenido guardado",
                            textAlign = TextAlign.Center
                        )
                        Button(onClick = { onAction(Action.OnCreateUserButtonClick) }) {
                            Text(text = "Crear mi usuario")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<ProfileScreenViewModel.SideEffect>,
    navigateToRegister: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { effect ->
            when (effect) {
                ProfileScreenViewModel.SideEffect.NavigateToRegister -> navigateToRegister()
            }
        }.collect()
    }
}