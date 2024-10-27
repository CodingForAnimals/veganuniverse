package org.codingforanimals.veganuniverse.recipes.presentation.details

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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.commons.ui.R.string.report_success
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_message
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_message_try_again
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.VerifiedOnlyUserAction
import org.codingforanimals.veganuniverse.recipes.domain.usecase.RecipeDetailsUseCases
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.recipes.presentation.RECIPE_ID
import org.codingforanimals.veganuniverse.recipes.presentation.RecipesDestination
import org.codingforanimals.veganuniverse.recipes.presentation.details.entity.RecipeView
import org.codingforanimals.veganuniverse.recipes.presentation.model.toUI

private const val TAG = "RecipeDetailsViewModel"

internal class RecipeDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val useCases: RecipeDetailsUseCases,
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val verifiedOnlyUserAction: VerifiedOnlyUserAction,
) : ViewModel() {

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    private val navigationEffectsChannel: Channel<NavigationEffect> = Channel()
    val navigationEffects: Flow<NavigationEffect> = navigationEffectsChannel.receiveAsFlow()

    private val sideEffectsChannel: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = sideEffectsChannel.receiveAsFlow()

    private val recipeId = savedStateHandle.get<String>(RECIPE_ID)

    var isOwner: Boolean? by mutableStateOf(null)
        private set

    val recipeState: StateFlow<RecipeState> = flow<RecipeState> {
        useCases.getRecipe(recipeId!!).getOrNull()?.toView()!!.let { recipe ->
            val userId = flowOnCurrentUser().firstOrNull()?.id
            isOwner = recipe.userId == userId
            emit(RecipeState.Success(recipe))
        }
    }.catch {
        Log.e(TAG, it.stackTraceToString())
        Analytics.logNonFatalException(it)
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
            likeEnabled.value = true

            if (!result.isSuccess) {
                send(currentState)
                snackbarEffectsChannel.send(
                    Snackbar(
                        message = unexpected_error_message_try_again,
                    )
                )
            }
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
            bookmarkEnabled.value = true
            if (!result.isSuccess) {
                send(currentState)
                snackbarEffectsChannel.send(
                    Snackbar(
                        message = unexpected_error_message_try_again,
                    )
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false,
    )

    var dialog: Dialog? by mutableStateOf(null)
        private set

    sealed class Dialog {
        data class Image(val url: String) : Dialog()
        data object Report : Dialog()
        data object Edit : Dialog()
        data object Delete : Dialog()
    }

    fun onAction(action: Action) {
        when (action) {
            Action.OnBackClick -> {
                viewModelScope.launch {
                    navigationEffectsChannel.send(NavigationEffect.NavigateUp)
                }
            }

            is Action.OnImageClick -> {
                action.url?.let {
                    dialog = Dialog.Image(it)
                }
            }

            Action.OnDialogDismissRequest -> {
                dialog = null
            }

            Action.OnBookmarkClick -> {
                viewModelScope.launch {
                    verifiedOnlyUserAction {
                        toggleBookmark()
                    }
                }
            }

            Action.OnLikeClick -> {
                viewModelScope.launch {
                    verifiedOnlyUserAction {
                        toggleLike()
                    }
                }
            }

            Action.OnErrorDialogDismissRequest -> {
                viewModelScope.launch {
                    navigationEffectsChannel.send(NavigationEffect.NavigateUp)
                }
            }

            Action.OnEditClick -> {
                viewModelScope.launch {
                    verifiedOnlyUserAction {
                        dialog = Dialog.Edit
                    }
                }
            }

            Action.OnReportClick -> {
                viewModelScope.launch {
                    verifiedOnlyUserAction {
                        dialog = Dialog.Report
                    }
                }
            }

            is Action.OnReportResult -> onReportResult(action.result)
            Action.OnDeleteClick -> {
                dialog = Dialog.Delete
            }

            Action.OnConfirmDelete -> deleteRecipe()
            Action.OnShareClick -> {
                viewModelScope.launch {
                    recipeId ?: return@launch
                    val recipeAppLink = RecipesDestination.Details.getAppLink(recipeId)
                    sideEffectsChannel.send(SideEffect.Share(recipeAppLink))
                }
            }
        }
    }

    private fun onReportResult(result: ReportContentDialogResult) {
        when (result) {
            ReportContentDialogResult.Dismiss -> {
                dialog = null
            }

            ReportContentDialogResult.SendReport -> {
                recipeId ?: return
                viewModelScope.launch {
                    val reportResult = useCases.reportRecipe(recipeId)
                    dialog = null
                    if (reportResult.isSuccess) {
                        snackbarEffectsChannel.send(Snackbar(report_success))
                    } else {
                        snackbarEffectsChannel.send(Snackbar(unexpected_error_message))
                    }
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

    private fun deleteRecipe() {
        recipeId ?: return
        viewModelScope.launch {
            val result = useCases.deleteRecipe(recipeId)
            dialog = null
            if (result.isSuccess) {
                navigationEffectsChannel.send(NavigationEffect.NavigateUp)
            } else {
                snackbarEffectsChannel.send(Snackbar(R.string.delete_recipe_error_message))
            }
        }
    }

    private fun Recipe.toView(): RecipeView? {
        return RecipeView(
            id = id ?: return null,
            userId = userId,
            username = username,
            name = name,
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
        data object OnShareClick : Action()
        data object OnEditClick : Action()
        data object OnReportClick : Action()
        data object OnDeleteClick : Action()
        data object OnConfirmDelete : Action()
        data class OnImageClick(val url: String?) : Action()
        data object OnDialogDismissRequest : Action()
        data object OnLikeClick : Action()
        data object OnBookmarkClick : Action()
        data object OnErrorDialogDismissRequest : Action()
        data class OnReportResult(val result: ReportContentDialogResult) : Action()
    }

    sealed class NavigationEffect {
        data object NavigateUp : NavigationEffect()
    }

    sealed class SideEffect {
        data class Share(val textToShare: String) : SideEffect()
    }
}