@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.create.recipe

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.create.presentation.CreateContentHero
import org.codingforanimals.veganuniverse.commons.create.presentation.HeroAnchorDefaults
import org.codingforanimals.veganuniverse.commons.create.presentation.ImagePicker
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.create.presentation.R.string.publish
import org.codingforanimals.veganuniverse.create.recipe.CreateRecipeViewModel.Action
import org.codingforanimals.veganuniverse.create.recipe.CreateRecipeViewModel.SideEffect
import org.codingforanimals.veganuniverse.create.recipe.CreateRecipeViewModel.UiState
import org.codingforanimals.veganuniverse.create.recipe.composables.Ingredients
import org.codingforanimals.veganuniverse.create.recipe.composables.Steps
import org.codingforanimals.veganuniverse.create.recipe.presentation.R
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.components.SelectableChip
import org.codingforanimals.veganuniverse.commons.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.commons.ui.components.VUNormalTextField
import org.codingforanimals.veganuniverse.commons.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.commons.ui.dialog.NoActionDialog
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.utils.rememberImageCropperLauncherForActivityResult
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateRecipeScreen(
    navigateUp: () -> Unit,
    navigateToThankYouScreen: () -> Unit,
) {
    val viewModel: CreateRecipeViewModel = koinViewModel()
    val imagePicker = rememberImageCropperLauncherForActivityResult(
        onCropSuccess = { viewModel.onAction(Action.ImagePicker.Success(it)) },
    )

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            VUTopAppBar(
                title = stringResource(R.string.create_recipe),
                onBackClick = { viewModel.onAction(Action.OnBackClick) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        CreateRecipeScreen(
            modifier = Modifier.padding(paddingValues),
            uiState = viewModel.uiState,
            onAction = viewModel::onAction,
        )
    }

    VUCircularProgressIndicator(visible = viewModel.uiState.loading)

    viewModel.uiState.dialog?.let {
        NoActionDialog(
            title = it.title,
            message = it.message,
            buttonText = back,
            onDismissRequest = { viewModel.onAction(Action.DialogDismissRequest) }
        )
    }

    if (viewModel.showUnverifiedEmailDialog) {
        UnverifiedEmailDialog(onResult = viewModel::onUnverifiedEmailResult)
    }

    HandleSideEffects(
        sideEffect = viewModel.sideEffect,
        imagePicker = imagePicker,
    )

    HandleSnackbarEffects(
        snackbarEffects = viewModel.snackbarEffects,
        snackbarHostState = snackbarHostState,
    )

    LaunchedEffect(Unit) {
        viewModel.navigationEffects.onEach { effect ->
            when (effect) {
                CreateRecipeViewModel.NavigationEffect.NavigateToThankYouScreen -> navigateToThankYouScreen()
                CreateRecipeViewModel.NavigationEffect.NavigateUp -> navigateUp()
            }
        }.collect()
    }
}

@Composable
private fun CreateRecipeScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing_06),
        contentPadding = PaddingValues(bottom = Spacing_06)
    ) {
        item {
            val heroAnchorColors = when {
                uiState.pictureField.isValid -> HeroAnchorDefaults.primaryColors()
                uiState.isValidating -> HeroAnchorDefaults.errorColors()
                else -> HeroAnchorDefaults.secondaryColors()
            }
            CreateContentHero(
                heroAnchorIcon = VUIcons.RecipesFilled,
                heroAnchorColors = heroAnchorColors,
                content = {
                    ImagePicker(
                        imageModel = uiState.pictureField.model,
                        isError = uiState.isValidating && !uiState.pictureField.isValid,
                        onClick = { onAction(Action.ImagePicker.Click) },
                    )
                },
            )
        }

        item {
            Text(
                modifier = Modifier.padding(horizontal = Spacing_06),
                text = stringResource(R.string.create_recipe_title),
                style = MaterialTheme.typography.titleMedium,
            )
        }

        item {
            VUNormalTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06),
                value = uiState.nameField.value,
                onValueChange = { onAction(Action.OnTextChange.Name(it)) },
                label = stringResource(R.string.title),
                isError = uiState.isValidating && !uiState.nameField.isValid,
                maxChars = 64,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
            )
        }

        item {
            VUNormalTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06),
                value = uiState.descriptionField.value,
                onValueChange = { onAction(Action.OnTextChange.Description(it)) },
                label = stringResource(R.string.description),
                placeholder = stringResource(R.string.create_recipe_description_placeholder),
                isError = uiState.isValidating && !uiState.descriptionField.isValid,
                maxChars = 600,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
            )
        }

        item {
            VUNormalTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06),
                value = uiState.servingsField.value,
                onValueChange = { onAction(Action.OnTextChange.Servings(it)) },
                label = stringResource(R.string.servings),
                placeholder = stringResource(R.string.create_recipe_servings_placeholder),
                isError = uiState.isValidating && !uiState.servingsField.isValid,
                maxChars = 24,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
                leadingIcon = VUIcons.Profile,
            )
        }

        item {
            VUNormalTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06),
                value = uiState.prepTimeField.value,
                onValueChange = { onAction(Action.OnTextChange.PrepTime(it)) },
                label = stringResource(R.string.prep_time),
                isError = uiState.isValidating && !uiState.prepTimeField.isValid,
                maxChars = 64,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
                leadingIcon = VUIcons.Clock,
            )
        }

        item { HorizontalDivider() }

        item {
            Ingredients(
                ingredientsField = uiState.ingredientsField,
                isValidating = uiState.isValidating,
                onAction = onAction,
            )
        }

        item { HorizontalDivider() }

        item {
            Steps(
                stepsField = uiState.stepsField,
                isValidating = uiState.isValidating,
                onAction = onAction,
            )
        }

        item { HorizontalDivider() }

        item {
            val color = when {
                !uiState.isValidating -> MaterialTheme.colorScheme.onSurfaceVariant
                !uiState.tagsField.isValid -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
            Text(
                modifier = Modifier.padding(horizontal = Spacing_06),
                text = stringResource(R.string.tags),
                style = MaterialTheme.typography.titleMedium,
                color = color,
            )
            FlowRow(
                modifier = Modifier.padding(
                    start = Spacing_06,
                    end = Spacing_06,
                    top = Spacing_05,
                ),
                horizontalArrangement = Arrangement.spacedBy(Spacing_04)
            ) {
                for (tag in uiState.tagsField.tags) {
                    SelectableChip(
                        label = stringResource(tag.label),
                        icon = tag.icon,
                        selected = tag.selected,
                        onClick = { onAction(Action.OnTagSelected(tag.label)) }
                    )
                }
            }
        }

        item {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
                    .padding(horizontal = Spacing_06),
                onClick = { onAction(Action.OnSubmitClick) },
                content = {
                    Text(text = stringResource(publish))
                },
            )
        }
    }
}

@Composable
private fun HandleSideEffects(
    sideEffect: Flow<SideEffect>,
    imagePicker: ActivityResultLauncher<PickVisualMediaRequest>,
) {
    LaunchedEffect(Unit) {
        sideEffect.onEach { sideEffect ->
            when (sideEffect) {
                SideEffect.OpenImageSelector -> {
                    imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }
        }.collect()
    }
}

@Preview
@Composable
private fun PreviewCreateRecipeScreen() {
    VeganUniverseTheme {
        Surface {
            CreateRecipeScreen(
                uiState = UiState(),
                onAction = {},
            )
        }
    }
}