@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.product.presentation.detail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.U
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.product.presentation.toUI
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.R.string.bookmark_action
import org.codingforanimals.veganuniverse.commons.ui.R.string.contributed_by
import org.codingforanimals.veganuniverse.commons.ui.R.string.edit
import org.codingforanimals.veganuniverse.commons.ui.R.string.report
import org.codingforanimals.veganuniverse.commons.ui.R.string.unbookmark_action
import org.codingforanimals.veganuniverse.commons.ui.contentdetails.ContentDetailsHero
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialog
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialog
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.details.ContentDetailItem
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.codingforanimals.veganuniverse.commons.ui.utils.DateUtils
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailDialog
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailResult
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.detail.ProductDetailViewModel.Action
import org.codingforanimals.veganuniverse.product.presentation.detail.ProductDetailViewModel.NavigationEffect
import org.codingforanimals.veganuniverse.product.presentation.model.Product
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
internal fun ProductDetailScreen(
    navigateUp: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
) {
    val viewModel: ProductDetailViewModel = koinViewModel()
    val state by viewModel.product.collectAsStateWithLifecycle()
    val isBookmarked by viewModel.isBookmarked.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    when (val currentState = state) {
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
                navigateUp = navigateUp,
                isBookmarked = isBookmarked,
                onAction = viewModel::onAction,
            )
        }
    }

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

    LaunchedEffect(Unit) {
        viewModel.navigationEffects.onEach { effect ->
            when (effect) {
                NavigationEffect.NavigateToAuthenticationScreen -> navigateToAuthenticateScreen()
            }
        }.collect()
    }
}

@Composable
private fun ProductDetailScreen(
    product: Product,
    isBookmarked: Boolean,
    navigateUp: () -> Unit = {},
    onAction: (Action) -> Unit = {},
) {
    var imageDialogUrl: String? by remember { mutableStateOf(null) }
    Scaffold(
        topBar = {
            ProductDetailTopBar(
                name = product.name,
                isBookmarked = isBookmarked,
                navigateUp = navigateUp,
                onBookmarkClick = { onAction(Action.OnBookmarkClick) },
                onEditClick = { onAction(Action.OnSuggestClick) },
                onReportClick = { onAction(Action.OnReportClick) },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            ContentDetailsHero(
                url = product.imageUrl,
                icon = VUIcons.VeganLogo,
                onImageClick = { imageDialogUrl = product.imageUrl },
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing_05)
            ) {
                product.type?.let { productType ->
                    ContentDetailItem(
                        title = stringResource(id = productType.label),
                        subtitle = stringResource(id = productType.description),
                        icon = productType.icon.id,
                    )
                }

                product.comment?.let { comment ->
                    ContentDetailItem(
                        modifier = Modifier.padding(top = Spacing_05),
                        title = stringResource(id = R.string.comment),
                        subtitle = comment,
                        icon = VUIcons.Community.id,
                    )
                }

                product.category?.let { category ->
                    ContentDetailItem(
                        title = stringResource(id = R.string.category),
                        subtitle = stringResource(id = category.label),
                        icon = VUIcons.Comment.id,
                    )
                }

                product.username?.let {
                    val timeAgo =
                        product.createdAt?.time?.let { ", ${DateUtils.getTimeAgo(it)}" }
                    val subtitle = remember { "$it$timeAgo" }
                    ContentDetailItem(
                        modifier = Modifier.padding(top = Spacing_05),
                        title = stringResource(id = contributed_by),
                        subtitle = subtitle,
                        icon = VUIcons.Create.id
                    )
                }
            }
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
private fun ProductDetailTopBar(
    name: String,
    isBookmarked: Boolean,
    navigateUp: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
) {
    MediumTopAppBar(
        title = { Text(text = name) },
        navigationIcon = {
            IconButton(
                onClick = navigateUp,
                content = {
                    Icon(
                        imageVector = VUIcons.ArrowBack.imageVector,
                        contentDescription = stringResource(id = back),
                    )
                }
            )
        },
        actions = {
            IconButton(onClick = onBookmarkClick) {
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
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(icon.id),
                            contentDescription = stringResource(id = contentDescription),
                        )
                    }
                )
            }

            IconButton(
                onClick = onEditClick,
                content = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = VUIcons.Edit.id),
                        contentDescription = stringResource(id = edit),
                    )
                }
            )
            IconButton(
                onClick = onReportClick,
                content = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = VUIcons.Report.id),
                        contentDescription = stringResource(id = report),
                    )
                }
            )
        }
    )
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
    name = "El Pepaso",
    brand = "Argento",
    comment = "Antes no era vegano pero ahora sí lo es.",
    type = ProductType.VEGAN.toUI(),
    category = ProductCategory.BAKED_GOODS.toUI(),
    userId = "123123",
    username = "Pepe Argento",
    imageUrl = "",
    createdAt = Date()
)

@Preview
@Composable
private fun PreviewVeganProductDetailScreen() {
    VeganUniverseTheme {
        ProductDetailScreen(
            product = productPreview,
            isBookmarked = false,
        )
    }
}

@Preview
@Composable
private fun PreviewNotVeganProductDetailScreen() {
    VeganUniverseTheme {
        val product = productPreview.copy(type = ProductType.NOT_VEGAN.toUI())
        ProductDetailScreen(
            product = product,
            isBookmarked = false,
        )
    }
}

@Preview
@Composable
private fun PreviewDoubtfulVeganProductDetailScreen() {
    VeganUniverseTheme {
        val product = productPreview.copy(type = ProductType.DOUBTFUL.toUI())
        ProductDetailScreen(
            product = product,
            isBookmarked = false,
        )
    }
}
