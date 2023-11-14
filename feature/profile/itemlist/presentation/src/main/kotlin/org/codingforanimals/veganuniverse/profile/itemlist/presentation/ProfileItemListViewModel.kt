package org.codingforanimals.veganuniverse.profile.itemlist.presentation

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.profile.itemlist.presentation.usecase.GetProfileItemsUseCase
import org.codingforanimals.veganuniverse.profile.model.SaveableContentType
import org.codingforanimals.veganuniverse.profile.model.SaveableType
import org.codingforanimals.veganuniverse.ui.cards.CardItem
import org.codingforanimals.veganuniverse.ui.dialog.Dialog


private const val TAG = "ProfileItemListViewMode"

class ProfileItemListViewModel(
    savedStateHandle: SavedStateHandle,
    private val getProfileItems: GetProfileItemsUseCase,
) : ViewModel() {

    private val sideEffectsChannel: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = sideEffectsChannel.receiveAsFlow()

    var uiState by mutableStateOf(
        UiState.init(
            navArgSaveableType = savedStateHandle[NAV_ARG_SAVEABLE_TYPE],
            navArgContentType = savedStateHandle[NAV_ARG_CONTENT_TYPE],
        )
    )

    init {
        getMoreItems()
    }

    private var getMoreItemsJob: Job? = null

    fun onAction(action: Action) {
        when (action) {
            is Action.NavigateToItemDetails -> {
                val navigationSideEffect = when (uiState.contentType) {
                    SaveableContentType.PLACE -> SideEffect.NavigateToPlace(geoHash = action.id)
                    SaveableContentType.RECIPE -> SideEffect.NavigateToRecipe(id = action.id)
                }
                viewModelScope.launch {
                    sideEffectsChannel.send(navigationSideEffect)
                }
            }

            Action.OnBackClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateUp)
                }
            }

            Action.OnLoadMoreClick -> getMoreItems()
            is Action.OnItemClick -> {
                viewModelScope.launch {
                    val navigationEffect = when (uiState.contentType) {
                        SaveableContentType.RECIPE -> SideEffect.NavigateToRecipe(action.id)
                        SaveableContentType.PLACE -> SideEffect.NavigateToPlace(action.id)
                    }
                    sideEffectsChannel.send(navigationEffect)
                }
            }
        }
    }

    private fun getMoreItems() {
        getMoreItemsJob?.cancel()
        getMoreItemsJob = viewModelScope.launch {
            uiState = uiState.copy(loading = true)
            val res = getProfileItems(
                saveableType = uiState.saveableType,
                contentType = uiState.contentType,
                index = uiState.lastItemIndex,
            )
            uiState = when (res) {
                GetProfileItemsUseCase.Result.Error -> uiState.copy(
                    errorDialog = Dialog.unknownErrorDialog(),
                    loading = false,
                )

                is GetProfileItemsUseCase.Result.Success -> uiState.copy(
                    lastItemIndex = res.lastIndex,
                    items = uiState.appendItems(res.items),
                    loading = false,
                    canLoadMore = res.items.isNotEmpty(),
                )
            }
        }
    }

    data class UiState(
        @StringRes val title: Int? = null,
        val loading: Boolean = false,
        val saveableType: SaveableType = SaveableType.BOOKMARK,
        val contentType: SaveableContentType = SaveableContentType.PLACE,
        val items: List<CardItem> = emptyList(),
        val lastItemIndex: Int = -1,
        val canLoadMore: Boolean = false,
        val errorDialog: Dialog? = null,
    ) {
        fun appendItems(items: List<CardItem>): List<CardItem> {
            val current = this.items.toMutableList()
            current.addAll(items)
            return current
        }

        companion object {
            fun init(navArgSaveableType: String?, navArgContentType: String?): UiState {
                return try {
                    val saveableType = SaveableType.valueOf(navArgSaveableType!!)
                    val contentType = SaveableContentType.valueOf(navArgContentType!!)
                    val title = when (saveableType) {
                        SaveableType.BOOKMARK -> {
                            when (contentType) {
                                SaveableContentType.PLACE -> R.string.profile_item_list_bookmarked_places_title
                                SaveableContentType.RECIPE -> R.string.profile_item_list_bookmarked_recipes_title
                            }
                        }

                        SaveableType.CONTRIBUTION -> {
                            when (contentType) {
                                SaveableContentType.PLACE -> R.string.profile_item_list_contributed_places_title
                                SaveableContentType.RECIPE -> R.string.profile_item_list_contributed_recipes_title
                            }
                        }

                        else -> -1
                    }
                    UiState(
                        title = title,
                        loading = true,
                        saveableType = saveableType,
                        contentType = contentType,
                    )
                } catch (e: Throwable) {
                    Log.e(TAG, e.stackTraceToString())
                    UiState(errorDialog = Dialog.unknownErrorDialog())
                }
            }
        }
    }

    sealed class Action {
        data object OnBackClick : Action()
        data object OnLoadMoreClick : Action()

        data class NavigateToItemDetails(val id: String) : Action()
        data class OnItemClick(val id: String) : Action()
    }

    sealed class SideEffect {
        data object NavigateUp : SideEffect()
        data class NavigateToRecipe(val id: String) : SideEffect()
        data class NavigateToPlace(val geoHash: String) : SideEffect()
    }

    companion object {
        const val NAV_ARG_SAVEABLE_TYPE = "saveable_type"
        const val NAV_ARG_CONTENT_TYPE = "content_type"
    }
}