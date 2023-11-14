package org.codingforanimals.veganuniverse.recipes.presentation.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.entity.RecipeView
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.model.RecipeToggleableItem
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.usecase.CollectUserRecipeToggleableStateUseCase
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.usecase.GetRecipeUseCase
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.usecase.UpdateRecipeToggleableStatusUseCase
import org.codingforanimals.veganuniverse.ui.dialog.Dialog
import org.codingforanimals.veganuniverse.ui.icon.ToggleIconState

internal class RecipeDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    getRecipe: GetRecipeUseCase,
    private val collectUserRecipeToggleableState: CollectUserRecipeToggleableStateUseCase,
    private val updateRecipeLikeStatus: UpdateRecipeToggleableStatusUseCase,
) : ViewModel() {

    private var collectUserLikeJob: Job? = null
    private var collectUserBookmarkJob: Job? = null

    private val sideEffectsChannel: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = sideEffectsChannel.receiveAsFlow()

    var uiState by mutableStateOf(UiState())
        private set

    init {
        viewModelScope.launch {
            val recipeId = savedStateHandle.get<String>("recipeId")
            uiState = when (val recipeStatus = getRecipe(recipeId)) {
                GetRecipeUseCase.Status.Loading -> uiState.copy(
                    loading = true,
                )

                GetRecipeUseCase.Status.Error -> uiState.copy(
                    loading = false,
                    dialog = Dialog.unknownErrorDialog(),
                )

                is GetRecipeUseCase.Status.Success -> {
                    collectUserRecipeLikeOnUserStateChange(recipeStatus.recipe.id)
                    collectUserRecipeBookmarkOnUserStateChange(recipeStatus.recipe.id)
                    uiState.copy(
                        loading = false,
                        recipe = recipeStatus.recipe,
                    )
                }
            }
        }
    }

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

            Action.OnBookmarkClick -> {
                updateBookmarkStatus()
            }

            Action.OnLikeClick -> {
                updateLikeStatus()
            }
        }
    }

    private fun collectUserRecipeLikeOnUserStateChange(recipeId: String) {
        collectUserLikeJob?.cancel()
        collectUserLikeJob = viewModelScope.launch {
            collectUserRecipeToggleableState(
                recipeId,
                RecipeToggleableItem.Like
            ).collectLatest { status ->
                when (status) {
                    CollectUserRecipeToggleableStateUseCase.Status.Error -> uiState = uiState.copy(
                        likeState = ToggleIconState(
                            loading = false,
                        ),
                        dialog = Dialog.unknownErrorDialog()
                    )

                    CollectUserRecipeToggleableStateUseCase.Status.Loading -> uiState =
                        uiState.copy(
                            likeState = uiState.likeState.copy(loading = true)
                        )

                    is CollectUserRecipeToggleableStateUseCase.Status.Success -> uiState =
                        uiState.copy(
                            likeState = ToggleIconState(
                                loading = false,
                                toggled = status.toggled,
                            )
                        )
                }

            }
        }
    }

    private fun collectUserRecipeBookmarkOnUserStateChange(recipeId: String) {
        collectUserBookmarkJob?.cancel()
        collectUserBookmarkJob = viewModelScope.launch {
            collectUserRecipeToggleableState(
                recipeId,
                RecipeToggleableItem.Bookmark
            ).collectLatest { status ->
                when (status) {
                    CollectUserRecipeToggleableStateUseCase.Status.Error -> uiState = uiState.copy(
                        bookmarkState = ToggleIconState(
                            loading = false,
                        ),
                        dialog = Dialog.unknownErrorDialog()
                    )

                    CollectUserRecipeToggleableStateUseCase.Status.Loading -> uiState =
                        uiState.copy(
                            bookmarkState = uiState.likeState.copy(loading = true)
                        )

                    is CollectUserRecipeToggleableStateUseCase.Status.Success -> uiState =
                        uiState.copy(
                            bookmarkState = ToggleIconState(
                                loading = false,
                                toggled = status.toggled,
                            )
                        )
                }

            }
        }
    }

    private fun updateBookmarkStatus() {
        uiState.recipe?.let { recipe ->
            viewModelScope.launch {
                updateRecipeLikeStatus(
                    currentToggleState = uiState.bookmarkState.toggled,
                    recipeId = recipe.id,
                    item = RecipeToggleableItem.Bookmark,
                ).collectLatest { status ->
                    when (status) {
                        UpdateRecipeToggleableStatusUseCase.Status.Error -> {
                            uiState = uiState.copy(dialog = Dialog.unknownErrorDialog())
                        }

                        UpdateRecipeToggleableStatusUseCase.Status.GuestUser -> {
                            sideEffectsChannel.send(SideEffect.NavigateToAuthenticateScreen)
                        }

                        UpdateRecipeToggleableStatusUseCase.Status.Loading -> {
                            uiState = uiState.copy(
                                bookmarkState = uiState.bookmarkState.copy(loading = true)
                            )
                        }

                        is UpdateRecipeToggleableStatusUseCase.Status.Success -> {
                            uiState = uiState.copy(
                                bookmarkState = ToggleIconState(
                                    loading = false,
                                    toggled = status.toggled
                                )
                            )
                        }
                    }

                }
            }
        }
    }

    private fun updateLikeStatus() {
        uiState.recipe?.let { recipe ->
            viewModelScope.launch {
                updateRecipeLikeStatus(
                    currentToggleState = uiState.likeState.toggled,
                    recipeId = recipe.id,
                    item = RecipeToggleableItem.Like
                ).collectLatest { status ->
                    when (status) {
                        UpdateRecipeToggleableStatusUseCase.Status.Error -> {
                            uiState = uiState.copy(dialog = Dialog.unknownErrorDialog())
                        }

                        UpdateRecipeToggleableStatusUseCase.Status.GuestUser -> {
                            sideEffectsChannel.send(SideEffect.NavigateToAuthenticateScreen)
                        }

                        is UpdateRecipeToggleableStatusUseCase.Status.Success -> {
                            uiState = uiState.copy(
                                likeState = ToggleIconState(
                                    loading = false,
                                    toggled = status.toggled
                                )
                            )
                        }

                        UpdateRecipeToggleableStatusUseCase.Status.Loading -> {
                            uiState = uiState.copy(
                                likeState = ToggleIconState(
                                    loading = true,
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    data class UiState(
        val recipe: RecipeView? = null,
        val openImageDialog: Boolean = false,
        val loading: Boolean = true,
        val dialog: Dialog? = null,
        val likeState: ToggleIconState = ToggleIconState(),
        val bookmarkState: ToggleIconState = ToggleIconState(),
    )

    sealed class Action {
        data object OnBackClick : Action()
        data object OnImageClick : Action()
        data object OnImageDialogDismissRequest : Action()
        data object OnLikeClick : Action()
        data object OnBookmarkClick : Action()
    }

    sealed class SideEffect {
        data object NavigateUp : SideEffect()
        data object NavigateToAuthenticateScreen : SideEffect()
    }
}