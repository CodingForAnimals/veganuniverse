@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.additives.presentation.validator.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.additives.domain.model.Additive
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveEdit
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveType
import org.codingforanimals.veganuniverse.additives.presentation.R
import org.codingforanimals.veganuniverse.additives.presentation.model.AdditiveTypeUI
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.R.string.validate
import org.codingforanimals.veganuniverse.commons.ui.details.ContentDetailItem
import org.codingforanimals.veganuniverse.commons.ui.dialog.ErrorDialog
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.snackbar.LocalSnackbarSender
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdditiveEditValidationScreen(
    navigateUp: () -> Unit,
) {
    val viewModel: AdditiveEditValidationViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AdditiveEditValidationScreen(
        state = state,
        modifier = Modifier.fillMaxSize(),
        onValidate = viewModel::validate,
        navigateUp = navigateUp,
    )

    val snackbarSender = LocalSnackbarSender.current
    LaunchedEffect(Unit) {
        viewModel.result.onEach { result ->
            when (result) {
                AdditiveEditValidationViewModel.Result.Error -> {
                    snackbarSender(result.snackbar)
                }

                AdditiveEditValidationViewModel.Result.Success -> {
                    snackbarSender(result.snackbar)
                    navigateUp()
                }
            }
        }.collect()
    }
}

@Composable
private fun AdditiveEditValidationScreen(
    state: AdditiveEditValidationViewModel.AdditiveState,
    modifier: Modifier = Modifier,
    onValidate: () -> Unit = {},
    navigateUp: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
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
            )
        },
    ) { paddingValues ->
        when (state) {
            AdditiveEditValidationViewModel.AdditiveState.Error -> {
                ErrorDialog(navigateUp)
            }

            AdditiveEditValidationViewModel.AdditiveState.Loading -> Unit
            is AdditiveEditValidationViewModel.AdditiveState.Content -> {
                val typeUI =
                    remember { AdditiveTypeUI.fromString(state.remoteAdditive.type.name) }
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(Spacing_05),
                    verticalArrangement = Arrangement.spacedBy(Spacing_06),
                ) {
                    Column {
                        Text(
                            text = state.remoteAdditive.code
                                ?: stringResource(R.string.additive_edit_validation_screen_empty_code),
                            style = MaterialTheme.typography.headlineLarge,
                        )
                        state.editAdditive.code?.let { editAdditiveCode ->
                            if (state.editAdditive.code != state.remoteAdditive.code) {
                                Text(
                                    text = editAdditiveCode,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = LocalContentColor.current.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing_04)
                    ) {

                        Column {
                            Text(
                                text = state.remoteAdditive.name
                                    ?: stringResource(R.string.additive_edit_validation_screen_empty_name),
                                style = MaterialTheme.typography.titleMedium,
                            )
                            if (state.editAdditive.name != state.remoteAdditive.name) {
                                Text(
                                    text = state.editAdditive.name.orEmpty(),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = LocalContentColor.current.copy(alpha = 0.5f)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = state.remoteAdditive.description
                                    ?: stringResource(R.string.additive_edit_validation_screen_empty_description),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            if (state.editAdditive.description != state.remoteAdditive.description) {
                                Text(
                                    text = state.editAdditive.description.orEmpty(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = LocalContentColor.current.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                    Column {
                        ContentDetailItem(
                            title = stringResource(typeUI.label),
                            subtitle = stringResource(typeUI.description),
                            icon = typeUI.icon.id,
                        )
                        if (state.editAdditive.type != state.remoteAdditive.type) {
                            val editTypeUI = remember {
                                AdditiveTypeUI.fromString(state.editAdditive.type.name)
                            }
                            ContentDetailItem(
                                modifier = Modifier.alpha(0.5f),
                                title = stringResource(editTypeUI.label),
                                subtitle = stringResource(editTypeUI.description),
                                icon = editTypeUI.icon.id,
                            )
                        }
                    }
                    Button(
                        modifier = Modifier.align(Alignment.End),
                        onClick = onValidate,
                    ) {
                        Text(stringResource(validate))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAdditiveDetailScreen() {
    val remote = Additive.mock(description = null)
    val edit = AdditiveEdit.mock(
        code = "INS 100c",
        name = "Curcumina V2",
        description = "La curcumina ya no es apto vegan",
        type = AdditiveType.NOT_VEGAN
    )
    VeganUniverseTheme {
        AdditiveEditValidationScreen(
            state = AdditiveEditValidationViewModel.AdditiveState.Content(
                remoteAdditive = remote,
                editAdditive = edit
            )
        )
    }
}
