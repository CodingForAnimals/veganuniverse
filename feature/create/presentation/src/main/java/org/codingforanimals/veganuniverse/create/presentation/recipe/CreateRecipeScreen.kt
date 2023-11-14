@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.create.presentation.recipe

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.common.R.string.back
import org.codingforanimals.veganuniverse.core.ui.R.string.description
import org.codingforanimals.veganuniverse.core.ui.R.string.prep_time
import org.codingforanimals.veganuniverse.core.ui.R.string.servings
import org.codingforanimals.veganuniverse.core.ui.R.string.tags
import org.codingforanimals.veganuniverse.core.ui.R.string.title
import org.codingforanimals.veganuniverse.create.presentation.R
import org.codingforanimals.veganuniverse.create.presentation.composables.CreateContentHero
import org.codingforanimals.veganuniverse.create.presentation.composables.HeroAnchorDefaults
import org.codingforanimals.veganuniverse.create.presentation.composables.ImagePicker
import org.codingforanimals.veganuniverse.create.presentation.recipe.CreateRecipeViewModel.Action
import org.codingforanimals.veganuniverse.create.presentation.recipe.CreateRecipeViewModel.SideEffect
import org.codingforanimals.veganuniverse.create.presentation.recipe.CreateRecipeViewModel.UiState
import org.codingforanimals.veganuniverse.create.presentation.recipe.composables.Ingredients
import org.codingforanimals.veganuniverse.create.presentation.recipe.composables.Steps
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_05
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.Spacing_12
import org.codingforanimals.veganuniverse.ui.VeganUniverseTheme
import org.codingforanimals.veganuniverse.ui.auth.VerifyEmailPromptScreen
import org.codingforanimals.veganuniverse.ui.components.SelectableChip
import org.codingforanimals.veganuniverse.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.ui.components.VUNormalTextField
import org.codingforanimals.veganuniverse.ui.dialog.NoActionDialog
import org.codingforanimals.veganuniverse.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.utils.rememberImageCropperLauncherForActivityResult
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun CreateRecipeScreen(
    navigateToThankYouScreen: () -> Unit,
    navigateToAuthenticationScreen: () -> Unit,
    viewModel: CreateRecipeViewModel = koinViewModel(),
) {
    val imagePicker = rememberImageCropperLauncherForActivityResult(
        onCropSuccess = { viewModel.onAction(Action.ImagePicker.Success(it)) },
    )

    HandleSideEffects(
        sideEffect = viewModel.sideEffect,
        imagePicker = imagePicker,
        navigateToThankYouScreen = navigateToThankYouScreen,
        navigateToAuthenticationScreen = navigateToAuthenticationScreen,
    )

    Box {
        CreateRecipeScreen(
            uiState = viewModel.uiState,
            onAction = viewModel::onAction,
        )

        if (viewModel.uiState.showVerifyEmailPrompt) {
            VerifyEmailPromptScreen(
                onSendRequest = { viewModel.onAction(Action.VerifyEmail.Send) },
                onDismissRequest = { viewModel.onAction(Action.VerifyEmail.Dismiss) },
            )
        }

        viewModel.uiState.dialog?.let {
            NoActionDialog(
                title = it.title,
                message = it.message,
                buttonText = back,
                onDismissRequest = { viewModel.onAction(Action.DialogDismissRequest) }
            )
        }

        VUCircularProgressIndicator(visible = viewModel.uiState.loading)
    }
}

@Composable
private fun CreateRecipeScreen(
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing_06),
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
                style = MaterialTheme.typography.titleLarge,
            )
        }

        item {
            VUNormalTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06),
                value = uiState.titleField.value,
                onValueChange = { onAction(Action.OnTextChange.Title(it)) },
                label = stringResource(title),
                isError = uiState.isValidating && !uiState.titleField.isValid,
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
                label = stringResource(description),
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
                label = stringResource(servings),
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
                label = stringResource(prep_time),
                isError = uiState.isValidating && !uiState.prepTimeField.isValid,
                maxChars = 64,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
                leadingIcon = VUIcons.Clock,
            )
        }

        item { Divider() }

        item {
            Ingredients(
                ingredientsField = uiState.ingredientsField,
                isValidating = uiState.isValidating,
                onAction = onAction,
            )
        }

        item { Divider() }

        item {
            Steps(
                stepsField = uiState.stepsField,
                isValidating = uiState.isValidating,
                onAction = onAction,
            )
        }

        item { Divider() }

        item {
            val color = when {
                !uiState.isValidating -> MaterialTheme.colorScheme.onSurfaceVariant
                !uiState.tagsField.isValid -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
            Text(
                modifier = Modifier.padding(horizontal = Spacing_06),
                text = stringResource(tags),
                style = MaterialTheme.typography.titleLarge,
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
                    .padding(
                        start = Spacing_06, end = Spacing_06,
                        bottom = Spacing_12,
                    ),
                onClick = { onAction(Action.OnSubmitClick) },
                content = {
                    Text(text = stringResource(R.string.submit_button_label))
                },
            )
        }
    }
}

@Composable
private fun HandleSideEffects(
    sideEffect: Flow<SideEffect>,
    imagePicker: ActivityResultLauncher<PickVisualMediaRequest>,
    navigateToThankYouScreen: () -> Unit,
    navigateToAuthenticationScreen: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffect.onEach { sideEffect ->
            when (sideEffect) {
                SideEffect.OpenImageSelector -> {
                    imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                SideEffect.NavigateToAuthenticationScreen -> {
                    navigateToAuthenticationScreen()
                }

                SideEffect.NavigateToThankYouScreen -> {
                    navigateToThankYouScreen()
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