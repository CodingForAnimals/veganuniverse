@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.recipes.presentation.details

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_09
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.recipe.presentation.toUI
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeTag
import org.codingforanimals.veganuniverse.commons.ui.R.string.contributed_by
import org.codingforanimals.veganuniverse.commons.ui.R.string.ok
import org.codingforanimals.veganuniverse.commons.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.contentdetails.ContentDetailsHero
import org.codingforanimals.veganuniverse.commons.ui.contentdetails.FeatureItemScreenTagsFlowRow
import org.codingforanimals.veganuniverse.commons.ui.contentdetails.TagItem
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialog
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.details.ContentDetailItem
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.codingforanimals.veganuniverse.commons.ui.utils.DateUtils
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.recipes.presentation.details.RecipeDetailsViewModel.Action
import org.codingforanimals.veganuniverse.recipes.presentation.details.RecipeDetailsViewModel.NavigationEffect
import org.codingforanimals.veganuniverse.recipes.presentation.details.entity.RecipeView
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
internal fun RecipeDetailsScreen(
    navigateUp: () -> Unit,
) {
    val viewModel: RecipeDetailsViewModel = koinViewModel()
    val recipeState: RecipeDetailsViewModel.RecipeState by viewModel.recipeState.collectAsStateWithLifecycle()
    val isLiked: Boolean by viewModel.isLiked.collectAsStateWithLifecycle()
    val isBookmarked: Boolean by viewModel.isBookmarked.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    RecipeDetailsScreen(
        recipeState = recipeState,
        isOwner = viewModel.isOwner,
        isLiked = isLiked,
        isBookmarked = isBookmarked,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction,
    )

    HandleDialog(
        dialog = viewModel.dialog,
        onReportResult = { viewModel.onAction(Action.OnReportResult(it)) },
        onDialogDismissRequest = { viewModel.onAction(Action.OnDialogDismissRequest) },
        onConfirmDelete = { viewModel.onAction(Action.OnConfirmDelete) }
    )

    HandleSnackbarEffects(
        snackbarEffects = viewModel.snackbarEffects,
        snackbarHostState = snackbarHostState,
    )

    HandleNavigationEffects(
        navigationEffects = viewModel.navigationEffects,
        navigateUp = navigateUp,
    )
}

@Composable
internal fun RecipeDetailsScreen(
    recipeState: RecipeDetailsViewModel.RecipeState,
    isOwner: Boolean?,
    isLiked: Boolean,
    isBookmarked: Boolean,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onAction: (Action) -> Unit = {},
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            RecipeDetailsTopAppBar(
                recipeState = recipeState,
                isOwner = isOwner,
                isLiked = isLiked,
                onLikeClick = { onAction(Action.OnLikeClick) },
                isBookmarked = isBookmarked,
                onBookmarkClick = { onAction(Action.OnBookmarkClick) },
                onBackClick = { onAction(Action.OnBackClick) },
                onEditClick = { onAction(Action.OnEditClick) },
                onReportClick = { onAction(Action.OnReportClick) },
                onDeleteClick = { onAction(Action.OnDeleteClick) }
            )
        }
    ) { paddingValues ->
        Crossfade(
            targetState = recipeState,
            label = "recipe_cross_fade",
            content = {
                when (it) {
                    RecipeDetailsViewModel.RecipeState.Error -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            AlertDialog(
                                title = { Text(text = stringResource(R.string.oops_something_went_wrong)) },
                                text = { Text(text = stringResource(R.string.sorry_can_not_find_recipe_details)) },
                                onDismissRequest = { onAction(Action.OnErrorDialogDismissRequest) },
                                confirmButton = {
                                    TextButton(
                                        onClick = { onAction(Action.OnErrorDialogDismissRequest) },
                                        content = {
                                            Text(text = stringResource(R.string.back))
                                        }
                                    )
                                }
                            )
                        }
                    }

                    RecipeDetailsViewModel.RecipeState.Loading -> {
                        VUCircularProgressIndicator()
                    }

                    is RecipeDetailsViewModel.RecipeState.Success -> {
                        RecipeContent(
                            modifier = Modifier.padding(paddingValues),
                            recipe = it.recipeView,
                            onAction = onAction,
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun RecipeContent(
    modifier: Modifier = Modifier,
    recipe: RecipeView,
    onAction: (Action) -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing_06),
    ) {
        ContentDetailsHero(
            url = recipe.imageUrl,
            icon = VUIcons.RecipesFilled,
            onImageClick = { onAction(Action.OnImageClick(recipe.imageUrl)) },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing_05),
            verticalArrangement = Arrangement.spacedBy(Spacing_06),
        ) {
            ContentDetailItem(
                title = stringResource(id = R.string.recipe_details_description_title),
                subtitle = recipe.description,
                icon = VUIcons.Community.id,
            )

            ContentDetailItem(
                title = stringResource(id = R.string.servings),
                subtitle = recipe.servings,
                icon = VUIcons.Profile.id,
            )

            ContentDetailItem(
                title = stringResource(id = R.string.prep_time),
                subtitle = recipe.prepTime,
                icon = VUIcons.Clock.id,
            )

            recipe.username?.let { username ->
                val createdAt =
                    recipe.createdAt?.let { DateUtils.getTimeAgo(time = it.time) }
                val contributedBy = "$username${createdAt?.let { ", $it" }}"
                ContentDetailItem(
                    title = stringResource(id = contributed_by),
                    subtitle = contributedBy,
                    icon = VUIcons.Profile.id,
                )
            }

            FeatureItemScreenTagsFlowRow(
                modifier = Modifier.fillMaxWidth(),
                tags = recipe.tags.map { TagItem(it.icon, it.label) }
            )

            ContentDetailItem(
                title = stringResource(id = R.string.ingredients),
                subtitle = {
                    recipe.ingredients.forEachIndexed { index, ing ->
                        key(index) {
                            Row(
                                modifier = Modifier.padding(bottom = Spacing_04),
                            ) {
                                VUIcon(
                                    icon = VUIcons.Bullet,
                                    contentDescription = stringResource(R.string.ingredient)
                                )
                                Text(modifier = Modifier.padding(start = Spacing_02), text = ing)
                            }
                        }
                    }
                }
            )

            ContentDetailItem(
                title = stringResource(id = R.string.step_by_step),
                subtitle = {
                    recipe.steps.forEachIndexed { index, step ->
                        key(index) {
                            Row(
                                modifier = Modifier.padding(bottom = Spacing_04),
                            ) {
                                Badge(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .aspectRatio(1f),
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    content = {
                                        Text(
                                            modifier = Modifier.offset(y = (1).dp),
                                            text = "${index + 1}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                    },
                                )
                                Text(modifier = Modifier.padding(start = Spacing_04), text = step)
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun HandleNavigationEffects(
    navigationEffects: Flow<NavigationEffect>,
    navigateUp: () -> Unit,
) {
    LaunchedEffect(Unit) {
        navigationEffects.onEach { sideEffect ->
            when (sideEffect) {
                NavigationEffect.NavigateUp -> navigateUp()
            }
        }.collect()
    }
}

@Composable
private fun HandleDialog(
    dialog: RecipeDetailsViewModel.Dialog?,
    onReportResult: (ReportContentDialogResult) -> Unit,
    onDialogDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit,
) {
    dialog?.let {
        when (dialog) {
            RecipeDetailsViewModel.Dialog.Edit -> {
                AlertDialog(
                    onDismissRequest = onDialogDismissRequest,
                    confirmButton = {
                        TextButton(onClick = onDialogDismissRequest) {
                            Text(text = stringResource(id = ok))
                        }
                    },
                    title = {
                        Text(text = stringResource(id = R.string.upcoming_feature_edit_recipe_title))
                    },
                    text = {
                        Text(text = stringResource(id = R.string.upcoming_feature_edit_recipe_message))
                    }
                )
            }

            RecipeDetailsViewModel.Dialog.Report -> {
                ReportContentDialog(onResult = onReportResult)
            }

            is RecipeDetailsViewModel.Dialog.Image -> {
                Dialog(
                    onDismissRequest = onDialogDismissRequest,
                    properties = DialogProperties(usePlatformDefaultWidth = false),
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing_09),
                        model = dialog.url,
                        contentDescription = stringResource(R.string.recipe_image_description),
                    )
                }
            }

            RecipeDetailsViewModel.Dialog.Delete -> {
                AlertDialog(
                    onDismissRequest = onDialogDismissRequest,
                    confirmButton = {
                        Button(onClick = onConfirmDelete) {
                            Text(text = stringResource(id = R.string.delete))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = onDialogDismissRequest) {
                            Text(text = stringResource(id = R.string.back))
                        }
                    },
                    title = { Text(text = stringResource(id = R.string.delete_recipe_dialog_title)) },
                    text = { Text(text = stringResource(id = R.string.delete_recipe_dialog_text)) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewRecipeDetailsScreen() {
    VeganUniverseTheme {
        val recipe = remember {
            RecipeView(
                id = "123",
                "123",
                "Username",
                "Panqueques de banana",
                "Estos panqueques de banana son súper fáciles de hacer y solo llevan 3 ingredientes. Para compartir en familia y disfrutar!",
                7,
                Date(1719680580000),
                listOf(
                    RecipeTag.QUICK_RECIPE.toUI(),
                    RecipeTag.SWEET.toUI(),
                    RecipeTag.BREAKFAST_AND_EVENING.toUI()
                ),
                listOf(
                    "2 Bananas",
                    "1 Taza de harina",
                    "1 cda de azucar",
                    "1 cdita de polvo de hornear",
                    "3 cdas de aceite"
                ),
                listOf(
                    "Pisar la banana",
                    "Mezclar con el resto de los ingredientes (asegurarse de no tener grumos)",
                    "Cuando esté la mezcla chirla, poner en fuego mínimo-medio en una hornalla con una panquequera de teflón idealmente",
                    "Dejar 1 o 2 minutos de un lado y cuando se pueda despegar, usar una espátula y con cuidado darlo vuelta",
                    "Dejar otro 1 o 2 minutos del lado reverso y listo!",
                    "¡A disfrutar!"
                ),
                prepTime = "30 minutos",
                "4 panqueques grandes",
                null
            )
        }
        RecipeDetailsScreen(
            recipeState = RecipeDetailsViewModel.RecipeState.Success(recipe),
            isOwner = false,
            isLiked = true,
            isBookmarked = true
        )
    }
}
