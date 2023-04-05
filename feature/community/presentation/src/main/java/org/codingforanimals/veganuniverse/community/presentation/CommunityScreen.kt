@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.community.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.codingforanimals.veganuniverse.community.presentation.component.FeaturedTopicCard
import org.codingforanimals.veganuniverse.core.ui.components.VUAssistChip
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.shared.Post
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_03
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.model.test_post_list
import org.koin.androidx.compose.koinViewModel
import org.codingforanimals.veganuniverse.common.R as commonR

@Composable
fun CommunityScreen(
    navigateToFeaturedTopic: (String) -> Unit,
    navigateToPost: (String) -> Unit,
    viewModel: CommunityScreenViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        CommunityScreenViewModel.UiState.Loading -> {
            LoadingScreen()
        }
        is CommunityScreenViewModel.UiState.Success -> {
            CommunityScreen(
                navigateToFeaturedTopic = navigateToFeaturedTopic,
                navigateToPost = navigateToPost,
            )
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.Center),
        )
    }
}

@Composable
private fun CommunityScreen(
    navigateToFeaturedTopic: (String) -> Unit,
    navigateToPost: (String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                Title("Consideramos importante que veas")
                FeaturedTopics(
                    featuredTopics,
                    navigateToFeaturedTopic,
                )
                Title("Lo que dice nuestra comunidad")
                Row(
                    modifier = Modifier.padding(start = Spacing_04),
                    horizontalArrangement = Arrangement.spacedBy(Spacing_04)
                ) {
                    VUAssistChip(
                        icon = VUIcons.Filter,
                        label = stringResource(commonR.string.filter),
                        onClick = {},
                        iconDescription = stringResource(commonR.string.filter)
                    )
                    VUAssistChip(
                        icon = VUIcons.Sort,
                        label = stringResource(commonR.string.sort),
                        onClick = {},
                        iconDescription = stringResource(commonR.string.sort),
                    )
                }
                CommunityFeed(
                    navigateToPost = navigateToPost
                )
            }
        }
    }
}

@Composable
private fun FeaturedTopics(
    topics: List<String>,
    navigateToFeaturedTopic: (String) -> Unit,
) {
    LazyRow(
        modifier = Modifier
            .wrapContentHeight()
            .padding(top = Spacing_03),
        contentPadding = PaddingValues(12.dp, 0.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(topics) { topic ->
            FeaturedTopicCard(
                imageId = commonR.drawable.featured_topic_abc_vegan_test,
                text = topic,
                onClick = navigateToFeaturedTopic
            )
        }
    }
}

@Composable
private fun CommunityFeed(
    navigateToPost: (String) -> Unit,
) {
    PostList(
        navigateToPost = navigateToPost,
    )
}

@Composable
private fun Title(
    text: String,
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = Spacing_06, start = Spacing_04),
    )
}

@Composable
private fun PostList(
    navigateToPost: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(
            start = Spacing_04,
            end = Spacing_04,
            bottom = Spacing_06,
            top = Spacing_04,
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        test_post_list.forEach {
            Post(
                title = it.title,
                subtitle = it.subtitle,
                description = it.description,
                onClick = { navigateToPost("$it") },
                image = it.image != null
            )
        }
    }
}
