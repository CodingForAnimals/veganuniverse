package org.codingforanimals.veganuniverse.commons.ui.snackbar

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

data class Snackbar(
    val message: Int,
    val actionLabel: Int? = null,
    val action: (suspend () -> Unit)? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short,
)

@Composable
fun HandleSnackbarEffects(
    snackbarEffects: Flow<Snackbar>,
    snackbarHostState: SnackbarHostState,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        snackbarEffects.onEach {
            snackbarHostState.showSnackbar(it.toVisuals(context))
        }.collect()
    }
}

private fun Snackbar.toVisuals(context: Context): SnackbarVisuals {
    return object: SnackbarVisuals {
        override val actionLabel: String?
            get() = this@toVisuals.actionLabel?.let { context.getString(it) }
        override val duration: SnackbarDuration
            get() = this@toVisuals.duration
        override val message: String
            get() = context.getString(this@toVisuals.message)
        override val withDismissAction: Boolean
            get() = false
    }
}