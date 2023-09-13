package org.codingforanimals.veganuniverse.profile.presentation

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
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
import org.codingforanimals.veganuniverse.utils.rememberImageCropperLauncherForActivityResult
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ProfileScreen(
    navigateToRegister: () -> Unit,
    viewModel: ProfileScreenViewModel = koinViewModel(),
) {
    val imageCropperLauncher = rememberImageCropperLauncherForActivityResult(
        onCropSuccess = { profilePicUri ->
            if (profilePicUri != null) {
                viewModel.onAction(Action.NewProfilePictureSelected(profilePicUri))
            }
        }
    )

    HandleSideEffects(
        sideEffects = viewModel.sideEffect,
        navigateToRegister = navigateToRegister,
        imageCropperLauncher = imageCropperLauncher,
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
                UserContent(
                    state = uiState,
                    onAction = onAction
                )
            }
        }
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
private fun UserContent(
    state: UiState,
    onAction: (Action) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing_06),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        Crossfade(
            modifier = Modifier.fillMaxSize(),
            targetState = state.userContributions,
            label = "profile_screen_contributions_animation",
        ) { contributionsState ->
            when (contributionsState) {
                ProfileScreenViewModel.ContributionsState.Error -> ErrorView(message = R.string.contributions_error_message)
                ProfileScreenViewModel.ContributionsState.Loading -> VUCircularProgressIndicator()
                is ProfileScreenViewModel.ContributionsState.Success -> Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing_04)
                ) {
                    if (contributionsState.userHasNoContributions) {
                        ErrorView(message = R.string.contributions_empty_message)
                    } else {
                        if (contributionsState.places.isNotEmpty()) {
                            Text(
                                text = stringResource(R.string.contributions_places_title),
                                style = MaterialTheme.typography.titleMedium,
                            )
                            contributionsState.places.forEach { card ->
                                key(card.geoHash) {
                                    PlaceCard(placeCard = card, onCardClick = {})
                                }
                            }
                        }
                    }
                }

                null -> Unit
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
        NoActionDialog(
            title = errorDialog.errorTitle,
            message = errorDialog.errorMessage,
            buttonText = back,
            onDismissRequest = { onAction(Action.DismissErrorDialog) }
        )
    }
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateToRegister: () -> Unit,
    imageCropperLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { effect ->
            when (effect) {
                SideEffect.NavigateToRegister -> navigateToRegister()
                SideEffect.LaunchImageCropperForSelectingProfilePicture -> {
                    imageCropperLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                is SideEffect.ReloadProfilePicture -> {}
            }
        }.collect()
    }
}