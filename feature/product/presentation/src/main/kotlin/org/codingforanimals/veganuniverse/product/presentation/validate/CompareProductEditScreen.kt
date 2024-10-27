@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.product.presentation.validate

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.commons.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.commons.ui.dialog.ErrorDialog
import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory
import org.codingforanimals.veganuniverse.product.domain.model.ProductEdit
import org.codingforanimals.veganuniverse.product.domain.model.ProductType
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.detail.ProductDetailScreen
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
internal fun CompareProductEditScreen(
    navigateUp: () -> Unit,
) {
    val viewModel = koinViewModel<CompareProductEditViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    CompareProductEditScreen(
        state = state,
        navigateUp = navigateUp,
    )
}

@Composable
private fun CompareProductEditScreen(
    state: CompareProductEditViewModel.State,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
) {
    var title by remember { mutableStateOf<Int?>(null) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            VUTopAppBar(
                onBackClick = navigateUp,
                title = title?.let { stringResource(it) }.orEmpty()
            )
        }
    ) { paddingValues ->
        Crossfade(
            modifier = Modifier.padding(paddingValues),
            targetState = state,
            label = "compare_state"
        ) {
            when (it) {
                CompareProductEditViewModel.State.Error -> {
                    ErrorDialog(navigateUp)
                }

                CompareProductEditViewModel.State.Loading -> {
                    VUCircularProgressIndicator()
                }

                is CompareProductEditViewModel.State.Success -> {
                    val pagerState = rememberPagerState { 2 }
                    LaunchedEffect(pagerState.currentPage) {
                        title = if (pagerState.currentPage == 0) {
                            R.string.edit
                        } else {
                            R.string.original
                        }
                    }
                    HorizontalPager(pagerState) { page ->
                        when (page) {
                            0 -> {
                                ProductDetailScreen(
                                    product = it.edit.asProduct(),
                                    isBookmarked = false,
                                )
                            }
                            1 -> ProductDetailScreen(
                                product = it.original,
                                isBookmarked = false,
                            )                         }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductEdit.asProduct() = remember {
    Product(
        id = originalId,
        userId = userId,
        username = username,
        name = name,
        brand = brand,
        description = description,
        type = type,
        category = category,
        createdAt = createdAt,
        lastUpdatedAt = lastUpdatedAt,
        imageUrl = imageUrl,
        sourceUrl = sourceUrl
    )
}

@Preview
@Composable
private fun PreviewCompareProductEditScreen() {
    VeganUniverseTheme {
        val edit = ProductEdit(
            id = "id",
            userId = "userId",
            username = "username",
            name = "name",
            brand = "brand",
            description = "description",
            type = ProductType.VEGAN,
            category = ProductCategory.CHOCOLATE,
            createdAt = Date(),
            lastUpdatedAt = Date(),
            imageUrl = "imageUrl",
            sourceUrl = "sourceUrl",
            originalId = "originalId",
            editUsername = "pepe",
            editUserId = "",
        )

        val edit2 = edit.copy(
            type = ProductType.NOT_VEGAN,
            category = ProductCategory.BAKED,
            name = "name22",
            brand = "brand22",
            description = "description22",
            imageUrl = "imageUrl22",
            sourceUrl = "sourceUrl22",
        )
        CompareProductEditScreen(
            state = CompareProductEditViewModel.State.Success(
                edit = edit,
                original = edit2.asProduct(),
            )
        )
    }
}
