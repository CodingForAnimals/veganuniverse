package org.codingforanimals.veganuniverse.featuredtopic.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.codingforanimals.veganuniverse.core.ui.community.Post
import org.codingforanimals.veganuniverse.core.ui.components.VeganUniverseTopAppBar
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.featuredtopic.model.R
import org.codingforanimals.veganuniverse.featuredtopic.model.test_post_list

@Composable
internal fun FeaturedTopicScreen(
    onBackClick: () -> Unit,
) {
    val posts = test_post_list
    Column {
        VeganUniverseTopAppBar(title = "ABC Vegano", onBackClick = onBackClick)
        LazyColumn(
            modifier = Modifier.padding(horizontal = Spacing_04),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing_06)

        ) {
            item {
                Image(
                    modifier = Modifier.padding(vertical = Spacing_06),
                    painter = painterResource(R.drawable.featured_topic_abc_vegan_test),
                    contentDescription = "ABC Vegano imágen",
                )
            }
            items(
                items = posts,
                itemContent = { post ->
                    Post(
                        title = post.title,
                        subtitle = post.subtitle,
                        description = post.description,
                    )
                }
            )
        }
    }
}