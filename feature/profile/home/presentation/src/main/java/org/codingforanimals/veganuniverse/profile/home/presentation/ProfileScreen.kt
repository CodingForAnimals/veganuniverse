package org.codingforanimals.veganuniverse.profile.home.presentation

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.common.R.string.back
import org.codingforanimals.veganuniverse.core.common.R.string.update
import org.codingforanimals.veganuniverse.profile.home.presentation.ProfileScreenViewModel.Action
import org.codingforanimals.veganuniverse.profile.home.presentation.ProfileScreenViewModel.ForcedEffect
import org.codingforanimals.veganuniverse.profile.home.presentation.ProfileScreenViewModel.SideEffect
import org.codingforanimals.veganuniverse.profile.home.presentation.ProfileScreenViewModel.UiState
import org.codingforanimals.veganuniverse.profile.home.presentation.components.ContentTitle
import org.codingforanimals.veganuniverse.profile.home.presentation.components.ProfileFeatureContent
import org.codingforanimals.veganuniverse.profile.model.SaveableContentType
import org.codingforanimals.veganuniverse.profile.model.SaveableType
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_05
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.Spacing_08
import org.codingforanimals.veganuniverse.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.dialog.ActionDialog
import org.codingforanimals.veganuniverse.ui.dialog.NoActionDialog
import org.codingforanimals.veganuniverse.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.ui.utils.rememberImageCropperLauncherForActivityResult
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    navigateToRegister: () -> Unit,
    navigateToRecipe: (String) -> Unit,
    navigateToPlace: (String) -> Unit,
    navigateToItemList: (String, String) -> Unit,
    viewModel: ProfileScreenViewModel = koinViewModel(),
) {

    LaunchedEffect(Unit) {
        viewModel.onForcedEffect(ForcedEffect.UpdateSavedContentItems)
    }

    val imageCropperLauncher =
        rememberImageCropperLauncherForActivityResult(onCropSuccess = { profilePicUri ->
            if (profilePicUri != null) {
                viewModel.onAction(Action.NewProfilePictureSelected(profilePicUri))
            }
        })

    HandleSideEffects(
        sideEffects = viewModel.sideEffect,
        navigateToRegister = navigateToRegister,
        imageCropperLauncher = imageCropperLauncher,
        navigateToRecipe = navigateToRecipe,
        navigateToPlace = navigateToPlace,
        navigateToItemList = navigateToItemList,
    )

    ProfileScreen(
        uiState = viewModel.uiState,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun ProfileScreen(
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    Crossfade(
        targetState = uiState.user == null,
        label = "profile_screen_user_animation",
        content = { isGuestUser ->
            if (isGuestUser) {
                GuestUserContent(onAction)
            } else {
                ProfileContent(
                    state = uiState, onAction = onAction
                )
            }
        },
    )

    VUCircularProgressIndicator(visible = uiState.loading)
}

@Composable
private fun GuestUserContent(
    onAction: (Action) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        OutlinedCard(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .padding(Spacing_06)
                .align(Alignment.Center)
                .clickable { onAction(Action.OnCreateUserButtonClick) },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing_06),
                verticalArrangement = Arrangement.spacedBy(Spacing_05),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Text(
                        text = "¡Bienvenido a tu\nUniverso Vegano!",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        text = "Únete a nuestra comunidad y ayudemos juntos a los animales\n\nDescubre contenido exclusivo para usuarios como crear posteos, recetas, lugares, y acceder a tu contenido guardado",
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = { onAction(Action.OnCreateUserButtonClick) }) {
                        Text(text = "Crear mi usuario")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(
    state: UiState,
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing_06),
        contentPadding = PaddingValues(vertical = Spacing_08)
    ) {
        item {
            Box(modifier = Modifier.wrapContentSize()) {
                IconButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                    onClick = { onAction(Action.EditProfilePictureClick) },
                    content = { VUIcon(icon = VUIcons.Edit, contentDescription = "") },
                )
            }
            Column {
                Text(text = state.user?.name ?: "", style = MaterialTheme.typography.titleLarge)
                Text(text = state.user?.email ?: "", style = MaterialTheme.typography.titleMedium)
            }
            Button(onClick = { onAction(Action.LogOut) }) {
                Text(text = "Cerrar session")
            }
        }
        item {
            ContentTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06),
                icon = VUIcons.Bookmark, label = R.string.your_bookmarks
            )
            Crossfade(
                modifier = Modifier.animateContentSize(),
                targetState = state.bookmarks.hasNoItems,
                label = "profile_screen_bookmarks_empty_crossfade",
            ) { hasNoBookmarks ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(Spacing_04),
                ) {
                    if (hasNoBookmarks) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Spacing_06, vertical = Spacing_04),
                            text = stringResource(R.string.bookmarks_empty_message),
                        )
                    } else {
                        ProfileFeatureContent(
                            state = state.bookmarks.recipes,
                            subtitleLabel = R.string.your_recipes,
                            subtitleIcon = VUIcons.Recipes,
                            onShowMoreClick = {
                                onAction(
                                    Action.OnShowMoreClick(
                                        saveableType = SaveableType.BOOKMARK,
                                        contentType = SaveableContentType.RECIPE,
                                    )
                                )
                            },
                            errorLabel = R.string.bookmarks_error_recipes_message,
                            onItemClick = { onAction(Action.OnRecipeClick(it)) }
                        )

                        ProfileFeatureContent(
                            state = state.bookmarks.places,
                            subtitleLabel = R.string.your_places,
                            subtitleIcon = VUIcons.Location,
                            onShowMoreClick = {
                                onAction(
                                    Action.OnShowMoreClick(
                                        saveableType = SaveableType.BOOKMARK,
                                        contentType = SaveableContentType.PLACE,
                                    )
                                )
                            },
                            errorLabel = R.string.bookmarks_error_places_message,
                            onItemClick = { onAction(Action.OnPlaceClick(it)) }
                        )
                    }
                }
            }
        }
        item {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06)
            )
        }
        item {
            ContentTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06),
                icon = VUIcons.Add, label = R.string.your_contributions
            )

            Crossfade(
                modifier = Modifier.animateContentSize(),
                targetState = state.contributions.hasNoItems,
                label = "profile_screen_contributions_empty_crossfade"
            ) { hasNoContributions ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(Spacing_04),
                ) {
                    if (hasNoContributions) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Spacing_06, vertical = Spacing_04),
                            text = stringResource(R.string.contributions_empty_message),
                        )
                    } else {
                        ProfileFeatureContent(
                            state = state.contributions.places,
                            subtitleLabel = R.string.your_places,
                            subtitleIcon = VUIcons.Location,
                            onShowMoreClick = {
                                onAction(
                                    Action.OnShowMoreClick(
                                        saveableType = SaveableType.CONTRIBUTION,
                                        contentType = SaveableContentType.PLACE,
                                    )
                                )
                            },
                            errorLabel = R.string.contributions_error_places_message,
                            onItemClick = { onAction(Action.OnPlaceClick(it)) }
                        )

                        ProfileFeatureContent(
                            state = state.contributions.recipes,
                            subtitleLabel = R.string.your_recipes,
                            subtitleIcon = VUIcons.Recipes,
                            onShowMoreClick = {
                                onAction(
                                    Action.OnShowMoreClick(
                                        saveableType = SaveableType.CONTRIBUTION,
                                        contentType = SaveableContentType.RECIPE,
                                    )
                                )
                            },
                            errorLabel = R.string.contributions_error_recipes_message,
                            onItemClick = { onAction(Action.OnRecipeClick(it)) }
                        )
                    }
                }
            }
        }
    }

    if (state.showImageDialog) {
        Dialog(onDismissRequest = { onAction(Action.DismissImageDialog) }) {
//            Image(
//                modifier = Modifier.wrapContentSize(Alignment.Center),
//                contentDescription = "",
//            )
        }
    } else if (state.showNewProfilePictureConfirmationDialog) {
        ActionDialog(
            title = R.string.confirm_new_profile_picture_title,
            message = R.string.confirm_new_profile_picture_message,
            confirmLabel = update,
            dismissLabel = back,
            onDismissRequest = { onAction(Action.DismissNewProfilePictureConfirmationDialog) },
            onConfirmRequest = { onAction(Action.UpdateProfilePicture) },
        )
    }

    state.errorDialog?.let { errorDialog ->
        NoActionDialog(title = errorDialog.errorTitle,
            message = errorDialog.errorMessage,
            buttonText = back,
            onDismissRequest = { onAction(Action.DismissErrorDialog) })
    }
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateToRegister: () -> Unit,
    imageCropperLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
    navigateToRecipe: (String) -> Unit,
    navigateToPlace: (String) -> Unit,
    navigateToItemList: (String, String) -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { effect ->
            when (effect) {
                SideEffect.NavigateToRegister -> navigateToRegister()
                SideEffect.LaunchImageCropperForSelectingProfilePicture -> {
                    imageCropperLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                is SideEffect.ReloadProfilePicture -> {}
                is SideEffect.NavigateToPlace -> navigateToPlace(effect.geoHash)
                is SideEffect.NavigateToRecipe -> navigateToRecipe(effect.id)
                is SideEffect.NavigateToItemList -> navigateToItemList(
                    effect.saveableType,
                    effect.contentType
                )
            }
        }.collect()
    }
}