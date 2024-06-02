package org.codingforanimals.veganuniverse.recipes.presentation.details

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.profile.model.ToggleResult
import org.codingforanimals.veganuniverse.recipe.model.Recipe
import org.codingforanimals.veganuniverse.recipe.presentation.toUI
import org.codingforanimals.veganuniverse.recipes.domain.usecase.RecipeDetailsUseCases
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.recipes.presentation.RECIPE_ID
import org.codingforanimals.veganuniverse.recipes.presentation.details.entity.RecipeView

private const val TAG = "RecipeDetailsViewModel"

internal class RecipeDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val useCases: RecipeDetailsUseCases,
) : ViewModel() {

    private val sideEffectsChannel: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = sideEffectsChannel.receiveAsFlow()

    private val recipeId = savedStateHandle.get<String>(RECIPE_ID)

    val recipeState: StateFlow<RecipeState> = flow<RecipeState> {
        val recipe = useCases.getRecipe(recipeId!!)?.toView()!!
        emit(RecipeState.Success(recipe))
    }.catch {
        Log.e(TAG, it.stackTraceToString())
        emit(RecipeState.Error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RecipeState.Loading,
    )

    private val likeEnabled = mutableStateOf(true)
    private val toggleLikeActionChannel: Channel<Boolean> = Channel()
    val isLiked: StateFlow<Boolean> = channelFlow {
        recipeId ?: return@channelFlow
        send(useCases.isLiked(recipeId))

        toggleLikeActionChannel.receiveAsFlow().collectLatest { currentState ->
            likeEnabled.value = false
            send(!currentState)
            val result = useCases.toggleLike(recipeId, currentState)
            handleToggleResultSideEffects(result, ::toggleLike)
            send(result.newValue)
            likeEnabled.value = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false,
    )

    private val bookmarkEnabled = mutableStateOf(true)
    private val toggleBookmarkActionChannel: Channel<Boolean> = Channel()
    val isBookmarked: StateFlow<Boolean> = channelFlow {
        recipeId ?: return@channelFlow
        send(useCases.isBookmarked(recipeId))

        toggleBookmarkActionChannel.receiveAsFlow().collect { currentState ->
            bookmarkEnabled.value = false
            send(!currentState)
            val result = useCases.toggleBookmark(recipeId, currentState)
            handleToggleResultSideEffects(result, ::toggleBookmark)
            send(result.newValue)
            bookmarkEnabled.value = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false,
    )

    private val _showImageDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showImageDialog: StateFlow<Boolean> = _showImageDialog.asStateFlow()

    fun onAction(action: Action) {
        when (action) {
            Action.OnBackClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateUp)
                }
            }

            Action.OnImageClick -> {
                _showImageDialog.value = true
            }

            Action.OnImageDialogDismissRequest -> {
                _showImageDialog.value = false
            }

            Action.OnBookmarkClick -> {
                viewModelScope.launch {
                    toggleBookmark()
                }
            }

            Action.OnLikeClick -> {
                viewModelScope.launch {
                    toggleLike()
                }
            }

            Action.OnErrorDialogDismissRequest -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateUp)
                }
            }
        }
    }

    private suspend fun toggleLike() {
        if (!likeEnabled.value) return
        toggleLikeActionChannel.send(isLiked.value)
    }

    private suspend fun toggleBookmark() {
        if (!bookmarkEnabled.value) return
        toggleBookmarkActionChannel.send(isBookmarked.value)
    }

    private suspend fun handleToggleResultSideEffects(
        result: ToggleResult,
        retryAction: suspend () -> Unit,
    ) {
        when (result) {
            is ToggleResult.Success -> Unit
            is ToggleResult.GuestUser -> {
                sideEffectsChannel.send(SideEffect.NavigateToAuthenticateScreen)
            }

            is ToggleResult.UnexpectedError -> {
                sideEffectsChannel.send(
                    SideEffect.DisplaySnackbar.UnexpectedErrorSnackbar(retryAction)
                )
            }
        }
    }

    private fun Recipe.toView(): RecipeView? {
        return RecipeView(
            id = id ?: return null,
            userId = userId,
            username = username,
            title = title,
            description = description,
            likes = likes,
            createdAt = createdAt,
            tags = tags.map { it.toUI() },
            ingredients = ingredients,
            steps = steps,
            prepTime = prepTime,
            servings = servings,
            imageUrl = imageUrl,
        )
    }

    sealed class RecipeState {
        data object Loading : RecipeState()
        data object Error : RecipeState()
        data class Success(val recipeView: RecipeView) : RecipeState()
    }

    sealed class Action {
        data object OnBackClick : Action()
        data object OnImageClick : Action()
        data object OnImageDialogDismissRequest : Action()
        data object OnLikeClick : Action()
        data object OnBookmarkClick : Action()
        data object OnErrorDialogDismissRequest : Action()
    }

    sealed class SideEffect {
        data object NavigateUp : SideEffect()
        data object NavigateToAuthenticateScreen : SideEffect()
        sealed class DisplaySnackbar(
            @StringRes open val message: Int,
            @StringRes open val actionLabel: Int? = null,
            open val action: (suspend () -> Unit)? = null,
        ) : SideEffect() {
            data class UnexpectedErrorSnackbar(
                override val action: (suspend () -> Unit)? = null,
            ) : DisplaySnackbar(
                message = R.string.snackbar_unexpected_error_message,
                actionLabel = R.string.try_again,
                action = action,
            )
        }
    }
}