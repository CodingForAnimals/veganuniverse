package org.codingforanimals.veganuniverse.create.presentation.place

import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider

internal class CreatePlaceViewModel(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val geocoder: Geocoder,
) : ViewModel() {

    private val ioDispatcher = coroutineDispatcherProvider.io()
    private val mainDispatcher = coroutineDispatcherProvider.main()

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.initState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    var icon: Bitmap? = null

    init {

    }

    fun onAction(action: Action) {
        when (action) {
            is Action.OnFormChanged -> with(action) {
                name?.let { _uiState.value = _uiState.value.copy(name = it) }
                openingHours?.let { _uiState.value = _uiState.value.copy(openingHours = it) }
                description?.let { _uiState.value = _uiState.value.copy(description = it) }
                type?.let { _uiState.value = _uiState.value.copy(type = it) }
                address?.let { _uiState.value = _uiState.value.copy(address = it) }
            }
            Action.OnAddressSearch -> fetchAddressCandidates()
            Action.OnCandidatesDialogDismissed -> {
                _uiState.value = _uiState.value.copy(addressCandidates = emptyList())
            }
            is Action.OnCandidateSelected -> {
                val newAddress = uiState.value.addressCandidates[action.index]
                _uiState.value = _uiState.value.copy(
                    address = newAddress.address,
                    location = newAddress.latLng,
                    addressCandidates = emptyList(),
                )
            }
        }
    }

    private fun fetchAddressCandidates() {
        val address = uiState.value.address
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
            _uiState.value = _uiState.value.copy(
                address = onlyCandidate.address,
                location = onlyCandidate.latLng,
            )
        } else {
            _uiState.value = _uiState.value.copy(addressCandidates = candidates)
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
        val name: String,
        val openingHours: String,
        val description: String,
        val type: PlaceType?,
        val address: String,
        val selectedTags: List<PlaceTag>,
        val addressCandidates: List<PlaceAddress>,
        val location: LatLng?,
    ) {
        companion object {
            fun initState() = UiState(
                name = "",
                openingHours = "",
                description = "",
                type = null,
                address = "",
                selectedTags = emptyList(),
                addressCandidates = emptyList(),
                location = null,
            )
        }
    }

    sealed class Action {
        data class OnFormChanged(
            val name: String? = null,
            val openingHours: String? = null,
            val description: String? = null,
            val type: PlaceType? = null,
            val address: String? = null,
        ) : Action()

        object OnAddressSearch : Action()
        object OnCandidatesDialogDismissed : Action()
        data class OnCandidateSelected(val index: Int) : Action()
    }

    data class PlaceAddress(
        val address: String,
        val latLng: LatLng,
    )

    enum class PlaceTag(val label: String, val icon: Icon) {
        GLUTEN_FREE(label = "Sin tacc", icon = VUIcons.GlutenFree),
        FULL_VEGAN(label = "100% vegano", icon = VUIcons.VeganLogo),
        DELIVERY(label = "Delivery", icon = VUIcons.Delivery),
        TAKEAWAY(label = "Take away", icon = VUIcons.Bag),
        DINE_IN(label = "Consumo en el lugar", icon = VUIcons.Chairs)
    }

    enum class PlaceType(val label: String, val icon: Icon) {
        MARKET(label = "Mercado", icon = VUIcons.Store),
        RESTAURANT(label = "Restaurante", icon = VUIcons.Restaurant),
        CAFE(label = "Café", icon = VUIcons.CoffeeMug),
    }
}