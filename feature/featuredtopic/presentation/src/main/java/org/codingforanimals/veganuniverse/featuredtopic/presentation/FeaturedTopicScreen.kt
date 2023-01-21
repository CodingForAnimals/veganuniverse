package org.codingforanimals.veganuniverse.featuredtopic.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
internal fun FeaturedTopicScreen(
    argument: String?,
) {
    Text(text = argument ?: "Error")
}