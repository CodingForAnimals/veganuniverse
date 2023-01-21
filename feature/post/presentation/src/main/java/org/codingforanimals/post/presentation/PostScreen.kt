package org.codingforanimals.post.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PostScreen(postId: String?) {
    Text(text = "This is post $postId screen!!!")
}