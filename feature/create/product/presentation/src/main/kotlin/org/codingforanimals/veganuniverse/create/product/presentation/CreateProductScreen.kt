@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.create.product.presentation

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.create.presentation.CreateContentHero
import org.codingforanimals.veganuniverse.commons.create.presentation.HeroAnchorDefaults
import org.codingforanimals.veganuniverse.commons.create.presentation.ImagePicker
import org.codingforanimals.veganuniverse.commons.create.presentation.R.string.publish
import org.codingforanimals.veganuniverse.commons.designsystem.LightBlue
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.product.presentation.toUI
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.commons.ui.components.VUNormalTextField
import org.codingforanimals.veganuniverse.commons.ui.components.VURadioButton
import org.codingforanimals.veganuniverse.commons.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.commons.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.commons.ui.dialog.NoActionDialog
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.codingforanimals.veganuniverse.commons.ui.utils.rememberImageCropperLauncherForActivityResult
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.StringField
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailDialog
import org.codingforanimals.veganuniverse.create.product.presentation.CreateProductViewModel.Action
import org.codingforanimals.veganuniverse.create.product.presentation.CreateProductViewModel.SideEffect
import org.codingforanimals.veganuniverse.create.product.presentation.CreateProductViewModel.UiState
import org.codingforanimals.veganuniverse.create.product.presentation.model.ProductCategoryField
import org.koin.androidx.compose.koinViewModel

@Composable
fun SelectProductCategoryScreen(
    navigateUp: () -> Unit,
    onCategoryClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            VUTopAppBar(
                title = stringResource(R.string.create_product_top_app_bar_title),
                onBackClick = navigateUp,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(Spacing_06)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            Text(
                text = stringResource(R.string.product_category),
                style = MaterialTheme.typography.titleLarge,
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(
                    Spacing_06,
                    Alignment.CenterHorizontally
                ),
                verticalArrangement = Arrangement.spacedBy(Spacing_06),
            ) {
                ProductCategory.entries.forEach { category ->
                    key(category.name) {
                        val categoryUI = remember { category.toUI() }
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            onClick = { onCategoryClick(category.name) },
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
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
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreateProductScreen(
    navigateUp: () -> Unit,
    navigateToThankYouScreen: () -> Unit,
    viewModel: CreateProductViewModel = koinViewModel(),
) {
    val imagePicker = rememberImageCropperLauncherForActivityResult(
        onCropSuccess = { viewModel.onAction(Action.ImagePicker.Success(it)) },
    )

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            VUTopAppBar(
                title = stringResource(R.string.create_product_top_app_bar_title),
                onBackClick = { viewModel.onAction(Action.OnBackClick) }
            )
        }
    ) { paddingValues ->
        CreateProductScreen(
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

    LaunchedEffect(Unit) {
        viewModel.navigationEffects.onEach { navigationEffect ->
            when (navigationEffect) {
                CreateProductViewModel.NavigationEffect.NavigateToThankYouScreen -> navigateToThankYouScreen()
                CreateProductViewModel.NavigationEffect.NavigateUp -> navigateUp()
            }
        }.collect()
    }
}

@Composable
private fun CreateProductScreen(
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
            !uiState.productSupportsImage -> HeroAnchorDefaults.primaryColors()
            uiState.pictureField.isValid -> HeroAnchorDefaults.primaryColors()
            uiState.isValidating -> HeroAnchorDefaults.errorColors()
            else -> HeroAnchorDefaults.secondaryColors()
        }
        CreateContentHero(
            heroAnchorIcon = uiState.heroAnchorIcon,
            heroAnchorColors = heroAnchorColors,
            content = {
                if (uiState.productSupportsImage) {
                    ImagePicker(
                        imageModel = uiState.pictureField.model,
                        isError = uiState.isValidating && !uiState.pictureField.isValid,
                        onClick = { onAction(Action.ImagePicker.Click) },
                    )
                } else {
                    val background = remember {
                        when (uiState.category) {
                            ProductCategory.ADDITIVES -> LightBlue
                            else -> Color.Transparent
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(vertical = Spacing_05)
                            .aspectRatio(1f)
                            .align(Alignment.Center)
                            .clip(CardDefaults.shape)
                            .background(background)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(Spacing_04)
                                .align(Alignment.Center),
                            text = uiState.nameField.value,
                            style = MaterialTheme.typography.headlineMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            },
        )

        Text(
            modifier = Modifier.padding(horizontal = Spacing_06),
            text = stringResource(uiState.title),
            style = MaterialTheme.typography.titleLarge,
        )

        VUNormalTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing_06),
            value = uiState.nameField.value,
            onValueChange = { onAction(Action.OnTextChange.Name(it)) },
            label = stringResource(uiState.productNamePlaceholder),
            isError = uiState.isValidating && !uiState.nameField.isValid,
            maxChars = 64,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next,
            ),
        )

        AnimatedVisibility(uiState.productSupportsBrand) {
            VUNormalTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06),
                value = uiState.brandField.value,
                onValueChange = { onAction(Action.OnTextChange.Brand(it)) },
                label = stringResource(R.string.product_brand),
                isError = uiState.isValidating && !uiState.brandField.isValid,
                maxChars = 64,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
            )
        }

        VUNormalTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing_06),
            value = uiState.commentsField.value,
            onValueChange = { onAction(Action.OnTextChange.Comments(it)) },
            label = stringResource(R.string.product_comments),
            isError = uiState.isValidating && !uiState.commentsField.isValid,
            maxChars = 256,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,
            ),
        )

        val typeColor = when {
            !uiState.isValidating -> MaterialTheme.colorScheme.onSurfaceVariant
            !uiState.productTypeField.isValid -> MaterialTheme.colorScheme.error
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
                style = MaterialTheme.typography.titleLarge,
                color = typeColor,
            )
            val colors = if (uiState.isValidating && !uiState.productTypeField.isValid) {
                RadioButtonDefaults.colors(unselectedColor = MaterialTheme.colorScheme.error)
            } else {
                RadioButtonDefaults.colors()
            }
            ProductType.entries.forEach { type ->
                val typeUI = remember { type.toUI() }
                VURadioButton(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(typeUI.label),
                    selected = uiState.productTypeField.type == type,
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
                style = MaterialTheme.typography.titleLarge,
                color = typeColor,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing_06)
            ) {
                val categoryUI = remember { uiState.category.toUI() }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
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
                        fontWeight = FontWeight.SemiBold,
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
                Text(text = stringResource(publish))
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
                CreateProductScreen(
                    uiState = UiState(category = ProductCategory.OTHER),
                    onAction = {},
                )
            }
        }
    }
}

@Composable
@Preview
private fun PreviewCreateAdditiveScreen() {
    VeganUniverseBackground {
        VeganUniverseTheme {
            Surface {
                CreateProductScreen(
                    uiState = UiState(
                        category = ProductCategory.ADDITIVES,
                        nameField = StringField("INS 311"),
                    ),
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
                CreateProductScreen(
                    uiState = UiState(
                        ProductCategory.OTHER,
                        nameField = StringField("OLEOMARGARINA"),
                    ),
                    onAction = {},
                )
            }
        }
    }
}