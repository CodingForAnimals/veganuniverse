package org.codingforanimals.veganuniverse.create.presentation.place

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.core.ui.viewmodel.PictureField
import org.codingforanimals.veganuniverse.core.ui.viewmodel.StringField
import org.codingforanimals.veganuniverse.create.domain.place.PlaceFormDomainEntity
import org.codingforanimals.veganuniverse.create.presentation.model.AddressField
import org.codingforanimals.veganuniverse.create.presentation.model.LocationField
import org.codingforanimals.veganuniverse.create.presentation.model.SelectedTagsField
import org.codingforanimals.veganuniverse.create.presentation.model.TypeField
import org.codingforanimals.veganuniverse.create.presentation.place.entity.toPlaceForm
import org.codingforanimals.veganuniverse.create.presentation.place.error.CreatePlaceErrorDialog
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.CreatePlaceUseCase
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetAutoCompleteIntentUseCase
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetCreatePlaceScreenContent
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetPlaceDataStatus
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetPlaceDataUseCase

private const val TAG = "CreatePlaceViewModel"

internal class CreatePlaceViewModel(
    getCreatePlaceScreenContent: GetCreatePlaceScreenContent,
    private val getPlaceDataUseCase: GetPlaceDataUseCase,
    private val getAutoCompleteIntentUseCase: GetAutoCompleteIntentUseCase,
    private val createPlaceUseCase: CreatePlaceUseCase,
) : ViewModel() {

    var uiState by mutableStateOf(UiState())

    private val _sideEffects: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = _sideEffects.receiveAsFlow()

    var icon: Bitmap? = null

    val createPlaceFormItems = getCreatePlaceScreenContent()

    fun onAction(action: Action) {
        when (action) {
            Action.OnSearchMapClick -> openAutoCompleteOverlay()
            Action.OnImagePickerClick -> openImageSelector()
            Action.OnErrorDialogDismissRequest -> dismissErrorDialog()
            is Action.OnPlaceSelected -> getPlaceData(action.activityResult)
            is Action.OnFormChange -> onFormChange(action)
            Action.OnSubmitClick -> onSubmitClick()
        }
    }

    private fun openAutoCompleteOverlay() {
        viewModelScope.launch {
            _sideEffects.send(SideEffect.OpenAutoCompleteOverlay(getAutoCompleteIntentUseCase()))
        }
    }

    private fun openImageSelector() {
        viewModelScope.launch {
            _sideEffects.send(SideEffect.OpenImageSelector)
        }
    }

    private fun dismissErrorDialog() {
        uiState = uiState.copy(errorDialog = null, isLoading = false)
    }

    private fun getPlaceData(activityResult: ActivityResult) {
        val intent = activityResult.data
        if (activityResult.resultCode == Activity.RESULT_OK && intent != null) {
            viewModelScope.launch {
                getPlaceDataUseCase(intent).collectLatest { placeDataStatus ->
                    updateUiWithPlaceDataStatus(placeDataStatus)
                }
            }
        }
    }

    private suspend fun updateUiWithPlaceDataStatus(placeDataStatus: GetPlaceDataStatus) {
        when (placeDataStatus) {
            GetPlaceDataStatus.Loading -> {
                uiState = uiState.copy(isLoading = true)
            }
            is GetPlaceDataStatus.StreetAddressData -> with(placeDataStatus) {
                uiState = uiState.copy(
                    locationField = LocationField(latLng),
                    isLoading = false
                )
                _sideEffects.send(SideEffect.ZoomInLocation(latLng))
            }
            is GetPlaceDataStatus.EstablishmentData -> with(placeDataStatus) {
                uiState = uiState.copy(
                    nameField = StringField(name),
                    openingHoursField = StringField(openingHours),
                    locationField = LocationField(latLng),
                    addressField = AddressField(
                        streetAddress = streetAddress,
                        locality = locality,
                        province = province,
                        country = country,
                    ),
                    isLoading = false
                )
                _sideEffects.send(SideEffect.ZoomInLocation(latLng))
            }
            is GetPlaceDataStatus.EstablishmentPicture -> {
                uiState = uiState.copy(
                    pictureField = PictureField(placeDataStatus.bitmap),
                    isLoading = false
                )
            }
            GetPlaceDataStatus.EstablishmentPictureException -> {
                uiState = uiState.copy(pictureField = PictureField(), isLoading = false)
            }
            GetPlaceDataStatus.PlaceTypeException -> {
                uiState = uiState.copy(
                    errorDialog = CreatePlaceErrorDialog.PlaceTypeErrorDialog,
                    isLoading = false
                )
            }
            GetPlaceDataStatus.MissingCriticalFieldException -> {
                uiState = uiState.copy(
                    errorDialog = CreatePlaceErrorDialog.MissingCriticalFieldErrorDialog,
                    isLoading = false
                )
            }
            GetPlaceDataStatus.UnknownException -> {
                uiState = uiState.copy(
                    errorDialog = CreatePlaceErrorDialog.UnknownErrorDialog,
                    isLoading = false,
                )
            }
        }
    }

    private fun onFormChange(onFormChange: Action.OnFormChange) {
        with(onFormChange) {
            imageUri?.let { uiState = uiState.copy(pictureField = PictureField(it)) }
            address?.let {
                uiState = uiState.copy(addressField = uiState.addressField.copy(streetAddress = it))
            }
            name?.let { uiState = uiState.copy(nameField = StringField(it)) }
            openingHours?.let { uiState = uiState.copy(openingHoursField = StringField(it)) }
            type?.let { uiState = uiState.copy(typeField = TypeField(it)) }
            description?.let { uiState = uiState.copy(descriptionField = StringField(it)) }
            tag?.let {
                val tags = uiState.selectedTagsField.getUpdatedSelectedTags(tag)
                uiState = uiState.copy(selectedTagsField = tags)
            }
        }
    }

    private fun onSubmitClick() {
        val form = uiState.toPlaceForm()
        if (form == null) {
            uiState = uiState.copy(
                isValidating = true,
                errorDialog = CreatePlaceErrorDialog.InvalidFormErrorDialog,
            )
        } else {
            submitForm(form)
        }
    }

    private fun submitForm(formDomainEntity: PlaceFormDomainEntity) {
        viewModelScope.launch {
            createPlaceUseCase(formDomainEntity).collectLatest { createPlaceStatus ->
                when (createPlaceStatus) {
                    CreatePlaceUseCase.Status.Loading -> {
                        uiState = uiState.copy(isLoading = true)
                    }
                    CreatePlaceUseCase.Status.Success -> {
                        uiState = uiState.copy(isLoading = false)
                        _sideEffects.send(SideEffect.NavigateToThankYouScreen)
                    }
                    CreatePlaceUseCase.Status.PlaceAlreadyExists -> {
                        uiState = uiState.copy(
                            isLoading = false,
                            errorDialog = CreatePlaceErrorDialog.PlaceAlreadyExists {
                                _sideEffects.send(SideEffect.NavigateToAlreadyExistingPlace)
                            },
                        )
                    }
                    CreatePlaceUseCase.Status.UnknownError -> {
                        uiState = uiState.copy(
                            isLoading = false,
                            errorDialog = CreatePlaceErrorDialog.UnknownErrorDialog,
                        )
                    }
                }
            }
        }
    }

    data class UiState(
        val cameraPositionState: CameraPositionState = CameraPositionState(defaultCameraPosition),
        val errorDialog: CreatePlaceErrorDialog? = null,
        val locationField: LocationField = LocationField(),
        val addressField: AddressField = AddressField(),
        val nameField: StringField = StringField(),
        val openingHoursField: StringField = StringField(),
        val pictureField: PictureField = PictureField(),
        val typeField: TypeField = TypeField(),
        val descriptionField: StringField = StringField(),
        val selectedTagsField: SelectedTagsField = SelectedTagsField(),
        val isLoading: Boolean = false,
        val isValidating: Boolean = false,
    ) {

        companion object {
            private val defaultCameraPosition = CameraPosition.fromLatLngZoom(
                LatLng(-34.0, -64.0),
                4f,
            )
        }
    }

    sealed class Action {
        data class OnFormChange(
            val imageUri: Uri? = null,
            val address: String? = null,
            val name: String? = null,
            val openingHours: String? = null,
            val description: String? = null,
            val type: PlaceType? = null,
            val tag: PlaceTag? = null,
        ) : Action()

        object OnSubmitClick : Action()
        object OnImagePickerClick : Action()
        object OnSearchMapClick : Action()
        data class OnPlaceSelected(val activityResult: ActivityResult) : Action()
        object OnErrorDialogDismissRequest : Action()
    }

    sealed class SideEffect {
        object NavigateToThankYouScreen : SideEffect()
        object NavigateToAlreadyExistingPlace : SideEffect()
        object ShowTryAgainDialog : SideEffect()
        object OpenImageSelector : SideEffect()
        data class OpenAutoCompleteOverlay(val autocompleteIntent: Intent) : SideEffect()
        data class ZoomInLocation(private val latLng: LatLng) : SideEffect() {
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ANIMATION_ZOOM)
            val duration = DEFAULT_ANIMATION_DURATION

            companion object {
                private const val DEFAULT_ANIMATION_ZOOM = 15f
                private const val DEFAULT_ANIMATION_DURATION = 750
            }
        }
    }
}