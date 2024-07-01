@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.create.place.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
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
import org.codingforanimals.veganuniverse.commons.navigation.Deeplink
import org.codingforanimals.veganuniverse.commons.navigation.DeeplinkNavigator
import org.codingforanimals.veganuniverse.create.place.domain.model.PlaceForm
import org.codingforanimals.veganuniverse.create.place.domain.usecase.SubmitPlace
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem
import org.codingforanimals.veganuniverse.create.place.presentation.error.CreatePlaceErrorDialog
import org.codingforanimals.veganuniverse.create.place.presentation.model.AddressField
import org.codingforanimals.veganuniverse.create.place.presentation.model.CreatePlaceOpeningHoursUI
import org.codingforanimals.veganuniverse.create.place.presentation.model.GetPlaceDataStatus
import org.codingforanimals.veganuniverse.create.place.presentation.model.LocationField
import org.codingforanimals.veganuniverse.create.place.presentation.model.OpeningHoursField
import org.codingforanimals.veganuniverse.create.place.presentation.model.PeriodEnd
import org.codingforanimals.veganuniverse.create.place.presentation.model.PeriodType
import org.codingforanimals.veganuniverse.create.place.presentation.model.SelectedTagsField
import org.codingforanimals.veganuniverse.create.place.presentation.model.TypeField
import org.codingforanimals.veganuniverse.create.place.presentation.usecase.GetAutoCompleteIntentUseCase
import org.codingforanimals.veganuniverse.create.place.presentation.usecase.GetCreatePlaceScreenContent
import org.codingforanimals.veganuniverse.create.place.presentation.usecase.GetPlaceDataUseCase
import org.codingforanimals.veganuniverse.commons.place.domain.model.DayOfWeek
import org.codingforanimals.veganuniverse.commons.place.shared.model.AddressComponents
import org.codingforanimals.veganuniverse.commons.place.shared.model.OpeningHours
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceTag
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceType
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.PictureField
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.StringField
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_not_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailResult

class CreatePlaceViewModel(
    getCreatePlaceScreenContent: GetCreatePlaceScreenContent,
    private val getPlaceDataUseCase: GetPlaceDataUseCase,
    private val getAutoCompleteIntentUseCase: GetAutoCompleteIntentUseCase,
    private val submitPlace: SubmitPlace,
    private val deeplinkNavigator: DeeplinkNavigator
) : ViewModel() {

    var uiState by mutableStateOf(UiState(content = getCreatePlaceScreenContent()))
        private set

    private val sideEffectChannel: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = sideEffectChannel.receiveAsFlow()

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    var showUnverifiedEmailDialog by mutableStateOf(false)
        private set

    fun onAction(action: Action) {
        when (action) {
            Action.OnSearchMapClick -> openAutoCompleteOverlay()
            Action.OnImagePickerClick -> openImageSelector()
            Action.OnErrorDialogDismissRequest -> dismissErrorDialog()
            is Action.OnPlaceSelected -> getPlaceData(action.activityResult)
            is Action.OnFormChange -> onFormChange(action)
            Action.OnSubmitClick -> attemptSubmitForm()
            Action.OnOpeningHoursEditButtonClick -> openOpeningHoursEditDialog()
            Action.OnOpeningHoursDismissEditDialog -> dismissOpeningHoursEditDialog()
            Action.OnTimePickerDismissed -> updateSelectedPeriod()
            is Action.EditPeriodButtonClick -> openTimePickerOnSelectedPeriod(action)
            is Action.OnDayOpenCloseSwitchClick -> updateDayOpenCloseStatus(action.day)
            is Action.OnChangeSplitPeriodClick -> updateDaySplitStatus(action.day)
            Action.OnHideExpandOpeningHoursClick -> updateHideExpandOpeningHoursState()
            Action.OnBackClick -> {
                viewModelScope.launch {
                    sideEffectChannel.send(SideEffect.NavigateUp)
                }
            }
        }
    }

    fun onUnverifiedEmailResult(result: UnverifiedEmailResult) {
        showUnverifiedEmailDialog = false
        when (result) {
            UnverifiedEmailResult.Dismissed -> Unit
            UnverifiedEmailResult.UnexpectedError -> {
                viewModelScope.launch {
                    snackbarEffectsChannel.send(
                        Snackbar(message = verification_email_not_sent)
                    )
                }
            }

            UnverifiedEmailResult.VerificationEmailSent -> {
                viewModelScope.launch {
                    snackbarEffectsChannel.send(
                        Snackbar(message = verification_email_sent)
                    )
                }
            }
        }
    }

    private fun openAutoCompleteOverlay() {
        viewModelScope.launch {
            sideEffectChannel.send(SideEffect.OpenAutoCompleteOverlay(getAutoCompleteIntentUseCase()))
        }
    }

    private fun openImageSelector() {
        viewModelScope.launch {
            sideEffectChannel.send(SideEffect.OpenImageSelector)
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
                    addressField = AddressField(
                        streetAddress = addressComponents.streetAddress.orEmpty(),
                        locality = addressComponents.locality.orEmpty(),
                        primaryAdminArea = addressComponents.primaryAdminArea.orEmpty(),
                        secondaryAdminArea = addressComponents.secondaryAdminArea.orEmpty(),
                        country = addressComponents.country.orEmpty(),
                    ),
                    isLoading = false
                )
                sideEffectChannel.send(SideEffect.ZoomInLocation(latLng))
            }

            is GetPlaceDataStatus.EstablishmentData -> with(placeDataStatus) {
                uiState = uiState.copy(
                    nameField = StringField(name),
                    openingHoursField = OpeningHoursField(
                        openingHours = openingHours.mapNotNull {
                            CreatePlaceOpeningHoursUI.fromModel(it)
                        }
                    ),
                    locationField = LocationField(latLng),
                    addressField = AddressField(
                        streetAddress = addressComponents.streetAddress.orEmpty(),
                        locality = addressComponents.locality.orEmpty(),
                        primaryAdminArea = addressComponents.primaryAdminArea.orEmpty(),
                        secondaryAdminArea = addressComponents.secondaryAdminArea.orEmpty(),
                        country = addressComponents.country.orEmpty(),
                    ),
                    pictureField = PictureField(placeDataStatus.bitmap),
                    isLoading = false
                )
                sideEffectChannel.send(SideEffect.ZoomInLocation(latLng))
            }

            GetPlaceDataStatus.PlaceTypeException -> {
                uiState = uiState.copy(
                    errorDialog = CreatePlaceErrorDialog.PlaceTypeErrorDialog,
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
                uiState =
                    uiState.copy(addressField = uiState.addressField?.copy(streetAddress = it))
            }
            name?.let { uiState = uiState.copy(nameField = StringField(it)) }
            type?.let { uiState = uiState.copy(typeField = TypeField(it)) }
            description?.let { uiState = uiState.copy(descriptionField = StringField(it)) }
            tag?.let {
                val tags = uiState.selectedTagsField.getUpdatedSelectedTags(tag)
                uiState = uiState.copy(selectedTagsField = tags)
            }
        }
    }

    private fun attemptSubmitForm() {
        val placeForm = uiState.toForm() ?: run {
            uiState = uiState.copy(isValidating = true)
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val result = submitPlace(placeForm)
            uiState = uiState.copy(isLoading = false)
            when (result) {
                SubmitPlace.Result.GuestUser -> {
                    viewModelScope.launch {
                        sideEffectChannel.send(SideEffect.NavigateToAuthenticateScreen)
                    }
                }

                SubmitPlace.Result.UnexpectedError -> {
                    uiState = uiState.copy(errorDialog = CreatePlaceErrorDialog.UnknownErrorDialog)
                }

                SubmitPlace.Result.UnverifiedEmail -> {
                    showUnverifiedEmailDialog = true
                }

                is SubmitPlace.Result.AlreadyExists -> {
                    uiState = uiState.copy(errorDialog = CreatePlaceErrorDialog.PlaceAlreadyExists)
                }

                is SubmitPlace.Result.Success -> {
                    viewModelScope.launch {
                        sideEffectChannel.send(SideEffect.NavigateToThankYouScreen)
                    }
                }

                SubmitPlace.Result.UserMustReauthenticate -> {
                    deeplinkNavigator.navigate(Deeplink.Reauthentication)
                }
            }
        }
    }

    private fun openOpeningHoursEditDialog() {
        uiState = uiState.copy(
            openingHoursField = uiState.openingHoursField.copy(isEditing = true),
        )
    }

    private fun dismissOpeningHoursEditDialog() {
        uiState = uiState.copy(
            openingHoursField = uiState.openingHoursField.copy(isEditing = false),
        )
    }

    private fun updateSelectedPeriod() {
        uiState.openingHoursTimePickerState?.let { state ->
            val updatedHour = state.state.hour
            val updatedMinute = state.state.minute
            val day =
                uiState.openingHoursField.sortedOpeningHours.first { it.dayOfWeek == state.dayOfWeek }
            val updatedDay = when {
                state.periodEnd == PeriodEnd.FROM && state.periodType == PeriodType.MAIN -> day.copy(
                    mainPeriod = day.mainPeriod.copy(
                        openingHour = updatedHour,
                        openingMinute = updatedMinute,
                    )
                )

                state.periodEnd == PeriodEnd.TO && state.periodType == PeriodType.MAIN -> day.copy(
                    mainPeriod = day.mainPeriod.copy(
                        closingHour = updatedHour,
                        closingMinute = updatedMinute,
                    )
                )

                state.periodEnd == PeriodEnd.FROM && state.periodType == PeriodType.SECONDARY -> day.copy(
                    secondaryPeriod = day.secondaryPeriod.copy(
                        openingHour = updatedHour,
                        openingMinute = updatedMinute,
                    )
                )

                state.periodEnd == PeriodEnd.TO && state.periodType == PeriodType.SECONDARY -> day.copy(
                    secondaryPeriod = day.secondaryPeriod.copy(
                        closingHour = updatedHour,
                        closingMinute = updatedMinute,
                    )
                )

                else -> CreatePlaceOpeningHoursUI(day.dayOfWeek)
            }
            val updatedOpeningHours = uiState.openingHoursField.sortedOpeningHours.toMutableList()
            if (updatedOpeningHours.remove(day)) {
                updatedOpeningHours.add(updatedDay)
            }
            uiState = uiState.copy(
                openingHoursField = uiState.openingHoursField.copy(openingHours = updatedOpeningHours),
                openingHoursTimePickerState = null,
            )
        }
    }

    private fun openTimePickerOnSelectedPeriod(action: Action.EditPeriodButtonClick) {
        val day = uiState.openingHoursField.sortedOpeningHours.first { it.dayOfWeek == action.day }
        val period = when (action.periodType) {
            PeriodType.MAIN -> day.mainPeriod
            PeriodType.SECONDARY -> day.secondaryPeriod
        }
        val (hours, minutes) = when (action.periodEnd) {
            PeriodEnd.FROM -> Pair(period.openingHour, period.openingMinute)
            PeriodEnd.TO -> Pair(period.closingHour, period.closingMinute)
        }
        val state = OpeningHoursTimePickerState(
            dayOfWeek = action.day,
            periodEnd = action.periodEnd,
            periodType = action.periodType,
            state = TimePickerState(hours, minutes, true)
        )
        uiState = uiState.copy(openingHoursTimePickerState = state)
    }

    private fun updateDayOpenCloseStatus(dayOfWeek: DayOfWeek) {
        val day = uiState.openingHoursField.sortedOpeningHours.first { it.dayOfWeek == dayOfWeek }
        val updatedDay = day.copy(isClosed = !day.isClosed)
        val list = uiState.openingHoursField.sortedOpeningHours.toMutableList()
        if (list.remove(day)) {
            list.add(updatedDay)
        }
        val newOpeningHours = uiState.openingHoursField.copy(openingHours = list)
        uiState = uiState.copy(openingHoursField = newOpeningHours)
    }

    private fun updateDaySplitStatus(dayOfWeek: DayOfWeek) {
        val day = uiState.openingHoursField.sortedOpeningHours.first { it.dayOfWeek == dayOfWeek }
        val newDay = day.copy(isSplit = !day.isSplit)
        val list = uiState.openingHoursField.sortedOpeningHours.toMutableList()
        if (list.remove(day)) {
            list.add(newDay)
        }
        val newOpeningHours = uiState.openingHoursField.copy(openingHours = list)
        uiState = uiState.copy(openingHoursField = newOpeningHours)
    }

    private fun updateHideExpandOpeningHoursState() {
        val openingHours = uiState.openingHoursField
        val updatedOpeningHours = openingHours.copy(isExpanded = !openingHours.isExpanded)
        uiState = uiState.copy(openingHoursField = updatedOpeningHours)
    }

    data class OpeningHoursTimePickerState(
        val dayOfWeek: DayOfWeek,
        val periodEnd: PeriodEnd,
        val periodType: PeriodType,
        val state: TimePickerState,
    )

    data class UiState(
        val content: List<CreatePlaceFormItem> = emptyList(),
        val cameraPositionState: CameraPositionState = CameraPositionState(defaultCameraPosition),
        val errorDialog: CreatePlaceErrorDialog? = null,
        val locationField: LocationField = LocationField(),
        val addressField: AddressField? = null,
        val nameField: StringField = StringField(),
        val openingHoursField: OpeningHoursField = OpeningHoursField(),
        val openingHoursTimePickerState: OpeningHoursTimePickerState? = null,
        val pictureField: PictureField = PictureField(),
        val typeField: TypeField = TypeField(),
        val descriptionField: StringField = StringField(),
        val selectedTagsField: SelectedTagsField = SelectedTagsField(),
        val isLoading: Boolean = false,
        val isValidating: Boolean = false,
    ) {

        fun toForm(): PlaceForm? {
            return PlaceForm(
                name = nameField.value.trim().ifBlank { return null },
                imageModel = pictureField.model ?: return null,
                addressComponents = addressField?.toAddressComponents() ?: return null,
                openingHours = openingHoursField.sortedOpeningHours.map { it.toModel() },
                description = descriptionField.value.ifBlank { return null },
                type = typeField.value ?: return null,
                latitude = locationField.latLng?.latitude ?: return null,
                longitude = locationField.latLng.longitude,
                tags = selectedTagsField.tags
            )
        }


        private fun AddressField.toAddressComponents(): AddressComponents {
            return AddressComponents(
                streetAddress = streetAddress.ifBlank { throw IllegalArgumentException() },
                locality = locality,
                primaryAdminArea = primaryAdminArea,
                secondaryAdminArea = secondaryAdminArea,
                country = country
            )
        }

        private fun CreatePlaceOpeningHoursUI.toModel(): OpeningHours {
            return OpeningHours(
                dayOfWeek = dayOfWeek.name,
                mainPeriod = mainPeriod.takeIf { !isClosed },
                secondaryPeriod = secondaryPeriod.takeIf { isSplit },
            )
        }

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

        data object OnSubmitClick : Action()
        data object OnOpeningHoursEditButtonClick : Action()
        data object OnOpeningHoursDismissEditDialog : Action()
        data object OnImagePickerClick : Action()
        data object OnSearchMapClick : Action()
        data class OnPlaceSelected(val activityResult: ActivityResult) : Action()
        data object OnErrorDialogDismissRequest : Action()
        data object OnTimePickerDismissed : Action()
        data class EditPeriodButtonClick(
            val day: DayOfWeek,
            val periodEnd: PeriodEnd,
            val periodType: PeriodType,
        ) : Action()

        data object OnHideExpandOpeningHoursClick : Action()
        data object OnBackClick : Action()

        data class OnDayOpenCloseSwitchClick(val day: DayOfWeek) : Action()
        data class OnChangeSplitPeriodClick(val day: DayOfWeek) : Action()
    }

    sealed class SideEffect {
        data object ShowVerifyEmailPrompt : SideEffect()
        data object NavigateToAuthenticateScreen : SideEffect()
        data object NavigateToThankYouScreen : SideEffect()
        data object OpenImageSelector : SideEffect()
        data object NavigateUp : SideEffect()

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