package org.codingforanimals.veganuniverse.create.presentation.place

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.annotation.StringRes
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
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.create.domain.ContentCreator
import org.codingforanimals.veganuniverse.create.domain.place.PlaceFormDomainEntity
import org.codingforanimals.veganuniverse.create.presentation.R
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.CreatePlaceFormItem.Map
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.CreatePlaceFormItem.NameField
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.CreatePlaceFormItem.OpeningHoursField
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.CreatePlaceFormItem.Picture
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetAutoCompleteIntentUseCase
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetPlaceDataStatus
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetPlaceDataUseCase

private const val TAG = "CreatePlaceViewModel"

internal class CreatePlaceViewModel(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val contentCreator: ContentCreator,
    private val getPlaceDataUseCase: GetPlaceDataUseCase,
    private val getAutoCompleteIntentUseCase: GetAutoCompleteIntentUseCase,
) : ViewModel() {

    private val ioDispatcher = coroutineDispatcherProvider.io()

    var uiState by mutableStateOf(UiState())

    private val _sideEffects: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = _sideEffects.receiveAsFlow()

    var icon: Bitmap? = null

    val createPlaceFormItems = listOf(
        Map,
        NameField,
        OpeningHoursField,
        Picture,
    )

    sealed class CreatePlaceFormItem {
        object Map : CreatePlaceFormItem()
        object NameField : CreatePlaceFormItem()
        object OpeningHoursField : CreatePlaceFormItem()
        object Picture : CreatePlaceFormItem()
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.OnFormChange -> onFormChange(action)
            is Action.OnTagClick -> onTagClick(action.tag)
            is Action.OnPlaceSelected -> getPlaceData(action.activityResult)
            Action.OnSearchMapClick -> openAutoCompleteOverlay()
            Action.OnImagePickerClick -> openImageSelector()
            Action.OnErrorDialogDismissRequest -> dismissErrorDialog()
            Action.SubmitPlace -> submit()
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
            GetPlaceDataStatus.EstablishmentPictureException -> {
                val form = uiState.form.copy(bitmap = null)
                uiState = uiState.copy(form = form, isLoading = false)
            }
            is GetPlaceDataStatus.EstablishmentData -> {
                val form = Form(
                    name = placeDataStatus.name,
                    openingHours = placeDataStatus.openingHours,
                )
                val location = Location(placeDataStatus.latLng, placeDataStatus.address)
                uiState =
                    uiState.copy(form = form, location = location, isLoading = false)
                _sideEffects.send(SideEffect.ZoomInLocation(placeDataStatus.latLng))
            }
            is GetPlaceDataStatus.EstablishmentPicture -> {
                val form = uiState.form.copy(bitmap = placeDataStatus.bitmap)
                uiState = uiState.copy(isLoading = false, form = form)
            }
            GetPlaceDataStatus.Loading -> {
                uiState = uiState.copy(isLoading = true)
            }
            is GetPlaceDataStatus.StreetAddressData -> {
                val location = Location(placeDataStatus.latLng, placeDataStatus.address)
                uiState = uiState.copy(location = location, isLoading = false)
                _sideEffects.send(SideEffect.ZoomInLocation(placeDataStatus.latLng))
            }
            GetPlaceDataStatus.PlaceTypeException -> {
                uiState =
                    uiState.copy(errorDialog = ErrorDialog.PlaceTypeErrorDialog, isLoading = false)
            }
            GetPlaceDataStatus.MissingCriticalFieldException -> {
                uiState =
                    uiState.copy(
                        errorDialog = ErrorDialog.MissingCriticalFieldErrorDialog,
                        isLoading = false
                    )
            }
            GetPlaceDataStatus.UnknownException -> {
                uiState =
                    uiState.copy(errorDialog = ErrorDialog.UnknownErrorDialog, isLoading = false)
            }
        }
    }

    private fun onTagClick(tag: PlaceTag) {
        val tags = uiState.form.selectedTags.toMutableList()
        if (!tags.remove(tag)) {
            tags.add(tag)
        }
        uiState = uiState.copy(form = uiState.form.copy(selectedTags = tags))
    }

    private fun onFormChange(onFormChange: Action.OnFormChange) {
        with(onFormChange) {
            val form = uiState.form
            imageUri?.let {
                uiState = uiState.copy(
                    form = form.copy(
                        imageUri = it,
                        imageUriError = false,
                        bitmap = null
                    )
                )
            }
            type?.let {
                uiState = uiState.copy(form = form.copy(type = it, typeError = false))
            }
            name?.let {
                uiState = uiState.copy(form = form.copy(name = it, nameError = false))
            }
            description?.let {
                uiState = uiState.copy(form = form.copy(description = it, descriptionError = false))
            }
            openingHours?.let {
                uiState =
                    uiState.copy(form = form.copy(openingHours = it, openingHoursError = false))
            }
            address?.let {
                uiState = uiState.copy(
                    form = form.copy(location = null, address = it, addressError = false)
                )
            }
        }
        uiState = uiState.copy(isPublishButtonEnabled = true)
    }

    private fun createForm(): PlaceFormDomainEntity? =
        try {
            with(uiState.form) {
                PlaceFormViewEntity(
                    type = type!!,
                    name = name,
                    openingHours = openingHours,
                    description = description,
                    address = address,
                    latitude = location!!.latitude,
                    longitude = location.longitude,
                    tags = selectedTags,
                ).toDomainEntity()
            }
        } catch (e: Throwable) {
            Log.e("CreatePlaceViewModel.kt", "Invalid form. Msg: ${e.stackTraceToString()}")
            reviewFormValidity()
            null
        }

    private fun submit() {
        if (!reviewFormValidity()) return
        val form = createForm() ?: return
        uiState = uiState.copy(isLoading = true)
        viewModelScope.launch {
            val createPlaceResult = withContext(ioDispatcher) { contentCreator.createPlace(form) }
            uiState = uiState.copy(isLoading = false)
            val resultEvent = if (createPlaceResult.isSuccess) {
                SideEffect.NavigateToThankYouScreen
            } else {
                createPlaceResult.exceptionOrNull()?.let {
                    Log.e(TAG, "Error creating place. ${it.stackTraceToString()}")
                }
                SideEffect.ShowTryAgainDialog
            }
            _sideEffects.send(resultEvent)
        }
    }


    private fun reviewFormValidity(): Boolean =
        with(uiState.form) {
            val imageUriError = imageUri == null
            val typeError = type == null
            val nameError = name.isBlank()
            val descriptionError = description.isBlank()
            val openingHoursError = openingHours.isBlank()
            val addressError = address.isBlank()
            val locationError = location == null

            val isFormInvalid =
                imageUriError || typeError || nameError || descriptionError || openingHoursError || addressError || locationError
            if (isFormInvalid) {
                val form = uiState.form
                uiState = uiState.copy(
                    form = form.copy(
                        imageUriError = imageUriError,
                        typeError = typeError,
                        nameError = nameError,
                        descriptionError = descriptionError,
                        openingHoursError = openingHoursError,
                        addressError = addressError,
                        locationError = locationError,
                    ),
                    isPublishButtonEnabled = false,
                )
                return@with false
            }
            return true
        }

    data class UiState(
        val form: Form = Form(),
        val location: Location? = null,
        val errorDialog: ErrorDialog? = null,
        val cameraPositionState: CameraPositionState = CameraPositionState(defaultCameraPosition),
        val isLoading: Boolean = false,
        val isPublishButtonEnabled: Boolean = true,
    ) {
        companion object {
            private val defaultCameraPosition = CameraPosition.fromLatLngZoom(
                LatLng(-34.0, -64.0),
                5f
            )
        }
    }

    data class Location(
        val latLng: LatLng,
        val address: String,
    )

    data class Form(
        val imageUri: Uri? = null,
        val bitmap: Bitmap? = null,
        val imageUriError: Boolean = false,
        val type: PlaceType? = null,
        val typeError: Boolean = false,
        val name: String = "",
        val nameError: Boolean = false,
        val openingHours: String = "",
        val openingHoursError: Boolean = false,
        val description: String = "",
        val descriptionError: Boolean = false,
        val address: String = "",
        val addressError: Boolean = false,
        val location: LatLng? = null,
        val locationError: Boolean = false,
        val selectedTags: List<PlaceTag> = emptyList(),
    )

    sealed class ErrorDialog(@StringRes val title: Int, @StringRes val message: Int) {
        object UnknownErrorDialog : ErrorDialog(
            title = R.string.error_title_unknown,
            message = R.string.error_message_unknown,
        )

        object PlaceTypeErrorDialog :
            ErrorDialog(
                title = R.string.error_title_place_type,
                message = R.string.error_message_place_type
            )

        object MissingCriticalFieldErrorDialog :
            ErrorDialog(
                title = R.string.error_title_missing_critical_field,
                message = R.string.error_message_missing_critical_field
            )
    }

    sealed class Action {
        data class OnFormChange(
            val imageUri: Uri? = null,
            val name: String? = null,
            val openingHours: String? = null,
            val description: String? = null,
            val type: PlaceType? = null,
            val address: String? = null,
            val location: LatLng? = null,
        ) : Action()

        object SubmitPlace : Action()
        data class OnTagClick(val tag: PlaceTag) : Action()
        object OnImagePickerClick : Action()
        object OnSearchMapClick : Action()
        data class OnPlaceSelected(val activityResult: ActivityResult) : Action()
        object OnErrorDialogDismissRequest : Action()
    }

    sealed class SideEffect {
        object NavigateToThankYouScreen : SideEffect()
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