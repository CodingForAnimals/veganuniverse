@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.create.place.presentation

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.CameraPositionState
import java.util.concurrent.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.create.place.presentation.CreatePlaceViewModel.Action
import org.codingforanimals.veganuniverse.create.place.presentation.CreatePlaceViewModel.SideEffect
import org.codingforanimals.veganuniverse.create.place.presentation.CreatePlaceViewModel.UiState
import org.codingforanimals.veganuniverse.create.place.presentation.composables.IconSelector
import org.codingforanimals.veganuniverse.create.place.presentation.composables.OpeningHours
import org.codingforanimals.veganuniverse.create.place.presentation.composables.SearchMap
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem.EnterDescription
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem.EnterName
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem.EnterOpeningHours
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem.SelectIcon
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem.SelectTags
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem.SubmitButton
import org.codingforanimals.veganuniverse.create.place.presentation.error.ErrorDialog
import org.codingforanimals.veganuniverse.create.ui.ImagePicker
import org.codingforanimals.veganuniverse.places.ui.PlaceTag
import org.codingforanimals.veganuniverse.ui.Spacing_02
import org.codingforanimals.veganuniverse.ui.Spacing_03
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_05
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.VeganUniverseTheme
import org.codingforanimals.veganuniverse.ui.auth.VerifyEmailPromptScreen
import org.codingforanimals.veganuniverse.ui.components.SelectableChip
import org.codingforanimals.veganuniverse.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.ui.components.VUNormalTextField
import org.codingforanimals.veganuniverse.ui.components.VUTextField
import org.codingforanimals.veganuniverse.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.ui.utils.rememberImageCropperLauncherForActivityResult
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreatePlaceScreen(
    navigateToThankYouScreen: () -> Unit,
    navigateToAlreadyExistingPlace: () -> Unit,
    navigateToAuthenticationScreen: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: CreatePlaceViewModel = koinViewModel(),
) {

    val placesApiLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { viewModel.onAction(Action.OnPlaceSelected(it)) },
    )

    val imagePicker = rememberImageCropperLauncherForActivityResult(onCropSuccess = {
        viewModel.onAction(
            Action.OnFormChange(imageUri = it)
        )
    })

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateToThankYouScreen = navigateToThankYouScreen,
        imagePicker = imagePicker,
        addressPicker = placesApiLauncher,
        cameraPositionState = viewModel.uiState.cameraPositionState,
        navigateToAlreadyExistingPlace = navigateToAlreadyExistingPlace,
        navigateToAuthenticationScreen = navigateToAuthenticationScreen,
        navigateUp = navigateUp,
    )

    Column(modifier = Modifier.fillMaxSize()) {

        VUTopAppBar(
            title = stringResource(R.string.create_place),
            onBackClick = { viewModel.onAction(Action.OnBackClick) }
        )
        CreatePlaceScreen(
            uiState = viewModel.uiState,
            onAction = viewModel::onAction,
        )

        if (viewModel.uiState.showVerifyEmailPrompt) {
            VerifyEmailPromptScreen(
                onSendRequest = { viewModel.onAction(Action.OnVerifyEmailPromptSendRequest) },
                onDismissRequest = { viewModel.onAction(Action.OnVerifyEmailPromptDismissRequest) },
            )
        }

        viewModel.uiState.errorDialog?.let { errorData ->
            ErrorDialog(
                errorData = errorData,
                navigateToAlreadyExistingPlace = navigateToAlreadyExistingPlace,
                onDismissRequest = { viewModel.onAction(Action.OnErrorDialogDismissRequest) },
            )
        }

        VUCircularProgressIndicator(visible = viewModel.uiState.isLoading)
    }
}

@Composable
private fun CreatePlaceScreen(
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing_05),
        contentPadding = PaddingValues(bottom = Spacing_06),
    ) {
        items(
            items = uiState.content,
            itemContent = { item ->
                when (item) {
                    CreatePlaceFormItem.Map -> SearchMap(uiState = uiState, onAction = onAction)
                    EnterName -> VUNormalTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing_06),
                        label = "Nombre",
                        value = uiState.nameField.value,
                        onValueChange = { onAction(Action.OnFormChange(name = it)) },
                        isError = uiState.isValidating && !uiState.nameField.isValid,
                        maxLines = 1,
                    )

                    EnterOpeningHours -> OpeningHours(
                        openingHoursField = uiState.openingHoursField,
                        openingHoursTimePickerState = uiState.openingHoursTimePickerState,
                        onAction = onAction,
                    )

                    CreatePlaceFormItem.SelectImage -> {
                        val isError = uiState.isValidating && !uiState.pictureField.isValid
                        val borderColor = when {
                            isError -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.outline
                        }
                        ImagePicker(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(2f)
                                .padding(horizontal = Spacing_06)
                                .border(1.dp, borderColor, ShapeDefaults.Medium)
                                .clip(ShapeDefaults.Medium),
                            imageModel = uiState.pictureField.model,
                            isError = isError,
                            onClick = { onAction(Action.OnImagePickerClick) },
                        )
                    }

                    SelectIcon -> IconSelector(
                        typeField = uiState.typeField,
                        isValidating = uiState.isValidating,
                        onAction = onAction,
                    )

                    EnterDescription -> {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing_06),
                            text = stringResource(R.string.place_description_field_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        VUTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(start = Spacing_06, end = Spacing_06, top = Spacing_02),
                            value = uiState.descriptionField.value,
                            isError = uiState.isValidating && !uiState.descriptionField.isValid,
                            onValueChange = { onAction(Action.OnFormChange(description = it)) },
                            placeholder = stringResource(R.string.place_description_field_placeholder)
                        )
                    }

                    SelectTags -> {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing_06),
                            text = stringResource(R.string.place_selected_tags_field_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = Spacing_06, end = Spacing_06, top = Spacing_03),
                            horizontalArrangement = Arrangement.spacedBy(Spacing_04),
                        ) {
                            for (tag in PlaceTag.values()) {
                                val selected = uiState.selectedTagsField.contains(tag)
                                SelectableChip(
                                    label = stringResource(tag.label),
                                    icon = tag.icon,
                                    selected = selected,
                                    onClick = { onAction(Action.OnFormChange(tag = tag)) },
                                )
                            }
                        }
                    }

                    SubmitButton -> Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(horizontal = Spacing_06),
                        onClick = { onAction(Action.OnSubmitClick) },
                        content = { Text(text = stringResource(R.string.submit_button_label)) },
                    )
                }
            },
        )
    }
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateToThankYouScreen: () -> Unit,
    imagePicker: ActivityResultLauncher<PickVisualMediaRequest>,
    addressPicker: ActivityResultLauncher<Intent>,
    cameraPositionState: CameraPositionState,
    navigateToAlreadyExistingPlace: () -> Unit,
    navigateToAuthenticationScreen: () -> Unit,
    navigateUp: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { effect ->
            when (effect) {
                SideEffect.NavigateToThankYouScreen -> {
                    navigateToThankYouScreen()
                }

                SideEffect.OpenImageSelector -> {
                    imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                is SideEffect.OpenAutoCompleteOverlay -> {
                    addressPicker.launch(effect.autocompleteIntent)
                }

                is SideEffect.ZoomInLocation -> {
                    try {
                        cameraPositionState.animate(effect.cameraUpdate, effect.duration)
                    } catch (e: CancellationException) {
                        Log.d("PlacesHomeScreen.kt", e.stackTraceToString())
                    }
                }

                SideEffect.NavigateToAlreadyExistingPlace -> {
                    navigateToAlreadyExistingPlace()
                }

                SideEffect.NavigateToAuthenticateScreen -> {
                    navigateToAuthenticationScreen()
                }

                SideEffect.ShowVerifyEmailPrompt -> {

                }

                SideEffect.NavigateUp -> navigateUp()
            }
        }.collect()
    }
}

@Preview
@Composable
private fun PreviewCreatePlaceScreen() {
    VeganUniverseTheme {
        VeganUniverseBackground {
            val items = listOf(
                CreatePlaceFormItem.Map,
                EnterName,
                EnterOpeningHours,
                CreatePlaceFormItem.SelectImage,
                SelectIcon,
                EnterDescription,
                SelectTags,
                SubmitButton,
            )
            CreatePlaceScreen(
                uiState = UiState(content = items),
                onAction = {},
            )
        }
    }
}