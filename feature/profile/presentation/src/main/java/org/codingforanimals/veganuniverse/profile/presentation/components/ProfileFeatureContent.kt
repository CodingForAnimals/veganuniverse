package org.codingforanimals.veganuniverse.profile.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.codingforanimals.veganuniverse.core.ui.R.string.show_more
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.error.ErrorView
import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.places.ui.compose.PlaceCard
import org.codingforanimals.veganuniverse.places.ui.entity.PlaceCard
import org.codingforanimals.veganuniverse.profile.presentation.R
import org.codingforanimals.veganuniverse.profile.presentation.model.ProfileFeatureContentState
import org.codingforanimals.veganuniverse.shared.ui.cards.LoadingSimpleCard
import org.codingforanimals.veganuniverse.shared.ui.cards.SimpleCard
import org.codingforanimals.veganuniverse.shared.ui.cards.SimpleCardItem

@Composable
internal fun <T : Any> ProfileFeatureContent(
    state: ProfileFeatureContentState<T>,
    subtitleLabel: Int,
    subtitleIcon: Icon,
    onShowMoreClick: () -> Unit,
    errorLabel: Int,
    emptyStateLabel: Int,
    emptyStateIcon: Icon,
    onItemClick: (String) -> Unit,
) {

    ContentSubtitle(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing_04, horizontal = Spacing_06),
        label = subtitleLabel,
        buttonLabel = show_more,
        onButtonClick = onShowMoreClick,
        leadingIcon = subtitleIcon,
    )


    Crossfade(
        modifier = Modifier.fillMaxWidth(),
        targetState = state,
        label = "profile_screen_content_crossfade",
    ) { currentState ->
        when (currentState) {
            ProfileFeatureContentState.Error -> {
                ErrorView(message = errorLabel)
            }

            ProfileFeatureContentState.Loading -> {
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

            is ProfileFeatureContentState.Success -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(Spacing_04),
                ) {
                    if (currentState.items.isEmpty()) {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing_06),
                            text = stringResource(emptyStateLabel)
                        )
                        VUIcon(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            icon = emptyStateIcon,
                            contentDescription = stringResource(R.string.your_places),
                        )
//                        Image(
//                            modifier = Modifier
//                                .size(24.dp)
//                                .align(Alignment.CenterHorizontally),
//                            painter = painterResource(emptyStateIcon),
//                            contentDescription = stringResource(R.string.your_places)
//                        )
                    } else {
                        currentState.items.forEachIndexed { index, item ->
                            key(index) {
                                when (item) {
                                    is PlaceCard -> {
                                        PlaceCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    bottom = Spacing_06,
                                                    start = Spacing_06,
                                                    end = Spacing_06
                                                ),
                                            placeCard = item,
                                            onCardClick = { onItemClick(item.geoHash) },
                                        )
                                    }

                                    is SimpleCardItem -> {
                                        SimpleCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    bottom = Spacing_06,
                                                    start = Spacing_06,
                                                    end = Spacing_06
                                                )
                                                .aspectRatio(2f),
                                            model = item,
                                            onClick = { onItemClick(item.id) },
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
}
