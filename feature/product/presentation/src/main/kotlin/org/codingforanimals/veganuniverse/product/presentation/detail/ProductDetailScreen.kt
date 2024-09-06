@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.product.presentation.detail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.commons.designsystem.Doubtful
import org.codingforanimals.veganuniverse.commons.designsystem.LightBlue
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.product.presentation.resolveBrand
import org.codingforanimals.veganuniverse.commons.product.presentation.toUI
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.contentdetails.ContentDetailsHero
import org.codingforanimals.veganuniverse.commons.ui.contentdetails.ContentDetailsHeroDefaults
import org.codingforanimals.veganuniverse.commons.ui.contentdetails.ContentDetailsHeroImageType
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialog
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialog
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.details.ContentDetailItem
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailDialog
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailResult
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.detail.ProductDetailViewModel.Action
import org.codingforanimals.veganuniverse.product.presentation.detail.components.ProductDetailTopBar
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
internal fun ProductDetailScreen(
    navigateUp: () -> Unit,
) {
    val viewModel: ProductDetailViewModel = koinViewModel()
    val state by viewModel.product.collectAsStateWithLifecycle()
    val isBookmarked by viewModel.isBookmarked.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ProductDetailScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        isBookmarked = isBookmarked,
        navigateUp = navigateUp,
        onAction = viewModel::onAction,
    )

    ProductDetailDialog(
        dialog = viewModel.dialog,
        onReportResult = viewModel::onReportResult,
        onSuggestionResult = viewModel::onSuggestionResult,
        onUnverifiedEmailResult = viewModel::onUnverifiedEmailResult
    )

    HandleSnackbarEffects(
        snackbarEffects = viewModel.snackbarEffects,
        snackbarHostState = snackbarHostState,
    )

}

@Composable
private fun ProductDetailScreen(
    state: ProductDetailViewModel.State,
    snackbarHostState: SnackbarHostState,
    isBookmarked: Boolean,
    navigateUp: () -> Unit,
    onAction: (Action) -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ProductDetailTopBar(
                state = state,
                isBookmarked = isBookmarked,
                navigateUp = navigateUp,
                onBookmarkClick = { onAction(Action.OnBookmarkClick) },
                onEditClick = { onAction(Action.OnSuggestClick) },
                onReportClick = { onAction(Action.OnReportClick) },
            )
        }
    ) { paddingValues ->
        Crossfade(
            modifier = Modifier.padding(paddingValues),
            targetState = state,
            label = "product_detail_cross_fade",
        ) {
            when (val currentState = it) {
                ProductDetailViewModel.State.Error -> {
                    AlertDialog(
                        onDismissRequest = navigateUp,
                        confirmButton = {
                            TextButton(
                                onClick = navigateUp,
                                content = { Text(text = stringResource(id = back)) }
                            )
                        }
                    )
                }

                ProductDetailViewModel.State.Loading -> Unit
                is ProductDetailViewModel.State.Success -> {
                    ProductDetailScreen(
                        product = currentState.product,
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductDetailScreen(product: Product) {
    var imageDialogUrl: String? by remember { mutableStateOf(null) }
    val typeUI = remember { product.type.toUI() }
    val categoryUI = remember { product.category.toUI() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val imageType = when (product.category) {
            ProductCategory.ADDITIVES -> ContentDetailsHeroImageType.Text(
                text = product.name,
                containerColor = LightBlue
            )

            else -> ContentDetailsHeroImageType.Image(product.imageUrl)
        }
        ContentDetailsHero(
            imageType = imageType,
            icon = typeUI.icon,
            onImageClick = { imageDialogUrl = product.imageUrl },
            colors = when (product.type) {
                ProductType.VEGAN -> ContentDetailsHeroDefaults.successColors()
                ProductType.NOT_VEGAN -> ContentDetailsHeroDefaults.errorColors()
                ProductType.DOUBTFUL -> ContentDetailsHeroDefaults.successColors().copy(
                    divider = Doubtful,
                    iconContainerBorder = Doubtful,
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing_05),
            verticalArrangement = Arrangement.spacedBy(Spacing_06)
        ) {
            ContentDetailItem(
                title = product.name,
                subtitle = product.resolveBrand,
            )
            ContentDetailItem(
                title = stringResource(typeUI.label),
                subtitle = stringResource(typeUI.description),
                icon = typeUI.icon.id,
            )

            product.comment?.let { comment ->
                ContentDetailItem(
                    title = stringResource(id = R.string.comment),
                    subtitle = comment,
                    icon = VUIcons.Community.id,
                )
            }

            ContentDetailItem(
                modifier = Modifier.padding(top = Spacing_05),
                title = stringResource(id = R.string.category),
                subtitle = stringResource(id = categoryUI.label),
                icon = VUIcons.Comment.id,
            )

            /**
             * Displaying contributor name temporarily disabled
            product.username?.let {
            val timeAgo =
            product.createdAt?.time?.let { ", ${DateUtils.getTimeAgo(it)}" }
            val subtitle = remember { "$it$timeAgo" }
            ContentDetailItem(
            modifier = Modifier.padding(top = Spacing_05),
            title = stringResource(id = contributed_by),
            subtitle = subtitle,
            icon = VUIcons.Profile.id
            )
            }
             */
        }
    }

    imageDialogUrl?.let {
        Dialog(
            onDismissRequest = { imageDialogUrl = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f),
                model = imageDialogUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
private fun ProductDetailDialog(
    dialog: ProductDetailViewModel.Dialog?,
    onReportResult: (ReportContentDialogResult) -> Unit,
    onSuggestionResult: (EditContentDialogResult) -> Unit,
    onUnverifiedEmailResult: (UnverifiedEmailResult) -> Unit,
) {
    when (dialog) {
        ProductDetailViewModel.Dialog.Report -> {
            ReportContentDialog(onResult = onReportResult)
        }

        ProductDetailViewModel.Dialog.Suggestion -> {
            EditContentDialog(onResult = onSuggestionResult)
        }

        ProductDetailViewModel.Dialog.UnverifiedEmail -> {
            UnverifiedEmailDialog(onResult = onUnverifiedEmailResult)
        }

        null -> Unit
    }
}

private val productPreview = Product(
    id = "123",
    name = "Producto Pepe",
    brand = "Argento's",
    comment = "Rico y económico. 100% vegano. Recomiendo!!",
    type = ProductType.VEGAN,
    category = ProductCategory.CHOCOLATES,
    userId = "123123",
    username = "Paola Argento",
    imageUrl = null,
    createdAt = Date(),
    validated = true,
)

@Preview
@Composable
private fun PreviewVeganProductDetailScreen() {
    VeganUniverseTheme {
        Surface {
            ProductDetailScreen(
                product = productPreview,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewNotVeganProductDetailScreen() {
    VeganUniverseTheme {
        Surface {
            val product = productPreview.copy(type = ProductType.NOT_VEGAN)
            ProductDetailScreen(
                product = product,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewDoubtfulVeganProductDetailScreen() {
    VeganUniverseTheme {
        Surface {
            val product = productPreview.copy(type = ProductType.DOUBTFUL)
            ProductDetailScreen(
                product = product,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewAdditiveProductDetailScreen() {
    VeganUniverseTheme {
        Surface {
            val product = productPreview.copy(
                type = ProductType.DOUBTFUL,
                category = ProductCategory.ADDITIVES,
                name = "INS 311"
            )
            ProductDetailScreen(
                product = product,
            )
        }
    }
}
