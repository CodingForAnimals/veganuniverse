@file:OptIn(ExperimentalFoundationApi::class)

package org.codingforanimals.veganuniverse.create.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceScreen
import org.codingforanimals.veganuniverse.create.presentation.post.CreatePostScreen
import org.codingforanimals.veganuniverse.create.presentation.product.CreateProductScreen
import org.codingforanimals.veganuniverse.create.presentation.recipe.CreateRecipeScreen

@Composable
internal fun CreateScreen(
    navigateToThankYouScreen: () -> Unit,
    navigateToAlreadyExistingPlace: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {
        val state = rememberPagerState()
        HorizontalPager(
            pageCount = products.size,
            state = state,
            userScrollEnabled = false,
        ) {
            when (products[it]) {
                is PostScreenId -> CreatePostScreen()
                is PlaceScreenId -> CreatePlaceScreen(
                    onCreateSuccess = navigateToThankYouScreen,
                    navigateToAlreadyExistingPlace = navigateToAlreadyExistingPlace,
                    navigateToAuthenticateScreen = navigateToAuthenticateScreen,
                )
                is ProductScreenId -> CreateProductScreen()
                is RecipeScreenId -> CreateRecipeScreen()
                is OtherScreenId -> {
                    Text(
                        text = "Otra pantalla",
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        BottomPageScroller(
            modifier = Modifier.align(Alignment.BottomCenter),
            index = state.currentPage,
            onItemClick = { coroutineScope.launch { state.animateScrollToPage(it) } }
        )
    }
}