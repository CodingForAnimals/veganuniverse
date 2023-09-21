package org.codingforanimals.veganuniverse.create.presentation.recipe

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.ui.R.string.description
import org.codingforanimals.veganuniverse.core.ui.R.string.servings
import org.codingforanimals.veganuniverse.core.ui.R.string.title
import org.codingforanimals.veganuniverse.core.ui.components.VUNormalTextField
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.create.presentation.composables.CreateContentHero
import org.codingforanimals.veganuniverse.create.presentation.composables.ImagePicker
import org.codingforanimals.veganuniverse.create.presentation.recipe.CreateRecipeViewModel.Action
import org.codingforanimals.veganuniverse.create.presentation.recipe.CreateRecipeViewModel.SideEffect
import org.codingforanimals.veganuniverse.create.presentation.recipe.CreateRecipeViewModel.UiState
import org.codingforanimals.veganuniverse.utils.rememberImageCropperLauncherForActivityResult
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun CreateRecipeScreen(
    viewModel: CreateRecipeViewModel = koinViewModel(),
) {
    val imagePicker = rememberImageCropperLauncherForActivityResult(
        onCropSuccess = {},
    )

    HandleSideEffects(
        sideEffect = viewModel.sideEffect,
        imagePicker = imagePicker,
    )

    CreateRecipeScreen(
        onAction = viewModel::onAction,
        uiState = viewModel.uiState,
    )
}

@Composable
private fun CreateRecipeScreen(
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing_05)
    ) {
        item {
            CreateContentHero(
                heroAnchorIcon = VUIcons.RecipesFilled,
                content = {
                    ImagePicker(
                        imageModel = uiState.pictureField.model,
                        isError = uiState.isValidating && !uiState.pictureField.isValid,
                        onClick = { onAction(Action.ImagePickerClick) },
                    )
                },
            )
        }

        item {
            VUNormalTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Spacing_06),
                value = uiState.titleField.value,
                onValueChange = { onAction(Action.OnTextChange.Title(it)) },
                label = stringResource(title),
                isError = uiState.isValidating && !uiState.titleField.isValid,
                maxLines = 1,
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
                    .fillMaxSize()
                    .padding(horizontal = Spacing_06),
                value = uiState.descriptionField.value,
                onValueChange = { onAction(Action.OnTextChange.Description(it)) },
                label = stringResource(description),
                placeholder = "placeholder\nplaceholderrenglon2",
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
                    .fillMaxSize()
                    .padding(horizontal = Spacing_06),
                value = uiState.servingsField.value,
                onValueChange = { onAction(Action.OnTextChange.Servings(it)) },
                label = stringResource(servings),
                placeholder = "Ej. 4 porciones o 500g",
                isError = uiState.isValidating && !uiState.servingsField.isValid,
                maxChars = 24,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
                leadingIcon = VUIcons.Profile,
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