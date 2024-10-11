package org.codingforanimals.veganuniverse.additives.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.additives.domain.model.Additive
import org.codingforanimals.veganuniverse.additives.domain.usecase.AdditivesUseCases
import org.codingforanimals.veganuniverse.additives.presentation.AdditivesDestination
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.VerifiedOnlyUserAction

internal class AdditiveDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val additiveUseCases: AdditivesUseCases,
    private val verifiedOnlyUserAction: VerifiedOnlyUserAction,
) : ViewModel() {

    private val snackbarEffectChannel = Channel<Snackbar>()
    val snackbarEffect = snackbarEffectChannel.receiveAsFlow()

    private val sideEffectChannel = Channel<SideEffect>()
    val sideEffects = sideEffectChannel.receiveAsFlow()

    private val id = savedStateHandle.get<String>(AdditivesDestination.Detail.ARG_ID)

    val state = flow<AdditiveState> {
        checkNotNull(id) {
            "Additive id is required"
        }
        val additive = additiveUseCases.getByIdFromLocal(id)
        emit(AdditiveState.Success(additive))
    }.catch {
        emit(AdditiveState.Error)
    }.stateIn(
        scope = viewModelScope,
        initialValue = AdditiveState.Loading,
        started = SharingStarted.WhileSubscribed(5000),
    )

    fun onEditClick() {
        id ?: return
        viewModelScope.launch {
            verifiedOnlyUserAction {
                sideEffectChannel.send(SideEffect.NavigateToAdditiveEdit(id))
            }
        }
    }

    fun onShareClick() {
        id ?: return
        viewModelScope.launch {
            val appLink = AdditivesDestination.Detail.getAppLink(id)
            sideEffectChannel.send(SideEffect.Share(appLink))
        }
    }

    sealed class AdditiveState {
        data object Loading : AdditiveState()
        data object Error : AdditiveState()
        data class Success(val additive: Additive) : AdditiveState()
    }

    sealed class SideEffect {
        data class Share(val textToShare: String) : SideEffect()
        data class NavigateToAdditiveEdit(val id: String) : SideEffect()
    }
}