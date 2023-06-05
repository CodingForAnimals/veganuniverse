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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.shared.ItemDetailHero
import org.codingforanimals.veganuniverse.core.ui.shared.ItemDetailHeroColors
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_10
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.Action
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.SideEffect
import org.codingforanimals.veganuniverse.create.presentation.place.composables.EnterLocation
import org.codingforanimals.veganuniverse.create.presentation.place.composables.PlaceForm
import org.codingforanimals.veganuniverse.create.presentation.place.composables.SelectTags
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

    val placesApiLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { viewModel.onAction(Action.OnAddressSelected(it)) }
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
        showTryAgainDialog = { isTryAgainDialogVisible = true },
        imagePicker = imagePicker,
        addressPicker = placesApiLauncher,
    )

    val uiState = viewModel.uiState

    CreatePlaceScreen(
        uiState = uiState,
        onAction = { viewModel.onAction(it) }
    )

    AnimatedVisibility(visible = uiState.isLoading) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable {}) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun CreatePlaceScreen(
    uiState: CreatePlaceViewModel.UiState,
    onAction: (Action) -> Unit,
) {
    LazyColumn {
        item {
            val heroColors = if (uiState.form.imageUri == null) {
                if (uiState.form.imageUriError) {
                    ItemDetailHeroColors.errorColors()
                } else {
                    ItemDetailHeroColors.secondaryColors()
                }
            } else {
                ItemDetailHeroColors.primaryColors()
            }
            ItemDetailHero(
                imageUri = uiState.form.imageUri,
                bitmap = uiState.form.bitmap,
                icon = VUIcons.LocationFilled,
                onImageClick = { onAction(Action.OnImageClick) },
                colors = heroColors,
            )
            PlaceForm(
                form = uiState.form,
                onTypeSelect = { onAction(Action.OnFormChange(type = it)) },
                onNameChange = { onAction(Action.OnFormChange(name = it)) },
                onOpeningHoursChange = { onAction(Action.OnFormChange(openingHours = it)) },
                onDescriptionChange = { onAction(Action.OnFormChange(description = it)) },
            )
            EnterLocation(
                address = uiState.form.address,
                addressError = uiState.form.addressError,
                location = uiState.form.location,
                locationError = uiState.form.locationError,
                onAction = onAction,
                onAddressChange = { onAction(Action.OnFormChange(address = it)) },
                addressCandidates = uiState.addressCandidates,
                onAddressSearch = { onAction(Action.OnAddressSearch) },
                onDialogDismissed = { onAction(Action.OnCandidatesDialogDismissed) },
                onCandidateSelected = { onAction(Action.OnCandidateSelected(it)) },
            )
            SelectTags(
                selectedTags = uiState.form.selectedTags,
                onTagClick = { onAction(Action.OnTagClick(it)) }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Button(
                    modifier = Modifier.padding(horizontal = Spacing_05),
                    onClick = { onAction(Action.SubmitPlace) },
                    enabled = uiState.isPublishButtonEnabled,
                ) {
                    Text(text = "Publicar")
                }
            }
            Spacer(modifier = Modifier.height(Spacing_10))
        }
    }
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    onCreateSuccess: () -> Unit,
    showTryAgainDialog: () -> Unit,
    imagePicker: ActivityResultLauncher<PickVisualMediaRequest>,
    addressPicker: ActivityResultLauncher<Intent>
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                SideEffect.NavigateToThankYouScreen -> {
                    onCreateSuccess()
                }
                SideEffect.ShowTryAgainDialog -> {
                    showTryAgainDialog()
                }
                SideEffect.OpenImageSelector -> {
                    imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
                SideEffect.OpenAddressSearchOverlay -> {
                    Places.initialize(context, "AIzaSyATDgK_LBizEIapwfzy90-_BI4tVAyKenE")
                    val fields = listOf(Place.Field.ID)
                    val intent =
                        Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                            .build(context)
                    addressPicker.launch(intent)
                }
            }
        }.collect()
    }
}
