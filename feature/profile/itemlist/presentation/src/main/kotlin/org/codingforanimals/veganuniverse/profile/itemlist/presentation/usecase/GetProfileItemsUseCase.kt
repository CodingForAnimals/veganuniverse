package org.codingforanimals.veganuniverse.profile.itemlist.presentation.usecase

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatus
import org.codingforanimals.veganuniverse.places.services.firebase.FetchPlaceService
import org.codingforanimals.veganuniverse.profile.itemlist.presentation.model.toPlaceCardItem
import org.codingforanimals.veganuniverse.profile.model.SaveableContentType
import org.codingforanimals.veganuniverse.profile.model.SaveableType
import org.codingforanimals.veganuniverse.profile.services.firebase.ProfileLookupsService
import org.codingforanimals.veganuniverse.recipes.services.firebase.FetchRecipeService
import org.codingforanimals.veganuniverse.ui.cards.CardItem
import org.codingforanimals.veganuniverse.ui.cards.SimpleCardItem

private const val TAG = "GetProfileItemsUseCase"

class GetProfileItemsUseCase(
    private val profileLookupsService: ProfileLookupsService,
    private val getUserStatus: GetUserStatus,
    private val fetchRecipeService: FetchRecipeService,
    private val fetchPlaceService: FetchPlaceService,
) {
    suspend operator fun invoke(
        saveableType: SaveableType,
        contentType: SaveableContentType,
        index: Int,
    ): Result {
        return try {
            val userId = getUserStatus()
                .firstOrNull()
                ?.id
                ?: return Result.Error

            val allIds = profileLookupsService
                .getContentSavedByUser(
                    saveableType = saveableType,
                    contentType = contentType,
                    userId = userId,
                ).reversed()

            val fromIndex = index + 1
            val maxIndex = allIds.size - 1
            val toIndex = when {
                fromIndex > maxIndex -> return Result.Success(emptyList(), index)
                fromIndex + 2 <= maxIndex -> fromIndex + 2
                fromIndex + 1 <= maxIndex -> fromIndex + 1
                fromIndex == maxIndex -> fromIndex
                else -> return Result.Error
            }
            val ids = allIds.subList(fromIndex, toIndex + 1)

            val items = when (contentType) {
                SaveableContentType.RECIPE -> fetchRecipeService.byIds(ids).map {
                    SimpleCardItem(
                        id = it.id,
                        title = it.title,
                        imageRef = it.imageRef,
                    )
                }

                SaveableContentType.PLACE -> fetchPlaceService.byIds(ids).mapNotNull {
                    it.toPlaceCardItem()
                }
            }
            Result.Success(
                items = items,
                lastIndex = index + items.size,
            )
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            Result.Error
        }
    }

    sealed class Result {
        data object Error : Result()
        data class Success(val items: List<CardItem>, val lastIndex: Int) : Result()
    }
}