@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.additives.presentation.validator.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.codingforanimals.veganuniverse.additives.presentation.R
import org.codingforanimals.veganuniverse.additives.presentation.components.AdditiveCard
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.commons.ui.dialog.ErrorDialog
import org.codingforanimals.veganuniverse.commons.ui.error.ErrorView
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun AdditiveEditListScreen(
    navigateToAdditiveEditValidation: (String) -> Unit,
    navigateUp: () -> Unit,
) {
    val viewModel: AdditiveEditListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AdditiveEditListScreen(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onAdditiveEditClick = navigateToAdditiveEditValidation,
        navigateUp = navigateUp,
    )
}

@Composable
private fun AdditiveEditListScreen(
    state: AdditiveEditListViewModel.State,
    modifier: Modifier = Modifier,
    onAdditiveEditClick: (String) -> Unit = {},
    navigateUp: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.additive_edit_list_top_app_bar_title))
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateUp
                    ) {
                        Icon(
                            imageVector = VUIcons.ArrowBack.imageVector,
                            contentDescription = stringResource(back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Crossfade(
            modifier = Modifier.padding(paddingValues),
            targetState = state,
            label = "additive_edit_list",
        ) {
            when (it) {
                AdditiveEditListViewModel.State.Empty -> {
                    ErrorView(message = R.string.additive_edit_list_empty_error)
                }

                AdditiveEditListViewModel.State.Error -> {
                    ErrorDialog(navigateUp)
                }

                AdditiveEditListViewModel.State.Loading -> {
                    VUCircularProgressIndicator()
                }

                is AdditiveEditListViewModel.State.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(Spacing_06),
                        contentPadding = PaddingValues(
                            horizontal = Spacing_05,
                            vertical = Spacing_06
                        )
                    ) {
                        items(it.edits) { additiveEdit ->
                            AdditiveCard(
                                additive = additiveEdit.additive,
                                onClick = { onAdditiveEditClick(additiveEdit.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
