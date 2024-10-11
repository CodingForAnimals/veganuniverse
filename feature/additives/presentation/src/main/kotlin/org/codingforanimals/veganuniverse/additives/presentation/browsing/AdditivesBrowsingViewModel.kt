package org.codingforanimals.veganuniverse.additives.presentation.browsing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.additives.domain.model.Additive
import org.codingforanimals.veganuniverse.additives.domain.usecase.AdditivesUseCases

internal class AdditivesBrowsingViewModel(
    getAdditives: AdditivesUseCases,
) : ViewModel() {

    private val searchTextDelayMs = 750L
    private var searchTextDelayJob: Job? = null
    var searchText by mutableStateOf("")
        private set

    private val searchAction = Channel<String>()
    val additivesState = channelFlow {
        val allAdditives = getAdditives.getAllAdditives()

        if (allAdditives.isEmpty()) {
            send(AdditivesState.Empty)
        } else {
            send(AdditivesState.Success(allAdditives))
        }

        searchAction.receiveAsFlow().collect { query ->
            val searchedAdditives = getAdditives.queryAdditives(query)
            if (searchedAdditives.isEmpty()) {
                send(AdditivesState.Empty)
            } else {
                send(AdditivesState.Success(searchedAdditives))
            }
        }
    }.catch {
        emit(AdditivesState.Error)
    }.stateIn(
        scope = viewModelScope,
        initialValue = AdditivesState.Loading,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000),
    )

    fun onSearchTextChange(value: String) {
        viewModelScope.launch {
            searchText = value
            searchTextDelayJob?.cancel()
            if (value.isEmpty() || value.length >= MIN_SEARCH_TEXT_LENGTH) {
                searchTextDelayJob = launch {
                    delay(searchTextDelayMs)
                    searchAction.send(value)
                }
            }
        }
    }

    sealed class AdditivesState {
        data object Loading : AdditivesState()
        data object Empty : AdditivesState()
        data object Error : AdditivesState()
        data class Success(val additives: List<Additive>) : AdditivesState()
    }

    companion object {
        private const val MIN_SEARCH_TEXT_LENGTH = 3
    }
}
