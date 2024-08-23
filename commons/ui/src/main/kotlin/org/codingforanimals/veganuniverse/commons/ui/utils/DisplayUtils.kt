package org.codingforanimals.veganuniverse.commons.ui.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Number.toPx(context: Context): Int =
    (this.toInt() * context.resources.displayMetrics.density).toInt()

@Composable
fun Number.toPx(): Dp {
    val context = LocalContext.current
    return (this.toInt() * context.resources.displayMetrics.density).dp
}

fun Number.toDp(context: Context): Int =
    (this.toInt() / context.resources.displayMetrics.density).toInt()

@Composable
fun Number.toDp(): Dp {
    val context = LocalContext.current
    return (this.toInt() / context.resources.displayMetrics.density).dp
}