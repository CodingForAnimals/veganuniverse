@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.core.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable

object VUTextFieldDefaults {

    @Composable
    fun colors() = TextFieldDefaults.outlinedTextFieldColors(
        unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}