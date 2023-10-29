package org.codingforanimals.veganuniverse.profile.presentation

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.common.R.string.back
import org.codingforanimals.veganuniverse.core.common.R.string.update
import org.codingforanimals.veganuniverse.core.ui.R.drawable.ic_bookmark
import org.codingforanimals.veganuniverse.core.ui.R.string.show_more
import org.codingforanimals.veganuniverse.core.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.error.ActionDialog
import org.codingforanimals.veganuniverse.core.ui.error.ErrorView
import org.codingforanimals.veganuniverse.core.ui.error.NoActionDialog
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.places.ui.compose.PlaceCard
import org.codingforanimals.veganuniverse.profile.presentation.ProfileScreenViewModel.Action
import org.codingforanimals.veganuniverse.profile.presentation.ProfileScreenViewModel.SideEffect
import org.codingforanimals.veganuniverse.profile.presentation.ProfileScreenViewModel.UiState
import org.codingforanimals.veganuniverse.profile.presentation.model.BookmarkState
import org.codingforanimals.veganuniverse.profile.presentation.model.ContributionState
import org.codingforanimals.veganuniverse.shared.ui.cards.LoadingSimpleCard
import org.codingforanimals.veganuniverse.shared.ui.cards.SimpleCard
import org.codingforanimals.veganuniverse.utils.rememberImageCropperLauncherForActivityResult
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ProfileScreen(
    navigateToRegister: () -> Unit,
    test: () -> Unit,
    viewModel: ProfileScreenViewModel = koinViewModel(),
) {
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
        test = test,
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
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing_06)
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
                Text(text = "Cerrar sesión")
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing_04)
            ) {
                VUIcon(
                    icon = VUIcons.Bookmark,
                    contentDescription = stringResource(R.string.your_bookmarks)
                )
                Text(
                    text = stringResource(R.string.your_bookmarks),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Row(
                modifier = Modifier.padding(vertical = Spacing_04, horizontal = Spacing_06),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.your_recipes),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                TextButton(onClick = {}) {
                    Text(text = stringResource(show_more))
                }
            }
            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = state.bookmarks.recipes,
                label = "profile_screen_bookmarks_recipes_crossfade"
            ) { recipes ->
                when (recipes) {
                    BookmarkState.Error -> {
                        ErrorView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Spacing_06),
                            message = R.string.bookmarks_error_recipes_message
                        )
                    }

                    BookmarkState.Loading -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spacing_06)
                        ) {
                            LoadingSimpleCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = Spacing_06)
                                    .aspectRatio(2f)
                            )
                            LoadingSimpleCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = Spacing_06)
                                    .aspectRatio(2f)
                            )
                        }
                    }

                    is BookmarkState.Success -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spacing_04),
                        ) {
                            if (recipes.items.isEmpty()) {
                                Text(text = stringResource(R.string.bookmarks_empty_recipes_message))
                                Image(
                                    painter = painterResource(ic_bookmark),
                                    contentDescription = stringResource(R.string.your_recipes)
                                )
                            } else {
                                recipes.items.forEachIndexed { index, cardItem ->
                                    key(index) {
                                        SimpleCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    bottom = Spacing_06,
                                                    start = Spacing_06,
                                                    end = Spacing_06
                                                )
                                                .aspectRatio(2f),
                                            model = cardItem,
                                            onClick = {},
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing_04)
            ) {
                VUIcon(
                    icon = VUIcons.Add,
                    contentDescription = stringResource(R.string.your_contributions)
                )
                Text(
                    text = stringResource(R.string.your_contributions),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Row(
                modifier = Modifier.padding(vertical = Spacing_04, horizontal = Spacing_06),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.your_places),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                TextButton(onClick = {}) {
                    Text(text = stringResource(show_more))
                }
            }

            Crossfade(
                modifier = Modifier.fillMaxWidth(),
                targetState = state.contributions.places,
                label = "profile_screen_contributions_crossfade",
            ) { places ->
                when (places) {
                    ContributionState.Error -> {
                        ErrorView(message = R.string.contributions_error_places_message)
                    }

                    ContributionState.Loading -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spacing_06)
                        ) {
                            LoadingSimpleCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = Spacing_06)
                                    .aspectRatio(2f)
                            )
                            LoadingSimpleCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = Spacing_06)
                                    .aspectRatio(2f)
                            )
                        }
                    }

                    is ContributionState.Success -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spacing_04),
                        ) {
                            if (places.items.isEmpty()) {
                                Text(text = stringResource(R.string.contributions_empty_places_message))
                                Image(
                                    painter = painterResource(ic_bookmark),
                                    contentDescription = stringResource(R.string.your_places)
                                )
                            } else {
                                places.items.forEachIndexed { index, card ->
                                    key(index) {
                                        PlaceCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    bottom = Spacing_06,
                                                    start = Spacing_06,
                                                    end = Spacing_06
                                                ),
                                            placeCard = card,
                                            onCardClick = {},
                                        )
                                    }
                                }
                            }
                        }
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
    test: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { effect ->
            when (effect) {
                SideEffect.NavigateToRegister -> navigateToRegister()
                SideEffect.LaunchImageCropperForSelectingProfilePicture -> {
                    imageCropperLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                is SideEffect.ReloadProfilePicture -> {}
                SideEffect.Test -> test()
            }
        }.collect()
    }
}