@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.additives.presentation.browsing

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.codingforanimals.veganuniverse.additives.domain.model.Additive
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveType
import org.codingforanimals.veganuniverse.additives.presentation.R
import org.codingforanimals.veganuniverse.additives.presentation.browsing.AdditivesBrowsingViewModel.AdditivesState
import org.codingforanimals.veganuniverse.additives.presentation.components.AdditiveCard
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_08
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.commons.ui.dialog.ErrorDialog
import org.codingforanimals.veganuniverse.commons.ui.error.ErrorView
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdditivesBrowsingScreen(
    navigateUp: () -> Unit,
    navigateToAdditiveDetail: (String) -> Unit,
) {
    val viewModel: AdditivesBrowsingViewModel = koinViewModel()
    val state by viewModel.additivesState.collectAsStateWithLifecycle()

    AdditivesBrowsingScreen(
        state = state,
        modifier = Modifier.fillMaxSize(),
        navigateUp = navigateUp,
        searchText = viewModel.searchText,
        onSearchTextChange = viewModel::onSearchTextChange,
        onAdditiveClick = navigateToAdditiveDetail,
    )
}

@Composable
private fun AdditivesBrowsingScreen(
    state: AdditivesState,
    modifier: Modifier = Modifier,
    searchText: String = "",
    navigateUp: () -> Unit = {},
    onSearchTextChange: (String) -> Unit = {},
    onAdditiveClick: (String) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                VUTopAppBar(
                    title = stringResource(R.string.additives),
                    onBackClick = navigateUp,
                )
                Row(
                    modifier = Modifier.padding(
                        bottom = Spacing_04,
                        start = Spacing_05,
                        end = Spacing_05,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(Spacing_04),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextField(
                        modifier = Modifier.weight(1f),
                        value = searchText,
                        onValueChange = onSearchTextChange,
                        leadingIcon = {
                            VUIcon(
                                icon = VUIcons.Search,
                                contentDescription = stringResource(id = R.string.search_additive)
                            )
                        },
                        shape = ShapeDefaults.Medium,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        placeholder = { Text(text = stringResource(id = R.string.search_additive)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Crossfade(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            targetState = state,
            label = "state_cross_fade"
        ) {
            when (it) {
                AdditivesState.Empty -> {
                    ErrorView(message = R.string.no_additives_found)
                }

                AdditivesState.Error -> {
                    ErrorDialog(
                        onDismissRequest = navigateUp,
                    )
                }

                AdditivesState.Loading -> {
                    VUCircularProgressIndicator()
                }

                is AdditivesState.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(Spacing_06),
                        contentPadding = PaddingValues(
                            start = Spacing_05,
                            end = Spacing_05,
                            top = Spacing_05,
                            bottom = Spacing_08,
                        )
                    ) {
                        items(it.additives) { additive ->
                            key(additive.id) {
                                AdditiveCard(
                                    additive = additive,
                                    onClick = { additive.id?.let { id -> onAdditiveClick(id) } }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAdditiveBrowsingScreenLoading() {
    VeganUniverseTheme {
        AdditivesBrowsingScreen(
            state = AdditivesState.Loading,
        )
    }
}

@Preview
@Composable
private fun PreviewAdditiveBrowsingScreenError() {
    VeganUniverseTheme {
        AdditivesBrowsingScreen(
            state = AdditivesState.Error,
        )
    }
}

@Preview
@Composable
private fun PreviewAdditiveBrowsingScreenEmpty() {
    VeganUniverseTheme {
        AdditivesBrowsingScreen(
            state = AdditivesState.Empty,
        )
    }
}

@Preview
@Composable
private fun PreviewAdditiveBrowsingScreenSuccess() {
    val additive = Additive(
        id = "123",
        code = "INS 100",
        name = "Curcumina",
        description = "Colorante amarillo derivado de la cúrcuma",
        type = AdditiveType.VEGAN,
    )
    val additives = listOf(
        additive.copy(type = AdditiveType.NOT_VEGAN),
        additive.copy(type = AdditiveType.DOUBTFUL, name = null),
        additive, additive.copy(type = AdditiveType.UNKNOWN, description = null),
    )
    VeganUniverseTheme {
        AdditivesBrowsingScreen(
            state = AdditivesState.Success(additives),
        )
    }
}
