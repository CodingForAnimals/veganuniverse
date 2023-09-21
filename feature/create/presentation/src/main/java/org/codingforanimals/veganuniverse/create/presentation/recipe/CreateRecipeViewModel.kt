package org.codingforanimals.veganuniverse.create.presentation.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.core.ui.viewmodel.PictureField
import org.codingforanimals.veganuniverse.core.ui.viewmodel.StringField

class CreateRecipeViewModel(

) : ViewModel() {

    private var imagePickerJob: Job? = null

    private val sideEffectChannel: Channel<SideEffect> = Channel()
    val sideEffect: Flow<SideEffect> = sideEffectChannel.receiveAsFlow()

    var uiState by mutableStateOf(UiState())
        private set

    fun onAction(action: Action) {
        when (action) {
            Action.ImagePickerClick -> openImagePicker()
            is Action.OnTextChange -> updateTextForm(action)
        }
    }

    private fun updateTextForm(action: Action.OnTextChange) {
        uiState = when (action) {
            is Action.OnTextChange.Title -> uiState.copy(
                titleField = StringField(action.text)
            )

            is Action.OnTextChange.Description -> uiState.copy(
                descriptionField = StringField(action.text)
            )

            is Action.OnTextChange.Servings -> uiState.copy(
                servingsField = StringField(action.text)
            )
        }
    }

    private fun openImagePicker() {
        imagePickerJob?.cancel()
        imagePickerJob = viewModelScope.launch {
            sideEffectChannel.send(SideEffect.OpenImageSelector)
        }
    }

    data class UiState(
        val pictureField: PictureField = PictureField(),
        val titleField: StringField = StringField(),
        val descriptionField: StringField = StringField(),
        val servingsField: StringField = StringField(),
        val isValidating: Boolean = false,
    )

    sealed class Action {
        data object ImagePickerClick : Action()
        sealed class OnTextChange : Action() {
            data class Title(val text: String) : OnTextChange()
            data class Description(val text: String) : OnTextChange()
            data class Servings(val text: String) : OnTextChange()
        }
    }

    sealed class SideEffect {
        data object OpenImageSelector : SideEffect()
    }
}