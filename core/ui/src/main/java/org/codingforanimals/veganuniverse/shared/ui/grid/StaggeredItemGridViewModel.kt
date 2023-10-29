package org.codingforanimals.veganuniverse.shared.ui.grid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class StaggeredItemGridViewModel(
    deferred: Deferred<List<StaggeredItem>>,
) : ViewModel() {
    val items = flow {
        emit(
            try {
                ItemsState.Success(deferred.await())
            } catch (e: Throwable) {
                Log.e("TAG", e.stackTraceToString())
                ItemsState.Error
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ItemsState.Loading,
    )

    sealed class ItemsState {
        data object Loading : ItemsState()
        data object Error : ItemsState()
        data class Success(val items: List<StaggeredItem>) : ItemsState()
    }
}