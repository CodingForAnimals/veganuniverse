package org.codingforanimals.veganuniverse.profile.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.codingforanimals.veganuniverse.profile.R
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    navigateToAuthenticationPrompt: () -> Unit,
) {
    val viewModel: ProfileScreenViewModel = koinViewModel()
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()

    ProfileScreen(
        modifier = modifier,
        profileState = profileState,
        navigateToAuthenticationPrompt = navigateToAuthenticationPrompt,
        logout = viewModel::logout,
    )
}

@Composable
private fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileState: ProfileScreenViewModel.ProfileState,
    navigateToAuthenticationPrompt: () -> Unit,
    logout: () -> Unit,
) {
    Crossfade(
        modifier = modifier,
        targetState = profileState,
        label = "profile_screen_cross_fade",
    ) { state ->
        when (state) {
            ProfileScreenViewModel.ProfileState.AuthenticatePrompt -> {
                AuthenticatePromptScreen(
                    onLoginClick = navigateToAuthenticationPrompt,
                )
            }

            ProfileScreenViewModel.ProfileState.ProfileContent -> {
                ProfileContentScreen(
                    logout = logout,
                )
            }
        }
    }
}

@Composable
private fun AuthenticatePromptScreen(
    onLoginClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing_04, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            painter = painterResource(id = VUIcons.Profile.id),
            contentDescription = null,
        )
        Text(text = stringResource(R.string.profie_screen_unauthenticated_user_title), style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = stringResource(R.string.profie_screen_unauthenticated_user_subtitle), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.profie_screen_unauthenticated_user_text),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = onLoginClick) {
            Text(text = stringResource(R.string.access))
        }
    }
}

@Composable
private fun ProfileContentScreen(
    logout: () -> Unit,
) {
    Column {
        Button(onClick = logout) {
            Text(text = "Log out")
        }
    }
}

