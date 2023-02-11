package org.codingforanimals.veganuniverse.registration.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
) {

    HandleSideEffects(
        effectsFlow = viewModel.sideEffects,
        showAlmostThereContent = {}
    )

    RegisterScreen()
}

@Composable
private fun RegisterScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(text = "Register screen")
    }
}

@Composable
private fun HandleSideEffects(
    effectsFlow: Flow<RegisterViewModel.SideEffect>,
    showAlmostThereContent: () -> Unit,
) {
    LaunchedEffect(Unit) {
        effectsFlow.onEach { effect ->
            when (effect) {
                RegisterViewModel.SideEffect.NavigateToCommunity -> Unit
                RegisterViewModel.SideEffect.ShowAlmostThereContent -> {

                }
            }
        }.collect()
    }
}