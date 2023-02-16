@file:OptIn(ExperimentalFoundationApi::class)

package org.codingforanimals.veganuniverse.registration.presentation.register

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun RegisterScreen(
    navigateToCommunity: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel(),
) {

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    HandleSideEffects(
        effectsFlow = viewModel.sideEffects,
        showAlmostThereContent = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(2)
            }
        },
        navigateToCommunity = navigateToCommunity,
    )

    RegisterScreen(
        onAction = viewModel::onAction,
        pagerState = pagerState,
    )
}

@Composable
private fun RegisterScreen(
    onAction: (RegisterViewModel.Action) -> Unit,
    pagerState: PagerState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HorizontalPager(
            state = pagerState,
            pageCount = 2
        ) { currentPage ->
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when (currentPage) {
                    0 -> {
                        Text(text = "Crea tu usuario")
                        Button(onClick = { onAction(RegisterViewModel.Action.RegistrationAttempt) }) {
                            Text(text = "Registrarte")
                        }
                    }
                    1 -> {
                        Text(text = "Por favor verifica tu email")
                        Button(onClick = { onAction(RegisterViewModel.Action.AlmostThereDismissed) }) {
                            Text(text = "Continuar como invitado")
                        }
                    }
                }

            }
        }

    }
}

@Composable
private fun HandleSideEffects(
    effectsFlow: Flow<RegisterViewModel.SideEffect>,
    showAlmostThereContent: () -> Unit,
    navigateToCommunity: () -> Unit,
) {
    LaunchedEffect(Unit) {
        effectsFlow.onEach { effect ->
            when (effect) {
                RegisterViewModel.SideEffect.NavigateToCommunity -> navigateToCommunity()
                RegisterViewModel.SideEffect.ShowAlmostThereContent -> showAlmostThereContent()
                RegisterViewModel.SideEffect.ShowRegisterError -> {

                }
            }
        }.collect()
    }
}