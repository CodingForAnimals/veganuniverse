@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.product.presentation.detail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.designsystem.Doubtful
import org.codingforanimals.veganuniverse.commons.designsystem.NoData
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.R.string.bookmark_action
import org.codingforanimals.veganuniverse.commons.ui.R.string.unbookmark_action
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.contentdetails.ContentDetailsHero
import org.codingforanimals.veganuniverse.commons.ui.contentdetails.ContentDetailsHeroDefaults
import org.codingforanimals.veganuniverse.commons.ui.contentdetails.ContentDetailsHeroImageType
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialog
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialog
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.details.ContentDetailItem
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.share.getShareIntent
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailDialog
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailResult
import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory
import org.codingforanimals.veganuniverse.product.domain.model.ProductType
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.detail.ProductDetailViewModel.Action
import org.codingforanimals.veganuniverse.product.presentation.model.toUI
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
internal fun ProductDetailScreen(
    navigateUp: () -> Unit,
    navigateToEditProduct: (String) -> Unit
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

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is ProductDetailViewModel.SideEffect.ShareProductAppLink -> {
                    runCatching {
                        context.startActivity(
                            getShareIntent(
                                textToShare = sideEffect.textToShare,
                                title = context.getString(R.string.share_product_title)
                            )
                        )
                    }.onFailure {
                        Analytics.logNonFatalException(it)
                    }
                }

                is ProductDetailViewModel.SideEffect.NavigateToEditProduct -> {
                    navigateToEditProduct(sideEffect.id)
                }
            }
        }.collect()
    }
}

@Composable
private fun ProductDetailScreen(
    state: ProductDetailViewModel.State,
    isBookmarked: Boolean,
    navigateUp: () -> Unit = {},
    onAction: (Action) -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = navigateUp,
                    ) {
                        Icon(
                            imageVector = VUIcons.ArrowBack.imageVector,
                            contentDescription = stringResource(back),
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onAction(Action.OnEditClick) }
                    ) {
                        VUIcon(icon = VUIcons.Edit)
                    }
                    IconButton(
                        onClick = { onAction(Action.OnReportClick) }
                    ) {
                        VUIcon(icon = VUIcons.Report)
                    }
                }
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
                        isBookmarked = isBookmarked,
                        onAction = onAction,
                    )
                }
            }
        }
    }
}

@Composable
internal fun ProductDetailScreen(
    product: Product,
    isBookmarked: Boolean,
    onAction: (Action) -> Unit = {},
) {
    var imageDialogUrl: String? by remember { mutableStateOf(null) }
    val typeUI = remember { product.type.toUI() }
    val categoryUI = remember { product.category.toUI() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val imageType = when (product.category) {
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

                ProductType.UNKNOWN -> ContentDetailsHeroDefaults.successColors().copy(
                    divider = NoData,
                    iconContainerBorder = NoData,
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing_05),
            verticalArrangement = Arrangement.spacedBy(Spacing_06)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ContentDetailItem(
                    modifier = Modifier.weight(1f),
                    title = product.name,
                    subtitle = product.brand,
                )
                IconButton(
                    onClick = { onAction(Action.OnShareClick) }
                ) {
                    VUIcon(icon = VUIcons.Share)
                }
                IconButton(
                    onClick = { onAction(Action.OnBookmarkClick) }
                ) {
                    Crossfade(
                        targetState = isBookmarked,
                        label = "bookmark_cross_fade",
                        content = { bookmarked ->
                            val (icon, contentDescription) = Pair(
                                VUIcons.BookmarkFilled,
                                unbookmark_action
                            )
                                .takeIf { bookmarked }
                                ?: Pair(VUIcons.Bookmark, bookmark_action)
                            VUIcon(
                                icon = icon,
                                contentDescription = stringResource(id = contentDescription),
                            )
                        }
                    )
                }
            }
            ContentDetailItem(
                title = stringResource(typeUI.label),
                subtitle = stringResource(typeUI.description),
                icon = typeUI.icon.id,
            )

            product.description?.let { description ->
                ContentDetailItem(
                    title = stringResource(id = R.string.comment),
                    subtitle = description,
                )
            }

            ContentDetailItem(
                title = stringResource(id = R.string.category),
                subtitle = stringResource(id = categoryUI.label),
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
    description = "Rico y económico. 100% vegano. Recomiendo!!",
    type = ProductType.VEGAN,
    category = ProductCategory.CHOCOLATE,
    userId = "123123",
    username = "Paola Argento",
    imageUrl = null,
    createdAt = Date(),
    sourceUrl = "https://url-del-producto.com",
    lastUpdatedAt = Date(),
)

@Preview
@Composable
private fun PreviewVeganProductDetailScreen() {
    VeganUniverseTheme {
        Surface {
            ProductDetailScreen(
                state = ProductDetailViewModel.State.Success(productPreview),
                isBookmarked = false,
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
                state = ProductDetailViewModel.State.Success(product),
                isBookmarked = false,
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
                state = ProductDetailViewModel.State.Success(product),
                isBookmarked = false,
            )
        }
    }
}
