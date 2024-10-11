@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.additives.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.additives.domain.model.Additive
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveType
import org.codingforanimals.veganuniverse.additives.presentation.model.AdditiveTypeUI
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialog
import org.codingforanimals.veganuniverse.commons.ui.details.ContentDetailItem
import org.codingforanimals.veganuniverse.commons.ui.dialog.ErrorDialog
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.share.getShareIntent
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdditiveDetailScreen(
    navigateUp: () -> Unit,
    navigateToEditAdditive: (String) -> Unit,
) {
    val viewModel: AdditiveDetailViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    AdditiveDetailScreen(
        state = state,
        modifier = Modifier.fillMaxSize(),
        snackbarHostState = snackbarHostState,
        navigateUp = navigateUp,
        onEditClick = viewModel::onEditClick,
        onShareClick = viewModel::onShareClick,
    )

    HandleSnackbarEffects(
        snackbarEffects = viewModel.snackbarEffect,
        snackbarHostState = snackbarHostState,
    )

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is AdditiveDetailViewModel.SideEffect.Share -> {
                    runCatching {
                        context.startActivity(
                            getShareIntent(sideEffect.textToShare)
                        )
                    }.onFailure { throwable ->
                        Analytics.logNonFatalException(throwable)
                    }
                }

                is AdditiveDetailViewModel.SideEffect.NavigateToAdditiveEdit -> navigateToEditAdditive(sideEffect.id)
            }
        }.collect()
    }
}

@Composable
private fun AdditiveDetailScreen(
    state: AdditiveDetailViewModel.AdditiveState,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navigateUp: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = navigateUp
                    ) {
                        Icon(
                            imageVector = VUIcons.ArrowBack.imageVector,
                            contentDescription = stringResource(back)
                        )
                    }
                },
                actions = {
                    VUIcon(
                        icon = VUIcons.Edit,
                        onIconClick = onEditClick,
                    )
                }
            )
        },
    ) { paddingValues ->
        when (state) {
            AdditiveDetailViewModel.AdditiveState.Error -> {
                ErrorDialog(navigateUp)
            }

            AdditiveDetailViewModel.AdditiveState.Loading -> Unit
            is AdditiveDetailViewModel.AdditiveState.Success -> {
                val typeUI =
                    remember { AdditiveTypeUI.fromString(state.additive.type.name) }
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(Spacing_05),
                    verticalArrangement = Arrangement.spacedBy(Spacing_06),
                ) {
                    Text(
                        text = state.additive.code,
                        style = MaterialTheme.typography.headlineLarge,
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing_04)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = state.additive.name.orEmpty(),
                                style = MaterialTheme.typography.titleMedium,
                            )
                            VUIcon(
                                icon = VUIcons.Share,
                                onIconClick = onShareClick,
                            )
                        }
                        state.additive.description?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                    ContentDetailItem(
                        title = stringResource(typeUI.label),
                        subtitle = stringResource(typeUI.description),
                        icon = typeUI.icon.id,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAdditiveDetailScreen() {
    val additive = Additive(
        id = "123",
        code = "INS 100",
        name = "Curcumina",
        description = "Colorante amarillo derivado de la cúrcuma",
        type = AdditiveType.VEGAN,
    )
    VeganUniverseTheme {
        AdditiveDetailScreen(state = AdditiveDetailViewModel.AdditiveState.Success(additive))
    }
}

@Preview
@Composable
private fun PreviewAdditiveDetailScreenX() {
    val additive = Additive(
        id = "123",
        code = "INS 100",
        name = "Curcumina",
        description = "Colorante amarillo derivado de la cúrcuma",
        type = AdditiveType.VEGAN,
    )
    VeganUniverseTheme {
        AdditiveDetailScreen(state = AdditiveDetailViewModel.AdditiveState.Success(additive))
    }
}