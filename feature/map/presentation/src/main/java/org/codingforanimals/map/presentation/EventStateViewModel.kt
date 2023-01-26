package org.codingforanimals.map.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class EventStateViewModel<
    Event : ViewEvent,
    UiState : ViewState,
    Effect : ViewSideEffect,
    > : ViewModel() {

    private val initialState: UiState by lazy { setInitialState() }
    abstract fun setInitialState(): UiState

    private val uiStateEmitter: MutableState<UiState> by lazy { mutableStateOf(initialState) }
    val uiState: State<UiState> = uiStateEmitter

    private val eventEmitter: MutableSharedFlow<Event> = MutableSharedFlow()

    private val effectEmitter: Channel<Effect> = Channel()
    val effect: Flow<Effect> = effectEmitter.receiveAsFlow()

    init {
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        viewModelScope.launch {
            eventEmitter.collect { event -> handleEvent(event) }
        }
    }

    abstract fun handleEvent(event: Event)

    fun setEvent(event: Event) {
        viewModelScope.launch { eventEmitter.emit(event) }
    }

    protected fun setState(reducer: UiState.() -> UiState) {
        val newState = uiState.value.reducer()
        uiStateEmitter.value = newState
    }

    protected fun setEffect(builder: () -> Effect) {
        val effect = builder()
        viewModelScope.launch { effectEmitter.send(effect) }
    }

}

interface ViewEvent
interface ViewState
interface ViewSideEffect