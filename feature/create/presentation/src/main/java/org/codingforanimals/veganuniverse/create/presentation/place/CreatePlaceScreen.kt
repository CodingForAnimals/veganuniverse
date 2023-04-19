package org.codingforanimals.veganuniverse.create.presentation.place

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.shared.ItemDetailHero
import org.codingforanimals.veganuniverse.core.ui.shared.ItemDetailHeroColors
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.Action
import org.codingforanimals.veganuniverse.create.presentation.place.components.EnterLocation
import org.codingforanimals.veganuniverse.create.presentation.place.components.PlaceForm
import org.codingforanimals.veganuniverse.create.presentation.place.components.SelectPlaceType
import org.codingforanimals.veganuniverse.create.presentation.place.components.SelectTags
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun CreatePlaceScreen(
    viewModel: CreatePlaceViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val cropImage = rememberLauncherForActivityResult(
        contract = CropImageContract(),
        onResult = { result ->
            if (result.isSuccessful) {
                imageUri = result.uriContent
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

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Spacing_05),
    ) {
        item {
            ItemDetailHero(
                imageUri = imageUri,
                icon = VUIcons.PlacesFilled,
                onImageClick = {
                    imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                colors = if (imageUri == null) ItemDetailHeroColors.secondaryColors() else ItemDetailHeroColors.primaryColors(),
            )
        }
        item {
            PlaceForm(
                name = uiState.name,
                onNameChange = { viewModel.onAction(Action.OnFormChanged(name = it)) },
                openingHours = uiState.openingHours,
                onOpeningHoursChange = { viewModel.onAction(Action.OnFormChanged(openingHours = it)) },
                description = uiState.description,
                onDescriptionChange = { viewModel.onAction(Action.OnFormChanged(description = it)) },
            )
        }
        item {
            SelectPlaceType(
                selectedPlaceType = uiState.type,
                onButtonClick = { viewModel.onAction(Action.OnFormChanged(type = it)) },
            )
        }
        item {
            EnterLocation(
                address = uiState.address,
                location = uiState.location,
                onAddressChange = { viewModel.onAction(Action.OnFormChanged(address = it)) },
                addressCandidates = uiState.addressCandidates,
                onAddressSearch = { viewModel.onAction(Action.OnAddressSearch) },
                onDialogDismissed = { viewModel.onAction(Action.OnCandidatesDialogDismissed) },
                onCandidateSelected = { viewModel.onAction(Action.OnCandidateSelected(it)) }
            )
        }
        item {
            SelectTags()
        }
    }
}

