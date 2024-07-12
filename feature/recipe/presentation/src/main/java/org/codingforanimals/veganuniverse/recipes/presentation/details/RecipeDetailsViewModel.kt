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
import org.codingforanimals.veganuniverse.commons.navigation.Deeplink
import org.codingforanimals.veganuniverse.commons.navigation.DeeplinkNavigator
import org.codingforanimals.veganuniverse.commons.profile.shared.model.ToggleResult
import org.codingforanimals.veganuniverse.commons.recipe.presentation.toUI
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.commons.ui.R.string.edit_error
import org.codingforanimals.veganuniverse.commons.ui.R.string.report_success
import org.codingforanimals.veganuniverse.commons.ui.R.string.edit_success
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_message
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_message_try_again
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_not_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailResult
import org.codingforanimals.veganuniverse.recipes.domain.usecase.EditRecipe
import org.codingforanimals.veganuniverse.recipes.domain.usecase.RecipeDetailsUseCases
import org.codingforanimals.veganuniverse.recipes.domain.usecase.ReportRecipe
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.recipes.presentation.RECIPE_ID
import org.codingforanimals.veganuniverse.recipes.presentation.details.entity.RecipeView

private const val TAG = "RecipeDetailsViewModel"

internal class RecipeDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val useCases: RecipeDetailsUseCases,
    private val deeplinkNavigator: DeeplinkNavigator,
    private val flowOnCurrentUser: FlowOnCurrentUser,
) : ViewModel() {

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    private val navigationEffectsChannel: Channel<NavigationEffect> = Channel()
    val navigationEffects: Flow<NavigationEffect> = navigationEffectsChannel.receiveAsFlow()

    private val recipeId = savedStateHandle.get<String>(RECIPE_ID)

    var isOwner: Boolean? by mutableStateOf(null)
        private set

    val recipeState: StateFlow<RecipeState> = flow<RecipeState> {
        val recipe = useCases.getRecipe(recipeId!!)?.toView()!!
        val userId = flowOnCurrentUser().firstOrNull()?.id
        isOwner = recipe.userId == userId
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
            handleToggleResult(result)
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
            handleToggleResult(result)
            send(result.newValue)
            bookmarkEnabled.value = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false,
    )

    var dialog: Dialog? by mutableStateOf(null)
        private set

    sealed class Dialog {
        data object UnverifiedEmail : Dialog()
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
                    navigationEffectsChannel.send(NavigationEffect.NavigateUp)
                }
            }

            Action.OnEditClick -> {
                dialog = Dialog.Edit
            }

            Action.OnReportClick -> {
                dialog = Dialog.Report
            }

            is Action.OnReportResult -> onReportResult(action.result)
            is Action.OnEditResult -> onEditResult(action.result)
            is Action.OnUnverifiedEmailResult -> onUnverifiedEmailResult(action.result)
            Action.OnDeleteClick -> {
                dialog = Dialog.Delete
            }

            Action.OnConfirmDelete -> deleteRecipe()
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
                    when (reportResult) {
                        ReportRecipe.Result.Success -> {
                            snackbarEffectsChannel.send(Snackbar(report_success))
                        }

                        ReportRecipe.Result.UnauthenticatedUser -> {
                            navigationEffectsChannel.send(NavigationEffect.NavigateToAuthenticateScreen)
                        }

                        ReportRecipe.Result.UnexpectedError -> {
                            snackbarEffectsChannel.send(Snackbar(unexpected_error_message))
                        }

                        ReportRecipe.Result.UnverifiedEmail -> {
                            dialog = Dialog.UnverifiedEmail
                        }

                        ReportRecipe.Result.UserMustReathenticate -> {
                            deeplinkNavigator.navigate(Deeplink.Reauthentication)
                        }
                    }
                }
            }
        }
    }

    private fun onEditResult(result: EditContentDialogResult) {
        dialog = null
        when (result) {
            EditContentDialogResult.Dismiss -> Unit
            is EditContentDialogResult.SendEdit -> {
                recipeId ?: return
                viewModelScope.launch {
                    when (useCases.editRecipe(recipeId, result.edition)) {
                        EditRecipe.Result.Success -> {
                            snackbarEffectsChannel.send(Snackbar(edit_success))
                        }
                        EditRecipe.Result.UnauthenticatedUser -> {
                            navigationEffectsChannel.send(NavigationEffect.NavigateToAuthenticateScreen)
                        }
                        EditRecipe.Result.UnexpectedError -> {
                            snackbarEffectsChannel.send(Snackbar(edit_error))
                        }
                        EditRecipe.Result.UnverifiedEmail -> {
                            dialog = Dialog.UnverifiedEmail
                        }
                        EditRecipe.Result.UserMustReauthenticate -> {
                            deeplinkNavigator.navigate(Deeplink.Reauthentication)
                        }
                    }
                }
            }
        }
    }

    private fun onUnverifiedEmailResult(result: UnverifiedEmailResult) {
        dialog = null
        when (result) {
            UnverifiedEmailResult.Dismissed -> Unit
            UnverifiedEmailResult.UnexpectedError -> {
                viewModelScope.launch {
                    snackbarEffectsChannel.send(Snackbar(verification_email_not_sent))
                }
            }

            UnverifiedEmailResult.VerificationEmailSent -> {
                viewModelScope.launch {
                    snackbarEffectsChannel.send(Snackbar(verification_email_sent))
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

    private suspend fun handleToggleResult(
        result: ToggleResult,
    ) {
        when (result) {
            is ToggleResult.Success -> Unit
            is ToggleResult.GuestUser -> {
                navigationEffectsChannel.send(NavigationEffect.NavigateToAuthenticateScreen)
            }

            is ToggleResult.UnexpectedError -> {
                snackbarEffectsChannel.send(
                    Snackbar(
                        message = unexpected_error_message_try_again,
                    )
                )
            }
        }
    }

    private fun deleteRecipe() {
        recipeId ?: return
        viewModelScope.launch {
            if (useCases.deleteRecipe(recipeId).isSuccess) {
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
        data object OnEditClick : Action()
        data object OnReportClick : Action()
        data object OnDeleteClick : Action()
        data object OnConfirmDelete : Action()
        data class OnImageClick(val url: String?) : Action()
        data object OnDialogDismissRequest : Action()
        data object OnLikeClick : Action()
        data object OnBookmarkClick : Action()
        data object OnErrorDialogDismissRequest : Action()
        data class OnEditResult(val result: EditContentDialogResult) : Action()
        data class OnReportResult(val result: ReportContentDialogResult) : Action()
        data class OnUnverifiedEmailResult(val result: UnverifiedEmailResult) : Action()
    }

    sealed class NavigationEffect {
        data object NavigateUp : NavigationEffect()
        data object NavigateToAuthenticateScreen : NavigationEffect()
    }
}