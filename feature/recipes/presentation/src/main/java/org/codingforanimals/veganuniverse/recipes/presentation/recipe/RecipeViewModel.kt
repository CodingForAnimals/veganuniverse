package org.codingforanimals.veganuniverse.recipes.presentation.recipe

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.entity.RecipeView
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.usecase.GetRecipeUseCase

private const val TAG = "RecipeViewModel"

internal class RecipeViewModel(
    savedStateHandle: SavedStateHandle,
    getRecipeUseCase: GetRecipeUseCase,
) : ViewModel() {

    private val sideEffectsChannel: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = sideEffectsChannel.receiveAsFlow()

    var uiState by mutableStateOf(UiState())
        private set

    val recipe: StateFlow<RecipeState> = flow {
        val status = try {
            val recipeId = savedStateHandle.get<String>("recipeId")!!
            getRecipeUseCase(recipeId)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            GetRecipeUseCase.Status.Error
        }
        emit(status)
    }.transform { status ->
        emit(
            when (status) {
                GetRecipeUseCase.Status.Error -> RecipeState.Error
                GetRecipeUseCase.Status.Loading -> RecipeState.Loading
                is GetRecipeUseCase.Status.Success -> RecipeState.Success(status.recipe)
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RecipeState.Loading,
    )

    fun onAction(action: Action) {
        when (action) {
            Action.OnBackClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateUp)
                }
            }

            Action.OnImageClick -> {
                uiState = uiState.copy(openImageDialog = true)
            }

            Action.OnImageDialogDismissRequest -> {
                uiState = uiState.copy(openImageDialog = false)
            }
        }
    }

    sealed class RecipeState {
        data object Loading : RecipeState()
        data object Error : RecipeState()
        data class Success(val recipe: RecipeView) : RecipeState()
    }

    data class UiState(
        val openImageDialog: Boolean = false,
    )

    sealed class Action {
        data object OnBackClick : Action()
        data object OnImageClick : Action()
        data object OnImageDialogDismissRequest : Action()
    }

    sealed class SideEffect {
        data object NavigateUp : SideEffect()
    }
}