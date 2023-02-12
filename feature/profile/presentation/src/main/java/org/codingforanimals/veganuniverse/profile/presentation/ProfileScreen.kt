package org.codingforanimals.veganuniverse.profile.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
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

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(text = "Profile screen")
            Button(onClick = viewModel::onRegisterClick) {
                Text(text = "Go to register screen")
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
        sideEffects.onEach {  effect ->
            when (effect) {
                ProfileScreenViewModel.SideEffect.NavigateToRegister -> navigateToRegister()
            }
        }.collect()
    }
}