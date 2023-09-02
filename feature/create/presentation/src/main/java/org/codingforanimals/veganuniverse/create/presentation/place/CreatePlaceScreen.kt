@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.create.presentation.place

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.maps.android.compose.CameraPositionState
import java.util.concurrent.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.core.ui.components.VUNormalTextField
import org.codingforanimals.veganuniverse.core.ui.components.VUSelectableChip
import org.codingforanimals.veganuniverse.core.ui.components.VUTextField
import org.codingforanimals.veganuniverse.core.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_03
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.core.ui.theme.VeganUniverseTheme
import org.codingforanimals.veganuniverse.create.presentation.R
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.Action
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.SideEffect
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.UiState
import org.codingforanimals.veganuniverse.create.presentation.place.composables.IconSelector
import org.codingforanimals.veganuniverse.create.presentation.place.composables.ImagePicker
import org.codingforanimals.veganuniverse.create.presentation.place.composables.OpeningHours
import org.codingforanimals.veganuniverse.create.presentation.place.composables.SearchMap
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.EnterDescription
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.EnterName
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.EnterOpeningHours
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.Map
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.SelectIcon
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.SelectImage
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.SelectTags
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.SubmitButton
import org.codingforanimals.veganuniverse.create.presentation.place.error.ErrorDialog
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun CreatePlaceScreen(
    onCreateSuccess: () -> Unit,
    navigateToAlreadyExistingPlace: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    viewModel: CreatePlaceViewModel = koinViewModel(),
) {

    val placesApiLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { viewModel.onAction(Action.OnPlaceSelected(it)) },
    )

    val cropImage = rememberLauncherForActivityResult(
        contract = CropImageContract(),
        onResult = { result ->
            if (result.isSuccessful) {
                viewModel.onAction(Action.OnFormChange(imageUri = result.uriContent))
            } else {
                Log.e(
                    "CreatePlaceScreen.kt",
                    "Error cropping image: ${result.error?.stackTraceToString()}"
                )
            }
        },
    )

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                cropImage.launch(CropImageContractOptions(uri, CropImageOptions()))
            }
        },
    )

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        onCreateSuccess = onCreateSuccess,
        imagePicker = imagePicker,
        addressPicker = placesApiLauncher,
        cameraPositionState = viewModel.uiState.cameraPositionState,
        navigateToAlreadyExistingPlace = navigateToAlreadyExistingPlace,
        navigateToAuthenticateScreen = navigateToAuthenticateScreen,
    )

    CreatePlaceScreen(
        uiState = viewModel.uiState,
        onAction = viewModel::onAction,
    )

    VUCircularProgressIndicator(visible = viewModel.uiState.isLoading)


    // TODO this will most likely need reworking to fit new designs in the future
    viewModel.uiState.errorDialog?.let { errorData ->
        ErrorDialog(
            errorData = errorData,
            navigateToAlreadyExistingPlace = navigateToAlreadyExistingPlace,
            onDismissRequest = { viewModel.onAction(Action.OnErrorDialogDismissRequest) },
        )
    }
}

@Composable
private fun CreatePlaceScreen(
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing_05)
    ) {
        items(
            items = uiState.content,
            itemContent = { item ->
                when (item) {
                    Map -> SearchMap(uiState = uiState, onAction = onAction)
                    EnterName -> VUNormalTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing_06),
                        label = "Nombre",
                        value = uiState.nameField.value,
                        onValueChange = { onAction(Action.OnFormChange(name = it)) },
                        isError = uiState.isValidating && !uiState.nameField.isValid,
//                        placeholder = stringResource(R.string.place_name_field_placeholder),
                    )

                    EnterOpeningHours -> OpeningHours(
                        openingHoursField = uiState.openingHoursField,
                        openingHoursTimePickerState = uiState.openingHoursTimePickerState,
                        onAction = onAction,
                    )

                    SelectImage -> ImagePicker(
                        onAction = onAction,
                        pictureField = uiState.pictureField,
                        isValidating = uiState.isValidating,
                    )

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
                                VUSelectableChip(
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
                            .padding(bottom = 75.dp, end = Spacing_06),
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
    onCreateSuccess: () -> Unit,
    imagePicker: ActivityResultLauncher<PickVisualMediaRequest>,
    addressPicker: ActivityResultLauncher<Intent>,
    cameraPositionState: CameraPositionState,
    navigateToAlreadyExistingPlace: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { effect ->
            when (effect) {
                SideEffect.NavigateToThankYouScreen -> {
                    onCreateSuccess()
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
                    navigateToAuthenticateScreen()
                }
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
                Map,
                EnterName,
                EnterOpeningHours,
                SelectImage,
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