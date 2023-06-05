package org.codingforanimals.veganuniverse.create.presentation.place

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.widget.Autocomplete
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.create.domain.ContentCreator
import org.codingforanimals.veganuniverse.create.domain.place.PlaceFormDomainEntity

private const val TAG = "CreatePlaceViewModel"

internal class CreatePlaceViewModel(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    context: Context,
    private val geocoder: Geocoder,
    private val contentCreator: ContentCreator,
) : ViewModel() {

    private val ioDispatcher = coroutineDispatcherProvider.io()
    private val mainDispatcher = coroutineDispatcherProvider.main()

    var uiState by mutableStateOf(UiState.initState())

    private val _sideEffects: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = _sideEffects.receiveAsFlow()

    private var searchJob: Job? = null

    var icon: Bitmap? = null

    private val googlePlacesApi by lazy { Places.createClient(context) }

    fun onAction(action: Action) {
        when (action) {
            is Action.OnFormChange -> onFormChange(action)
            Action.OnAddressSearch -> fetchAddressCandidates()
            Action.OnCandidatesDialogDismissed -> {
                uiState = uiState.copy(addressCandidates = emptyList())
            }
            is Action.OnCandidateSelected -> {
                val newAddress = uiState.addressCandidates[action.index]
                uiState = uiState.copy(
                    form = uiState.form.copy(
                        address = newAddress.address,
                        location = newAddress.latLng,
                        locationError = false,
                    ),
                    addressCandidates = emptyList(),
                )
            }
            Action.SubmitPlace -> submit()
            is Action.OnTagClick -> onTagClick(action.tag)
            Action.OnLocationFieldClick -> {
                viewModelScope.launch {
                    _sideEffects.send(SideEffect.OpenAddressSearchOverlay)
                }
            }
            Action.OnImageClick -> {
                viewModelScope.launch {
                    _sideEffects.send(SideEffect.OpenImageSelector)
                }
            }
            is Action.OnAddressSelected -> {
                val intent = action.activityResult.data
                if (action.activityResult.resultCode == Activity.RESULT_OK && intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    val metadata = place.photoMetadatas
                    if (metadata == null || metadata.isEmpty()) {
                        Log.e("pepe", "photo metadata vacia paaa")
                        return
                    }
                    val data = metadata.first()
                    val attributions = data.attributions
                    Log.e("pepe", "attributions $attributions")
                    val photoRequest = FetchPhotoRequest.builder(data).build()
                    googlePlacesApi.fetchPhoto(photoRequest)
                        .addOnSuccessListener {
                            Log.e("pepe", "bitmap success")
                            uiState = uiState.copy(form = uiState.form.copy(bitmap = it.bitmap))
                        }
                        .addOnFailureListener {
                            Log.e("pepe", "error pa ${it.stackTraceToString()}")
                        }

                    Log.e("pepe", "place $place")

                    place.types?.let { types ->
                        when {
                            types.contains(Place.Type.ESTABLISHMENT) -> {
                                Log.e("pepe", "establishment pa $types")
                            }
                            types.contains(Place.Type.STREET_ADDRESS) -> {
                                Log.e("pepe", "street number $types")
                            }
                            else -> {
                                Log.e("pepe", "otro $types")
                            }
                        }
                    }
                } else {
                    Log.e("pepe", "error pa ${action.activityResult.data?.extras}")
                }
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
                uiState = uiState.copy(form = form.copy(imageUri = it, imageUriError = false))
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

    private fun fetchAddressCandidates() {
        val address = uiState.form.address
        if (address.isBlank()) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocationName(address, 5) { data ->
                data.mapToPlaceAddress()?.let { candidates ->
                    handleAddressCandidatesStateUpdate(candidates)
                }
            }
        } else {
            searchJob?.cancel()
            searchJob = viewModelScope.launch(ioDispatcher) {
                val data = geocoder.getFromLocationName(address, 5)
                data?.mapToPlaceAddress()?.let { candidates ->
                    withContext(mainDispatcher) {
                        handleAddressCandidatesStateUpdate(candidates)
                    }
                }
            }
        }
    }

    private fun handleAddressCandidatesStateUpdate(candidates: List<PlaceAddress>) {
        if (candidates.size == 1) {
            val onlyCandidate = candidates.first()
            uiState = uiState.copy(
                form = uiState.form.copy(
                    address = onlyCandidate.address,
                    location = onlyCandidate.latLng,
                    locationError = false,
                ),
            )
        } else {
            uiState = uiState.copy(addressCandidates = candidates)
        }
    }

    private fun List<Address>.mapToPlaceAddress() = mapNotNull {
        if (it.thoroughfare == null ||
            it.subThoroughfare == null ||
            it.locality == null
        ) return@mapNotNull null
        PlaceAddress(
            address = it.getMappedAddress(),
            latLng = it.getLatLng(),
        )
    }.takeUnless { it.isEmpty() }

    private fun Address.getMappedAddress() =
        "$thoroughfare $subThoroughfare, $locality"

    private fun Address.getLatLng() = LatLng(latitude, longitude)

    data class UiState(
        val form: Form,
        val selectedTags: List<PlaceTag>,
        val addressCandidates: List<PlaceAddress>,
        val isLoading: Boolean,
        val isPublishButtonEnabled: Boolean,
    ) {
        companion object {
            fun initState() = UiState(
                form = Form.initialState(),
                selectedTags = emptyList(),
                addressCandidates = emptyList(),
                isLoading = false,
                isPublishButtonEnabled = true,
            )
        }
    }

    data class Form(
        val imageUri: Uri?,
        val bitmap: Bitmap?,
        val imageUriError: Boolean,
        val type: PlaceType?,
        val typeError: Boolean,
        val name: String,
        val nameError: Boolean,
        val openingHours: String,
        val openingHoursError: Boolean,
        val description: String,
        val descriptionError: Boolean,
        val address: String,
        val addressError: Boolean,
        val location: LatLng?,
        val locationError: Boolean,
        val selectedTags: List<PlaceTag>,
    ) {
        companion object {
            fun initialState() = Form(
                imageUri = null,
                bitmap = null,
                imageUriError = false,
                type = null,
                typeError = false,
                name = "",
                nameError = false,
                openingHours = "",
                openingHoursError = false,
                description = "",
                descriptionError = false,
                address = "",
                addressError = false,
                location = null,
                locationError = false,
                selectedTags = emptyList(),
            )
        }
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

        object OnAddressSearch : Action()
        object OnCandidatesDialogDismissed : Action()
        data class OnCandidateSelected(val index: Int) : Action()
        object SubmitPlace : Action()
        data class OnTagClick(val tag: PlaceTag) : Action()
        object OnImageClick : Action()
        object OnLocationFieldClick : Action()
        data class OnAddressSelected(val activityResult: ActivityResult) : Action()
    }

    sealed class SideEffect {
        object NavigateToThankYouScreen : SideEffect()
        object ShowTryAgainDialog : SideEffect()
        object OpenImageSelector : SideEffect()
        object OpenAddressSearchOverlay : SideEffect()
    }

    data class PlaceAddress(
        val address: String,
        val latLng: LatLng,
    )
}