@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.product.presentation.edit

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.commons.ui.components.VUNormalTextField
import org.codingforanimals.veganuniverse.commons.ui.components.VURadioButton
import org.codingforanimals.veganuniverse.commons.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.commons.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.commons.ui.create.CreateContentHero
import org.codingforanimals.veganuniverse.commons.ui.create.HeroAnchorDefaults
import org.codingforanimals.veganuniverse.commons.ui.create.ImagePicker
import org.codingforanimals.veganuniverse.commons.ui.dialog.NoActionDialog
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.codingforanimals.veganuniverse.commons.ui.snackbar.LocalSnackbarSender
import org.codingforanimals.veganuniverse.commons.ui.utils.rememberImageCropperLauncherForActivityResult
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailDialog
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory
import org.codingforanimals.veganuniverse.product.domain.model.ProductType
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.edit.EditProductViewModel.Action
import org.codingforanimals.veganuniverse.product.presentation.edit.EditProductViewModel.EntryPoint
import org.codingforanimals.veganuniverse.product.presentation.edit.EditProductViewModel.SideEffect
import org.codingforanimals.veganuniverse.product.presentation.edit.EditProductViewModel.UiState
import org.codingforanimals.veganuniverse.product.presentation.model.toUI
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditProductScreen(
    navigateUp: () -> Unit,
    navigateToThankYouScreen: () -> Unit,
    viewModel: EditProductViewModel = koinViewModel(),
) {
    val imagePicker = rememberImageCropperLauncherForActivityResult(
        onCropSuccess = { viewModel.onAction(Action.ImagePicker.Success(it)) },
    )
    val snackbarHostState = remember { SnackbarHostState() }

    val topBarTitle = remember(viewModel.entryPoint) {
        when (viewModel.entryPoint) {
            is EntryPoint.Edit -> R.string.create_product_top_app_bar_title_edit
            else -> R.string.create_product_top_app_bar_title
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            VUTopAppBar(
                title = stringResource(topBarTitle),
                onBackClick = { viewModel.onAction(Action.OnBackClick) }
            )
        }
    ) { paddingValues ->
        EditProductScreen(
            modifier = Modifier.padding(paddingValues),
            uiState = viewModel.uiState,
            onAction = viewModel::onAction,
        )
    }

    VUCircularProgressIndicator(visible = viewModel.uiState.loading)

    viewModel.uiState.dialog?.let { dialog ->
        NoActionDialog(
            title = dialog.title,
            message = dialog.message,
            buttonText = dialog.back,
            onDismissRequest = { viewModel.onAction(Action.DismissDialog) }
        )
    }

    if (viewModel.showUnverifiedEmailDialog) {
        UnverifiedEmailDialog(onResult = viewModel::onUnverifiedEmailResult)
    }

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        imagePicker = imagePicker,
    )

    HandleSnackbarEffects(
        snackbarEffects = viewModel.snackbarEffects,
        snackbarHostState = snackbarHostState,
    )

    AnimatedVisibility(viewModel.uiState.showCategories) {
        SelectCategoryScreen(
            modifier = Modifier.fillMaxSize(),
            onCategoryClick = {
                viewModel.onAction(Action.OnProductCategorySelected(it))
            },
            currentCategory = viewModel.uiState.category
        )
    }

    val snackbarSender = LocalSnackbarSender.current
    LaunchedEffect(Unit) {
        viewModel.navigationEffects.onEach { navigationEffect ->
            when (navigationEffect) {
                EditProductViewModel.NavigationEffect.NavigateToThankYouScreen -> navigateToThankYouScreen()
                is EditProductViewModel.NavigationEffect.NavigateUp -> {
                    navigationEffect.snackbar?.let {
                        snackbarSender(it)
                    }
                    navigateUp()
                }
            }
        }.collect()
    }
}

@Composable
private fun EditProductScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .animateContentSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Spacing_06),
    ) {
        val heroAnchorColors = when {
            uiState.imageModel != null -> HeroAnchorDefaults.primaryColors()
            uiState.isValidating -> HeroAnchorDefaults.errorColors()
            else -> HeroAnchorDefaults.secondaryColors()
        }
        CreateContentHero(
            heroAnchorIcon = uiState.heroAnchorIcon,
            heroAnchorColors = heroAnchorColors,
            content = {
                if (uiState.imageUrl != null) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = uiState.imageUrl,
                        contentScale = ContentScale.FillWidth,
                        contentDescription = null,
                    )
                } else {
                    ImagePicker(
                        imageModel = uiState.imageModel,
                        isError = uiState.isValidating && uiState.imageModel == null,
                        onClick = { onAction(Action.ImagePicker.Click) },
                    )
                }
            },
        )

        Text(
            modifier = Modifier.padding(horizontal = Spacing_06),
            text = stringResource(R.string.create_product_title),
            style = MaterialTheme.typography.titleMedium,
        )

        VUNormalTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing_06),
            value = uiState.name,
            onValueChange = { onAction(Action.OnTextChange.Name(it)) },
            label = stringResource(R.string.product_name),
            isError = uiState.isValidating && uiState.name.isBlank(),
            maxChars = 64,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next,
            ),
        )

        VUNormalTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing_06),
            value = uiState.brand,
            onValueChange = { onAction(Action.OnTextChange.Brand(it)) },
            label = stringResource(R.string.product_brand),
            isError = uiState.isValidating && uiState.brand.isBlank(),
            maxChars = 64,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next,
            ),
        )

        VUNormalTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing_06),
            value = uiState.description,
            onValueChange = { onAction(Action.OnTextChange.Description(it)) },
            label = stringResource(R.string.product_comments),
            maxChars = 256,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,
            ),
        )

        val typeColor = when {
            !uiState.isValidating -> MaterialTheme.colorScheme.onSurfaceVariant
            uiState.type == null -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing_03),
        ) {
            Text(
                modifier = Modifier.padding(horizontal = Spacing_06),
                text = stringResource(R.string.product_type),
                style = MaterialTheme.typography.titleMedium,
                color = typeColor,
            )
            val colors = if (uiState.isValidating && uiState.type == null) {
                RadioButtonDefaults.colors(unselectedColor = MaterialTheme.colorScheme.error)
            } else {
                RadioButtonDefaults.colors()
            }
            ProductType.entries.forEach { type ->
                val typeUI = remember { type.toUI() }
                VURadioButton(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(typeUI.label),
                    selected = uiState.type == type,
                    icon = typeUI.icon,
                    onClick = { onAction(Action.OnProductTypeSelected(type)) },
                    colors = colors,
                    paddingValues = PaddingValues(horizontal = Spacing_06)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing_06),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            Text(
                text = stringResource(R.string.selected_category),
                style = MaterialTheme.typography.titleMedium,
                color = typeColor,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing_06)
            ) {
                val categoryUI = remember(uiState.category) {
                    uiState.category?.toUI() ?: ProductCategory.OTHER.toUI()
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(Action.OnProductCategoryClick) }
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentScale = ContentScale.Crop,
                        model = categoryUI.imageRef,
                        contentDescription = stringResource(categoryUI.label),
                    )
                    Text(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(Spacing_03),
                        text = stringResource(categoryUI.label),
                        maxLines = 2,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(Modifier.weight(1f))
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .padding(
                    start = Spacing_06,
                    end = Spacing_06,
                    bottom = Spacing_06,
                ),
            onClick = { onAction(Action.OnSubmitClick) },
            content = {
                Text(text = stringResource(R.string.send))
            },
        )
    }
}

@Composable
fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    imagePicker: ActivityResultLauncher<PickVisualMediaRequest>,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                SideEffect.OpenImageSelector -> {
                    imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }
        }.collect()
    }
}

@Composable
@Preview
private fun PreviewCreateProductScreen() {
    VeganUniverseBackground {
        VeganUniverseTheme {
            Surface {
                EditProductScreen(
                    uiState = UiState(category = ProductCategory.OTHER),
                    onAction = {},
                )
            }
        }
    }
}

@Composable
@Preview
private fun PreviewCreateOtherScreen() {
    VeganUniverseBackground {
        VeganUniverseTheme {
            Surface {
                EditProductScreen(
                    uiState = UiState(
                        ProductCategory.OTHER,
                        name = "OLEOMARGARINA",
                    ),
                    onAction = {},
                )
            }
        }
    }
}
