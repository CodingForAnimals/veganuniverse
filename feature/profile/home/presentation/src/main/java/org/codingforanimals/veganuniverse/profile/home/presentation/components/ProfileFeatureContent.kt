package org.codingforanimals.veganuniverse.profile.home.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import org.codingforanimals.veganuniverse.core.ui.R.string.show_more
import org.codingforanimals.veganuniverse.places.ui.PlaceCard
import org.codingforanimals.veganuniverse.places.ui.PlaceCardItem
import org.codingforanimals.veganuniverse.profile.home.presentation.model.ProfileFeatureContentState
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.cards.LoadingSimpleCard
import org.codingforanimals.veganuniverse.ui.cards.SimpleCard
import org.codingforanimals.veganuniverse.ui.cards.SimpleCardItem
import org.codingforanimals.veganuniverse.ui.error.ErrorView
import org.codingforanimals.veganuniverse.ui.icon.Icon

@Composable
internal fun <T : Any> ProfileFeatureContent(
    state: ProfileFeatureContentState<T>,
    subtitleLabel: Int,
    subtitleIcon: Icon,
    onShowMoreClick: () -> Unit,
    errorLabel: Int,
    onItemClick: (String) -> Unit,
) {
    Crossfade(
        modifier = Modifier.fillMaxWidth(),
        targetState = state,
        label = "profile_screen_content_crossfade",
    ) { currentState ->
        when (currentState) {
            ProfileFeatureContentState.Error -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing_06)
                ) {
                    ContentSubtitle(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Spacing_04, horizontal = Spacing_06),
                        label = subtitleLabel,
                        leadingIcon = subtitleIcon,
                    )
                    ErrorView(message = errorLabel)
                }
            }

            ProfileFeatureContentState.Loading -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing_06)
                ) {
                    ContentSubtitle(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Spacing_04, horizontal = Spacing_06),
                        label = subtitleLabel,
                        leadingIcon = subtitleIcon,
                    )
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
                    if (currentState.items.isNotEmpty()) {
                        ContentSubtitle(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Spacing_04, horizontal = Spacing_06),
                            label = subtitleLabel,
                            buttonLabel = show_more,
                            onButtonClick = onShowMoreClick,
                            leadingIcon = subtitleIcon,
                        )
                        currentState.items.forEachIndexed { index, item ->
                            key(index) {
                                when (item) {
                                    is PlaceCardItem -> {
                                        PlaceCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    bottom = Spacing_06,
                                                    start = Spacing_06,
                                                    end = Spacing_06
                                                ),
                                            placeCard = item,
                                            onCardClick = { onItemClick(item.id) },
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
