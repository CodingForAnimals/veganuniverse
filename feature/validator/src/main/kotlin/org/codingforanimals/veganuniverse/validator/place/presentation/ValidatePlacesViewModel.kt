package org.codingforanimals.veganuniverse.validator.place.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.navigation.DeeplinkNavigator
import org.codingforanimals.veganuniverse.commons.place.shared.model.Place
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.validator.R
import org.codingforanimals.veganuniverse.validator.place.domain.GetUnvalidatedPlacesPaginationFlowUseCase
import org.codingforanimals.veganuniverse.validator.place.domain.ValidatePlaceUseCase

internal class ValidatePlacesViewModel(
    getUnvalidatedPlacesPaginationFlowUseCase: GetUnvalidatedPlacesPaginationFlowUseCase,
    private val validatePlaceUseCase: ValidatePlaceUseCase,
    private val deeplinkNavigator: DeeplinkNavigator,
) : ViewModel() {
    private val sideEffectsChannel = Channel<SideEffect>()

    val sideEffects = sideEffectsChannel.receiveAsFlow()
    private val snackbarEffectsChannel = Channel<Snackbar>()

    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    sealed class SideEffect {
        data object Refresh : SideEffect()
    }

    val unvalidatedPlaces = getUnvalidatedPlacesPaginationFlowUseCase().cachedIn(viewModelScope)

    fun validatePlace(place: Place) {
        viewModelScope.launch {
            if (validatePlaceUseCase(place.geoHash ?: return@launch).isSuccess) {
                launch { snackbarEffectsChannel.send(Snackbar(R.string.place_validated_success)) }
                launch { sideEffectsChannel.send(SideEffect.Refresh) }
            } else {
                launch { snackbarEffectsChannel.send(Snackbar(R.string.place_validated_error)) }
            }
        }
    }

    fun onPlaceClick(place: Place) {
        viewModelScope.launch {
            deeplinkNavigator.navigate(DeepLink.PlaceDetail(place.geoHash ?: return@launch))
        }
    }
}