@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.recipes.presentation.recipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import java.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.ui.R.drawable.vegan_restaurant
import org.codingforanimals.veganuniverse.core.ui.R.string.back
import org.codingforanimals.veganuniverse.core.ui.R.string.ingredient
import org.codingforanimals.veganuniverse.core.ui.R.string.ingredients
import org.codingforanimals.veganuniverse.core.ui.R.string.prep_time
import org.codingforanimals.veganuniverse.core.ui.R.string.servings
import org.codingforanimals.veganuniverse.core.ui.R.string.steps
import org.codingforanimals.veganuniverse.core.ui.R.string.tags
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.core.ui.error.NoActionDialog
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_08
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_09
import org.codingforanimals.veganuniverse.core.ui.theme.VeganUniverseTheme
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.RecipeDetailsViewModel.Action
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.RecipeDetailsViewModel.SideEffect
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.RecipeDetailsViewModel.UiState
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.entity.RecipeView
import org.codingforanimals.veganuniverse.recipes.ui.RecipeTag
import org.codingforanimals.veganuniverse.shared.ui.ToggleableIcon
import org.codingforanimals.veganuniverse.shared.ui.featureditem.FeatureItemScreenHero
import org.codingforanimals.veganuniverse.shared.ui.featureditem.FeatureItemScreenTagsFlowRow
import org.codingforanimals.veganuniverse.shared.ui.featureditem.TagItem
import org.codingforanimals.veganuniverse.utils.TimeAgo
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun RecipeDetailsScreen(
    onBackClick: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    viewModel: RecipeDetailsViewModel = koinViewModel(),
) {

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateUp = onBackClick,
        navigateToAuthenticateScreen = navigateToAuthenticateScreen,
    )

    RecipeDetailsScreen(
        uiState = viewModel.uiState,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun RecipeDetailsScreen(
    uiState: UiState,
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
        RecipeContent(
            uiState = uiState,
            onAction = onAction
        )

        if (uiState.openImageDialog) {
            Dialog(
                onDismissRequest = { onAction(Action.OnImageDialogDismissRequest) },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing_09),
                    model = uiState.recipe?.imageRef,
                    contentDescription = stringResource(R.string.recipe_image_description)
                )
            }
        }
    }

    uiState.dialog?.let { dialog ->
        NoActionDialog(
            title = dialog.title,
            message = dialog.message,
            buttonText = back,
            onDismissRequest = { onAction(Action.OnBackClick) }
        )
    }
}

@Composable
private fun RecipeContent(
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    uiState.recipe?.let { recipe ->
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
                            model = recipe.imageRef,
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
//                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = recipe.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                    ToggleableIcon(
                        toggled = uiState.likeState.toggled,
                        loading = uiState.likeState.loading,
                        onIconClick = { onAction(Action.OnLikeClick) },
                        onIcon = VUIcons.FavoriteFilled,
                        onTint = Color.Red,
                        offIcon = VUIcons.Favorite,
                    )
                    ToggleableIcon(
                        toggled = uiState.bookmarkState.toggled,
                        loading = uiState.bookmarkState.loading,
                        onIconClick = { onAction(Action.OnBookmarkClick) },
                        onIcon = VUIcons.BookmarkFilled,
                        onTint = MaterialTheme.colorScheme.primary,
                        offIcon = VUIcons.Bookmark,
                    )
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
            item {
                Row(
                    modifier = Modifier.padding(horizontal = Spacing_06),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing_04),
                ) {
                    Image(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(vegan_restaurant),
                        contentDescription = "Imágen del usuario creador del post",
                    )
                    Column {
                        Text(text = recipe.username, fontWeight = FontWeight.SemiBold)
                        Text(text = TimeAgo.getTimeAgo(time = recipe.createdAt.time))
                    }
                }
            }
            item {
                Text(
                    modifier = Modifier.padding(horizontal = Spacing_06),
                    text = recipe.description,
                )
            }
            item { Divider() }
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
            item { Divider() }
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
            item { Divider() }
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
    }
}

@Composable
private fun Divider() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.LightGray)
    )
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateUp: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                SideEffect.NavigateUp -> navigateUp()
                SideEffect.NavigateToAuthenticateScreen -> navigateToAuthenticateScreen()
            }
        }.collect()
    }
}

@Composable
@Preview
private fun PreviewRecipeScreen() {
    VeganUniverseTheme {
        Surface {
            RecipeDetailsScreen(
                uiState = UiState(
                    recipe = RecipeView(
                        id = "",
                        userId = "",
                        username = "User name",
                        title = "Titulo de receta",
                        description = "Esta es la descripcion de la receta",
                        likes = 3,
                        createdAt = Date(),
                        tags = listOf(
                            RecipeTag.QUICK_RECIPE,
                            RecipeTag.LUNCH_AND_DINNER,
                            RecipeTag.BREAKFAST_AND_EVENING,
                            RecipeTag.SALTY,
                            RecipeTag.GLUTEN_FREE
                        ),
                        ingredients = listOf("ingrediente 1", "ingrediente 2"),
                        steps = listOf("paso 1", "paso 2"),
                        prepTime = "30 minutos",
                        servings = "4 porciones",
                        imageRef = "",
                    )
                ),
                onAction = {})
        }
    }
}
