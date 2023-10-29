package org.codingforanimals.veganuniverse.shared.ui.grid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.codingforanimals.veganuniverse.shared.ui.grid.model.StaggeredItem

private const val TAG = "StaggeredItemsGridViewM"

class StaggeredItemsGridViewModel(
    deferredItems: Deferred<List<StaggeredItem>>,
) : ViewModel() {

    val items = flow {
        val resultState = try {
            UiState.Success(deferredItems.await())
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            UiState.Error
        }
        emit(resultState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    sealed class UiState {
        data object Loading : UiState()
        data object Error : UiState()
        data class Success(val items: List<StaggeredItem>) : UiState()
    }
}