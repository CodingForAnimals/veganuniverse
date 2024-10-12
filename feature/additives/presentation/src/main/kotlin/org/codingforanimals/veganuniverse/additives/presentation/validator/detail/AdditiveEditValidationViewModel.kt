package org.codingforanimals.veganuniverse.additives.presentation.validator.detail

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
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveEdit
import org.codingforanimals.veganuniverse.additives.domain.usecase.AdditivesUseCases
import org.codingforanimals.veganuniverse.additives.presentation.AdditivesDestination
import org.codingforanimals.veganuniverse.additives.presentation.R
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar

internal class AdditiveEditValidationViewModel(
    savedStateHandle: SavedStateHandle,
    private val additiveUseCases: AdditivesUseCases,
) : ViewModel() {

    private val editID =
        savedStateHandle.get<String>(AdditivesDestination.EditValidation.ARG_EDIT_ID)

    val state = flow<AdditiveState> {
        checkNotNull(editID) {
            "Additive id is required"
        }
        val editAdditive = additiveUseCases.getEditByID(editID)
        val remoteAdditive = additiveUseCases.getByIdFromRemote(editAdditive.additiveID)

        emit(AdditiveState.Content(remoteAdditive, editAdditive))
    }.catch {
        emit(AdditiveState.Error)
    }.stateIn(
        scope = viewModelScope,
        initialValue = AdditiveState.Loading,
        started = SharingStarted.WhileSubscribed(5000),
    )

    private val resultChannel = Channel<Result>()
    val result = resultChannel.receiveAsFlow()

    fun validate() {
        editID ?: return
        viewModelScope.launch {
            if (additiveUseCases.validateEdit(editID).isSuccess) {
                resultChannel.send(Result.Success)
            } else {
                resultChannel.send(Result.Error)
            }
        }
    }

    sealed class AdditiveState {
        data object Loading : AdditiveState()
        data object Error : AdditiveState()
        data class Content(
            val remoteAdditive: Additive,
            val editAdditive: AdditiveEdit,
        ) : AdditiveState()
    }

    sealed class Result(val snackbar: Snackbar) {
        data object Success : Result(Snackbar(R.string.additive_validated_successfully))
        data object Error : Result(Snackbar(R.string.additive_validated_error))
    }
}