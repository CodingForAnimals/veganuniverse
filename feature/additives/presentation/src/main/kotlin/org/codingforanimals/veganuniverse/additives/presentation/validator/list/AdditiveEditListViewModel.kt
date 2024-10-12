package org.codingforanimals.veganuniverse.additives.presentation.validator.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveEdit
import org.codingforanimals.veganuniverse.additives.domain.usecase.AdditivesUseCases

internal class AdditiveEditListViewModel(
    additivesUseCases: AdditivesUseCases,
) : ViewModel() {

    val state = flow {
        val edits = additivesUseCases.getEdits()
        if (edits.isEmpty()) {
            emit(State.Empty)
        } else {
            emit(State.Success(edits))
        }
    }.catch {
        emit(State.Error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = State.Loading
    )

    sealed class State {
        data object Loading : State()
        data object Error : State()
        data object Empty : State()
        data class Success(val edits: List<AdditiveEdit>) : State()
    }
}
