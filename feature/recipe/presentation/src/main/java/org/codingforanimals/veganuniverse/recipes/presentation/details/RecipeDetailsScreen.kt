@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.recipes.presentation.details

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.core.ui.R.string.ingredient
import org.codingforanimals.veganuniverse.core.ui.R.string.ingredients
import org.codingforanimals.veganuniverse.core.ui.R.string.prep_time
import org.codingforanimals.veganuniverse.core.ui.R.string.servings
import org.codingforanimals.veganuniverse.core.ui.R.string.steps
import org.codingforanimals.veganuniverse.core.ui.R.string.tags
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.recipes.presentation.details.RecipeDetailsViewModel.Action
import org.codingforanimals.veganuniverse.recipes.presentation.details.RecipeDetailsViewModel.SideEffect
import org.codingforanimals.veganuniverse.recipes.presentation.details.entity.RecipeView
import org.codingforanimals.veganuniverse.ui.Spacing_02
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_05
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.Spacing_08
import org.codingforanimals.veganuniverse.ui.Spacing_09
import org.codingforanimals.veganuniverse.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.ui.featureditem.FeatureItemScreenHero
import org.codingforanimals.veganuniverse.ui.featureditem.FeatureItemScreenTagsFlowRow
import org.codingforanimals.veganuniverse.ui.featureditem.TagItem
import org.codingforanimals.veganuniverse.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.utils.TimeAgo
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun RecipeDetailsScreen(
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    navigateToAuthenticateScreen: () -> Unit,
) {

    val viewModel: RecipeDetailsViewModel = koinViewModel()
    val recipeState: RecipeDetailsViewModel.RecipeState by viewModel.recipeState.collectAsStateWithLifecycle()
    val isLiked: Boolean by viewModel.isLiked.collectAsStateWithLifecycle()
    val isBookmarked: Boolean by viewModel.isBookmarked.collectAsStateWithLifecycle()
    val showImageDialog: Boolean by viewModel.showImageDialog.collectAsStateWithLifecycle()

    RecipeDetailsScreen(
        recipeState = recipeState,
        isLiked = isLiked,
        isBookmarked = isBookmarked,
        showImageDialog = showImageDialog,
        onAction = viewModel::onAction,
    )

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateUp = onBackClick,
        navigateToAuthenticateScreen = navigateToAuthenticateScreen,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
internal fun RecipeDetailsScreen(
    recipeState: RecipeDetailsViewModel.RecipeState,
    isLiked: Boolean,
    isBookmarked: Boolean,
    showImageDialog: Boolean,
    onAction: (Action) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        VUTopAppBar(
            onBackClick = { onAction(Action.OnBackClick) },
            actions = {
                var showMenu by rememberSaveable { mutableStateOf(false) }
                VUIcon(
                    icon = VUIcons.MoreOptions,
                    contentDescription = "",
                    onIconClick = { showMenu = !showMenu },
                )
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                        text = { Text(text = "Reportar receta") },
                        onClick = {},
                        leadingIcon = {
                            VUIcon(icon = VUIcons.Report, contentDescription = "")
                        },
                    )
                }
            }
        )

        when (recipeState) {
            RecipeDetailsViewModel.RecipeState.Error -> Box(modifier = Modifier.fillMaxSize()) {
                val onErrorDialogDismissRequest = { onAction(Action.OnErrorDialogDismissRequest) }
                AlertDialog(
                    title = { Text(text = stringResource(R.string.oops_something_went_wrong)) },
                    text = { Text(text = stringResource(R.string.sorry_can_not_find_recipe_details)) },
                    onDismissRequest = onErrorDialogDismissRequest,
                    confirmButton = {
                        TextButton(onClick = onErrorDialogDismissRequest) {
                            Text(text = stringResource(R.string.back))
                        }
                    }
                )
            }

            RecipeDetailsViewModel.RecipeState.Loading -> VUCircularProgressIndicator()
            is RecipeDetailsViewModel.RecipeState.Success -> RecipeContent(
                recipe = recipeState.recipeView,
                isLiked = isLiked,
                isBookmarked = isBookmarked,
                showImageDialog = showImageDialog,
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun RecipeContent(
    recipe: RecipeView,
    isLiked: Boolean,
    isBookmarked: Boolean,
    showImageDialog: Boolean,
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing_06),
        contentPadding = PaddingValues(bottom = Spacing_08)
    ) {
        item {
            FeatureItemScreenHero(
                heroAnchorIcon = VUIcons.RecipesFilled,
                content = {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { onAction(Action.OnImageClick) },
                        model = recipe.imageUrl,
                        contentDescription = stringResource(R.string.recipe_image_description),
                        contentScale = ContentScale.Crop,
                    )
                },
            )
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06),
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = recipe.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
                Crossfade(
                    targetState = isLiked,
                    label = "is_liked_cross_fade"
                ) { isLiked ->
                    val (icon, tint) = remember(isLiked) {
                        if (isLiked) {
                            Pair(VUIcons.FavoriteFilled, Color.Red)
                        } else {
                            Pair(VUIcons.Favorite, Color.Unspecified)
                        }
                    }
                    VUIcon(
                        icon = icon,
                        onIconClick = { onAction(Action.OnLikeClick) },
                        tint = tint,
                    )
                }
                Crossfade(
                    targetState = isBookmarked,
                    label = "is_bookmarked_cross_fade"
                ) { isBookmarked ->
                    val (icon, tint) = remember(isBookmarked) {
                        if (isBookmarked) {
                            Pair(VUIcons.BookmarkFilled, Color.DarkGray)
                        } else {
                            Pair(VUIcons.Bookmark, Color.Unspecified)
                        }
                    }
                    VUIcon(
                        icon = icon,
                        onIconClick = { onAction(Action.OnBookmarkClick) },
                        tint = tint,
                    )
                }
            }
        }
        item {
            Column(
                modifier = Modifier.padding(horizontal = Spacing_06),
                verticalArrangement = Arrangement.spacedBy(Spacing_05)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing_04)) {
                    VUIcon(
                        icon = VUIcons.Profile,
                        contentDescription = stringResource(servings)
                    )
                    Text(text = recipe.servings)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing_04)) {
                    VUIcon(
                        icon = VUIcons.Clock,
                        contentDescription = stringResource(prep_time)
                    )
                    Text(text = recipe.prepTime)
                }
            }
        }
        recipe.username?.let {
            item {
                Row(
                    modifier = Modifier.padding(horizontal = Spacing_06),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing_04),
                ) {
                    VUIcon(icon = VUIcons.Profile)
                    Column {
                        Text(text = recipe.username, fontWeight = FontWeight.SemiBold)
                        recipe.createdAt?.let {
                            Text(text = TimeAgo.getTimeAgo(time = it.time))
                        }
                    }
                }
            }
        }
        item {
            Text(
                modifier = Modifier.padding(horizontal = Spacing_06),
                text = recipe.description,
            )
        }
        item { HorizontalDivider() }
        item {
            Text(
                text = stringResource(tags),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(
                    bottom = Spacing_04,
                    start = Spacing_06,
                    end = Spacing_06
                )
            )
            FeatureItemScreenTagsFlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_09),
                tags = recipe.tags.map { TagItem(it.icon, it.label) })
        }
        item { HorizontalDivider() }
        item {
            Text(
                text = stringResource(ingredients),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(
                    bottom = Spacing_04,
                    start = Spacing_06,
                    end = Spacing_06
                )
            )
            recipe.ingredients.forEachIndexed { index, ing ->
                key(index) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = Spacing_06)
                            .padding(bottom = Spacing_04),
                    ) {
                        VUIcon(
                            icon = VUIcons.Bullet,
                            contentDescription = stringResource(ingredient)
                        )
                        Text(modifier = Modifier.padding(start = Spacing_02), text = ing)
                    }
                }
            }
        }
        item { HorizontalDivider() }
        item {
            Text(
                text = stringResource(steps),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(
                    bottom = Spacing_04,
                    start = Spacing_06,
                    end = Spacing_06
                )
            )
            recipe.steps.forEachIndexed { index, step ->
                key(index) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = Spacing_06)
                            .padding(bottom = Spacing_04),
                    ) {
                        Badge(
                            modifier = Modifier
                                .size(30.dp)
                                .aspectRatio(1f),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            content = {
                                Text(
                                    modifier = Modifier.offset(y = (2).dp),
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
    }

    if (showImageDialog) {
        Dialog(
            onDismissRequest = { onAction(Action.OnImageDialogDismissRequest) },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing_09),
                model = recipe.imageUrl,
                contentDescription = stringResource(R.string.recipe_image_description),
            )
        }
    }
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateUp: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                SideEffect.NavigateUp -> navigateUp()
                SideEffect.NavigateToAuthenticateScreen -> navigateToAuthenticateScreen()
                is SideEffect.DisplaySnackbar.UnexpectedErrorSnackbar -> {
                    launch {
                        val snackbarResult = snackbarHostState.showSnackbar(
                            message = context.getString(sideEffect.message),
                            actionLabel = sideEffect.actionLabel?.let { context.getString(it) }
                        )
                        when (snackbarResult) {
                            SnackbarResult.Dismissed -> Unit
                            SnackbarResult.ActionPerformed -> sideEffect.action?.invoke()
                        }
                    }
                }
            }
        }.collect()
    }
}
