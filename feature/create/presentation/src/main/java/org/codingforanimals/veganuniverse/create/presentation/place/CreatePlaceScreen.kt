@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.create.presentation.place

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.maps.android.compose.CameraPositionState
import java.util.concurrent.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUTextFieldDefaults
import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.create.presentation.R
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.Action
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.CreatePlaceFormItem.Map
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.CreatePlaceFormItem.NameField
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.CreatePlaceFormItem.OpeningHoursField
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.CreatePlaceFormItem.Picture
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.SideEffect
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.UiState
import org.codingforanimals.veganuniverse.create.presentation.place.composables.ImagePicker
import org.codingforanimals.veganuniverse.create.presentation.place.composables.SearchMap
import org.codingforanimals.veganuniverse.create.presentation.place.composables.TryAgainAlertDialog
import org.koin.androidx.compose.koinViewModel


@Composable
internal fun CreatePlaceScreen(
    onCreateSuccess: () -> Unit,
    viewModel: CreatePlaceViewModel = koinViewModel(),
) {
    var isTryAgainDialogVisible by remember { mutableStateOf(false) }
    if (isTryAgainDialogVisible) {
        TryAgainAlertDialog(
            dismissDialog = { isTryAgainDialogVisible = false },
            tryAgain = {
                isTryAgainDialogVisible = false
                viewModel.onAction(Action.SubmitPlace)
            },
        )
    }

    val placesApiLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { viewModel.onAction(Action.OnPlaceSelected(it)) })

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
        showTryAgainDialog = { isTryAgainDialogVisible = true },
        imagePicker = imagePicker,
        addressPicker = placesApiLauncher,
        cameraPositionState = viewModel.uiState.cameraPositionState,
    )

    CreatePlaceScreen(
        items = viewModel.createPlaceFormItems,
        uiState = viewModel.uiState,
        onAction = { viewModel.onAction(it) },
    )

    AnimatedVisibility(visible = viewModel.uiState.isLoading) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable {}) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    viewModel.uiState.errorDialog?.let { errorData ->
        val onDismissRequest = { viewModel.onAction(Action.OnErrorDialogDismissRequest) }
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = stringResource(errorData.title)) },
            text = { Text(text = stringResource(errorData.message)) },
            confirmButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        )
    }
}

@Composable
private fun CreatePlaceScreen(
    items: List<CreatePlaceViewModel.CreatePlaceFormItem>,
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing_04)
    ) {
        items(
            items = items,
            itemContent = {
                with(uiState.form) {
                    when (it) {
                        Map -> SearchMap(uiState = uiState, onAction = onAction)
                        OpeningHoursField -> VUTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Spacing_06),
                            value = openingHours,
                            onValueChange = { onAction(Action.OnFormChange(openingHours = it)) },
                            isError = openingHoursError,
                            placeholder = "Horario de atención",
                            leadingIcon = VUIcons.Clock,
                        )
                        NameField -> VUTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Spacing_06),
                            value = name,
                            onValueChange = { onAction(Action.OnFormChange(name = it)) },
                            isError = nameError,
                            placeholder = "Nombre",
                        )
                        Picture -> ImagePicker(
                            imageUri = imageUri,
                            bitmap = bitmap,
                            onAction = onAction,
                        )
                    }
                }
            },
        )
    }
}

@Composable
fun VUTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    isError: Boolean = false,
    leadingIcon: Icon? = null,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        placeholder = placeholder?.let {
            { Text(text = placeholder) }
        },
        shape = ShapeDefaults.Medium,
        colors = VUTextFieldDefaults.colors(),
        leadingIcon = leadingIcon?.let {
            {
                VUIcon(
                    icon = leadingIcon,
                    contentDescription = ""
                )
            }
        }
    )
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    onCreateSuccess: () -> Unit,
    showTryAgainDialog: () -> Unit,
    imagePicker: ActivityResultLauncher<PickVisualMediaRequest>,
    addressPicker: ActivityResultLauncher<Intent>,
    cameraPositionState: CameraPositionState,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { effect ->
            when (effect) {
                SideEffect.NavigateToThankYouScreen -> {
                    onCreateSuccess()
                }
                SideEffect.ShowTryAgainDialog -> {
                    showTryAgainDialog()
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
            }
        }.collect()
    }
}
