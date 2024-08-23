@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.create.product.presentation

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.create.product.presentation.CreateProductViewModel.Action
import org.codingforanimals.veganuniverse.create.product.presentation.CreateProductViewModel.SideEffect
import org.codingforanimals.veganuniverse.create.product.presentation.CreateProductViewModel.UiState
import org.codingforanimals.veganuniverse.create.ui.CreateContentHero
import org.codingforanimals.veganuniverse.create.ui.HeroAnchorDefaults
import org.codingforanimals.veganuniverse.create.ui.ImagePicker
import org.codingforanimals.veganuniverse.create.ui.R.string.submit_button_label
import org.codingforanimals.veganuniverse.product.model.ProductCategory
import org.codingforanimals.veganuniverse.product.model.ProductType
import org.codingforanimals.veganuniverse.product.presentation.toUI
import org.codingforanimals.veganuniverse.ui.Spacing_03
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.VeganUniverseTheme
import org.codingforanimals.veganuniverse.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.ui.components.VUNormalTextField
import org.codingforanimals.veganuniverse.ui.components.VURadioButton
import org.codingforanimals.veganuniverse.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.ui.dialog.NoActionDialog
import org.codingforanimals.veganuniverse.ui.utils.rememberImageCropperLauncherForActivityResult
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateProductScreen(
    navigateUp: () -> Unit,
    navigateToThankYouScreen: () -> Unit,
    navigateToAuthenticationScreen: () -> Unit,
    viewModel: CreateProductViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val imagePicker = rememberImageCropperLauncherForActivityResult(
        onCropSuccess = { viewModel.onAction(Action.ImagePicker.Success(it)) },
    )

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateUp = navigateUp,
        imagePicker = imagePicker,
        navigateToThankYouScreen = navigateToThankYouScreen,
        navigateToAuthenticationScreen = navigateToAuthenticationScreen,
    )

    Column {
        VUTopAppBar(
            title = stringResource(R.string.create_product_top_app_bar_title),
            onBackClick = { viewModel.onAction(Action.OnBackClick) }
        )


        CreateProductScreen(
            uiState = uiState,
            onAction = viewModel::onAction,
        )
    }

    VUCircularProgressIndicator(visible = uiState.loading)

    uiState.dialog?.let { dialog ->
        NoActionDialog(
            title = dialog.title,
            message = dialog.message,
            buttonText = dialog.back,
            onDismissRequest = { viewModel.onAction(Action.DismissDialog) }
        )
    }
}

@Composable
private fun CreateProductScreen(
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing_06),
        contentPadding = PaddingValues(bottom = Spacing_06)
    ) {
        item {
            val heroAnchorColors = when {
                uiState.pictureField.isValid -> HeroAnchorDefaults.primaryColors()
                uiState.isValidating -> HeroAnchorDefaults.errorColors()
                else -> HeroAnchorDefaults.secondaryColors()
            }
            CreateContentHero(
                heroAnchorIcon = uiState.heroAnchorIcon,
                heroAnchorColors = heroAnchorColors,
                content = {
                    ImagePicker(
                        imageModel = uiState.pictureField.model,
                        isError = uiState.isValidating && !uiState.pictureField.isValid,
                        onClick = { onAction(Action.ImagePicker.Click) },
                    )
                },
            )
        }

        item {
            Text(
                modifier = Modifier.padding(horizontal = Spacing_06),
                text = stringResource(R.string.create_product_title),
                style = MaterialTheme.typography.titleLarge,
            )
        }

        item {
            VUNormalTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06),
                value = uiState.nameField.value,
                onValueChange = { onAction(Action.OnTextChange.Name(it)) },
                label = stringResource(R.string.product_name),
                isError = uiState.isValidating && !uiState.nameField.isValid,
                maxChars = 64,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
            )
        }

        item {
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

        item {
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
        }

        item {
            val color = when {
                !uiState.isValidating -> MaterialTheme.colorScheme.onSurfaceVariant
                !uiState.productTypeField.isValid -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing_04),
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = Spacing_06),
                    text = stringResource(R.string.product_type),
                    style = MaterialTheme.typography.titleLarge,
                    color = color,
                )
                val colors = if (uiState.isValidating && !uiState.productTypeField.isValid) {
                    RadioButtonDefaults.colors(unselectedColor = MaterialTheme.colorScheme.error)
                } else {
                    RadioButtonDefaults.colors()
                }
                ProductType.values().forEach { type ->
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
        }

        item {
            val color = when {
                !uiState.isValidating -> MaterialTheme.colorScheme.onSurfaceVariant
                !uiState.productCategoryField.isValid -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06),
                verticalArrangement = Arrangement.spacedBy(Spacing_04),
            ) {
                Text(
                    text = stringResource(R.string.product_category),
                    style = MaterialTheme.typography.titleLarge,
                    color = color,
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
                    ProductCategory.values().forEach { category ->
                        key(category.name) {
                            val categoryUI = remember { category.toUI() }
                            val border = if (uiState.productCategoryField.category == category) {
                                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                            } else {
                                null
                            }
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .shadow(10.dp, shape = CardDefaults.shape),
                                onClick = { onAction(Action.OnProductCategorySelected(category)) },
                                border = border
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

        item {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
                    .padding(horizontal = Spacing_06),
                onClick = { onAction(Action.OnSubmitClick) },
                content = {
                    Text(text = stringResource(submit_button_label))
                },
            )
        }
    }
}

@Composable
fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateUp: () -> Unit,
    imagePicker: ActivityResultLauncher<PickVisualMediaRequest>,
    navigateToThankYouScreen: () -> Unit,
    navigateToAuthenticationScreen: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                SideEffect.NavigateUp -> navigateUp()
                SideEffect.OpenImageSelector -> {
                    imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                SideEffect.NavigateToThankYouScreen -> navigateToThankYouScreen()
                SideEffect.NavigateToAuthenticateScreen -> navigateToAuthenticationScreen()
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
                    uiState = UiState(),
                    onAction = {},
                )
            }
        }
    }
}