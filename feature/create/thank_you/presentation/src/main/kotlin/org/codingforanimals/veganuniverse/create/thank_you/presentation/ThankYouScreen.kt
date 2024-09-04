package org.codingforanimals.veganuniverse.create.thank_you.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.create.domain.model.CreateFeature
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons


@Composable
fun ThankYouScreen(
    contributionType: CreateFeature?,
    navigateToCreateScreen: () -> Unit,
    navigateToProfileScreen: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing_06),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        contributionType?.let {
            Icon(
                modifier = Modifier.size(86.dp),
                painter = painterResource(it.icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Text(
            modifier = Modifier.padding(top = Spacing_06),
            text = stringResource(R.string.thanks_for_your_contribution),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            modifier = Modifier.padding(top = Spacing_06),
            text = stringResource(R.string.thank_you_subtitle),
            textAlign = TextAlign.Center,
        )
        contributionType?.let {
            Text(
                modifier = Modifier.padding(top = Spacing_06),
                text = it.contentAvailableText,
                textAlign = TextAlign.Center,
            )
        }
        Button(
            modifier = Modifier.padding(top = Spacing_06),
            onClick = navigateToCreateScreen,
        ) {
            Text(text = stringResource(R.string.thank_you_continue))
        }

        TextButton(
            modifier = Modifier.padding(top = Spacing_02),
            onClick = navigateToProfileScreen,
        ) {
            Text(text = stringResource(R.string.go_to_profile))
        }
    }
}

private val CreateFeature.contentAvailableText: String
    @Composable
    get() = stringResource(
        when (this) {
            CreateFeature.PRODUCT -> R.string.product_content_available_text
            CreateFeature.PLACE -> R.string.place_content_available_text
            CreateFeature.RECIPE -> R.string.recipe_content_available_text
        }
    )

private val CreateFeature.icon: Int
    @Composable get() = remember {
        when (this) {
            CreateFeature.PRODUCT -> VUIcons.VeganLogo.id
            CreateFeature.PLACE -> VUIcons.Location.id
            CreateFeature.RECIPE -> VUIcons.Recipes.id
        }
    }

@Preview
@Composable
private fun PreviewPlaceThankYouScreen() {
    VeganUniverseTheme {
        VeganUniverseBackground {
            ThankYouScreen(
                contributionType = CreateFeature.PLACE,
                navigateToCreateScreen = {},
                navigateToProfileScreen = {}
            )
        }
    }
}

@Preview
@Composable
private fun PreviewRecipeThankYouScreen() {
    VeganUniverseTheme {
        VeganUniverseBackground {
            ThankYouScreen(
                contributionType = CreateFeature.RECIPE,
                navigateToCreateScreen = {},
                navigateToProfileScreen = {}
            )
        }
    }
}

@Preview
@Composable
private fun PreviewProductThankYouScreen() {
    VeganUniverseTheme {
        VeganUniverseBackground {
            ThankYouScreen(
                contributionType = CreateFeature.PRODUCT,
                navigateToCreateScreen = {},
                navigateToProfileScreen = {}
            )
        }
    }
}

@Preview
@Composable
private fun PreviewUnknownThankYouScreen() {
    VeganUniverseTheme {
        VeganUniverseBackground {
            ThankYouScreen(
                contributionType = null,
                navigateToCreateScreen = {},
                navigateToProfileScreen = {}
            )
        }
    }
}