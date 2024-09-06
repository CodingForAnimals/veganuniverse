package org.codingforanimals.veganuniverse.profile.presentation

import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.components.VuIcon
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.user.domain.model.User
import org.codingforanimals.veganuniverse.commons.user.domain.model.UserRole
import org.codingforanimals.veganuniverse.profile.R
import org.codingforanimals.veganuniverse.profile.presentation.ProfileScreenViewModel.Action
import org.codingforanimals.veganuniverse.profile.presentation.ProfileScreenViewModel.NavigationEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navigateToAuthenticationPrompt: () -> Unit,
    navigateToPlaceListing: (String) -> Unit,
    navigateToRecipeListing: (String) -> Unit,
    navigateToProductListing: (String) -> Unit,
) {
    val viewModel: ProfileScreenViewModel = koinViewModel()
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()

    ProfileScreen(
        modifier = modifier,
        profileState = profileState,
        navigateToAuthenticationPrompt = navigateToAuthenticationPrompt,
        onAction = viewModel::onAction,
    )

    LaunchedEffect(Unit) {
        viewModel.navigationEffects.onEach { effect ->
            when (effect) {
                is NavigationEffect.PlaceListing -> {
                    navigateToPlaceListing(effect.listingType.name)
                }

                is NavigationEffect.RecipeListing -> {
                    navigateToRecipeListing(effect.listingType.name)
                }

                is NavigationEffect.ProductListing -> {
                    navigateToProductListing(effect.listingType.name)
                }
            }
        }.collect()
    }
}

@Composable
private fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileState: ProfileScreenViewModel.ProfileState,
    navigateToAuthenticationPrompt: () -> Unit,
    onAction: (Action) -> Unit,
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

            is ProfileScreenViewModel.ProfileState.ProfileContent -> {
                ProfileContentScreen(
                    modifier = Modifier.fillMaxSize(),
                    user = state.user,
                    appVersion = appVersion(),
                    isVerified = state.isVerified,
                    onAction = onAction,
                )
            }

            ProfileScreenViewModel.ProfileState.Loading -> Unit
        }
    }
}

@Composable
private fun AuthenticatePromptScreen(
    onLoginClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Spacing_05),
        verticalArrangement = Arrangement.spacedBy(Spacing_04, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            painter = painterResource(id = VUIcons.Profile.id),
            contentDescription = null,
        )
        Text(
            text = stringResource(R.string.profie_screen_unauthenticated_user_title),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.profie_screen_unauthenticated_user_subtitle),
            style = MaterialTheme.typography.titleMedium
        )
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
    user: User,
    appVersion: String,
    isVerified: Boolean,
    modifier: Modifier = Modifier,
    onAction: (Action) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Spacing_05, vertical = Spacing_06),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing_03),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineMedium,
            )
            if (isVerified) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                        )
                ) {
                    Icon(
                        modifier = Modifier
                            .size(22.dp)
                            .align(Alignment.Center),
                        painter = painterResource(VUIcons.Check.id),
                        contentDescription = null,
                    )
                }
            }
        }
        Text(text = user.email, style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.padding(top = Spacing_06),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing_03),
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = VUIcons.Bookmark.id),
                contentDescription = null,
            )
            Text(
                text = stringResource(id = R.string.your_bookmarks),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        Row(
            modifier = Modifier.padding(top = Spacing_04),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing_05),
        ) {
            ProfileContentCard(
                modifier = modifier
                    .aspectRatio(1f)
                    .weight(1f),
                icon = VUIcons.VeganLogo,
                label = stringResource(id = R.string.products),
                onClick = { onAction(Action.BookmarksClick.Products) }
            )
            ProfileContentCard(
                modifier = modifier
                    .aspectRatio(1f)
                    .weight(1f),
                icon = VUIcons.Location,
                label = stringResource(id = R.string.places),
                onClick = { onAction(Action.BookmarksClick.Places) }
            )
            ProfileContentCard(
                modifier = modifier
                    .aspectRatio(1f)
                    .weight(1f),
                icon = VUIcons.Recipes,
                label = stringResource(id = R.string.recipes),
                onClick = { onAction(Action.BookmarksClick.Recipes) }
            )
        }

        Row(
            modifier = Modifier.padding(top = Spacing_06),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing_03),
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = VUIcons.Create.id),
                contentDescription = null,
            )
            Text(
                text = stringResource(id = R.string.your_contributions),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        Row(
            modifier = Modifier.padding(top = Spacing_04),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing_05),
        ) {
            ProfileContentCard(
                modifier = modifier
                    .aspectRatio(1f)
                    .weight(1f),
                icon = VUIcons.VeganLogo,
                label = stringResource(id = R.string.products),
                onClick = { onAction(Action.ContributionsClick.Products) }
            )
            ProfileContentCard(
                modifier = modifier
                    .aspectRatio(1f)
                    .weight(1f),
                icon = VUIcons.Location,
                label = stringResource(id = R.string.places),
                onClick = { onAction(Action.ContributionsClick.Places) }
            )
            ProfileContentCard(
                modifier = modifier
                    .aspectRatio(1f)
                    .weight(1f),
                icon = VUIcons.Recipes,
                label = stringResource(id = R.string.recipes),
                onClick = { onAction(Action.ContributionsClick.Recipes) }
            )
        }
        if (user.role == UserRole.VALIDATOR) {
            Row(
                modifier = Modifier.padding(top = Spacing_06),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing_03),
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = VUIcons.Profile.id),
                    contentDescription = null,
                )
                Text(
                    text = stringResource(id = R.string.your_role),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            ProfileContentCard(
                modifier = Modifier.padding(top = Spacing_04),
                icon = VUIcons.Check,
                label = stringResource(R.string.vegan_universe_validator),
                onClick = { onAction(Action.OnValidatorCardClick) }
            )
        }

        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = Spacing_06),
            onClick = { onAction(Action.OnLogoutClick) },
            content = {
                Text(text = stringResource(id = R.string.log_out))
            }
        )

        Text(
            modifier = Modifier
                .padding(top = Spacing_05)
                .align(Alignment.End),
            text = stringResource(R.string.version, appVersion),
            style = MaterialTheme.typography.labelSmall
        )

        Text(
            modifier = Modifier
                .align(Alignment.End),
            text = buildAnnotatedString {
                withStyle(MaterialTheme.typography.labelSmall.toSpanStyle()) {
                    append(stringResource(id = R.string.made_by))
                }
                withStyle(MaterialTheme.typography.labelSmall.toSpanStyle()) {
                    append(WHITESPACE)
                }
                withStyle(
                    MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ).toSpanStyle()
                ) {
                    append(stringResource(id = R.string.coding_for_animals))
                }
            },
        )
    }
}

@Composable
private fun ProfileContentCard(
    icon: Icon,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        content = {
            Column(
                modifier = Modifier
                    .padding(Spacing_04)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                VuIcon(
                    modifier = Modifier.size(24.dp),
                    icon = icon,
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier
                        .padding(top = Spacing_03)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    text = label,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    )
}

@Composable
private fun appVersion(): String {
    val context = LocalContext.current
    return remember {
        runCatching {
            context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_ACTIVITIES
            ).versionName
        }.getOrElse {
            Log.e("ProfileScreen", "Error getting app version", it)
            Analytics.logNonFatalException(it)
            "0.0.0"
        }
    }
}

private const val WHITESPACE = " "

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewProfileContentScreen() {
    VeganUniverseTheme {
        Scaffold(
            topBar = { MediumTopAppBar(title = {}) }
        ) {
            ProfileContentScreen(
                modifier = Modifier.padding(it),
                user = User(id = "123", "El Pepe Argento", "elpepe@gmail.com", UserRole.VALIDATOR),
                appVersion = "1.0.0",
                isVerified = true
            )
        }
    }
}
