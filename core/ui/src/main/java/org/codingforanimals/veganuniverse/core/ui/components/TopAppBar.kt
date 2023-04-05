@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons

@Composable
fun VUMediumTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    actions: @Composable RowScope.() -> Unit,
    onBackClick: (() -> Unit)?,
    colors: TopAppBarColors,
) {
    MediumTopAppBar(
        modifier = modifier,
        title = { Text(title) },
        navigationIcon = {
            onBackClick?.let {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = VUIcons.ArrowBack.imageVector,
                        contentDescription = "Atrás",
                    )
                }
            }
        },
        actions = actions,
        colors = colors,
    )
}

@Composable
fun VUTopAppBar(
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(),
    title: String = "",
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        colors = colors,
        navigationIcon = {
            onBackClick?.let {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = VUIcons.ArrowBack.imageVector,
                        contentDescription = "Atrás",
                    )
                }
            }
        },
        title = { Text(title) },
        actions = actions,
    )
}

@Composable
fun VUTopAppBar(
    title: @Composable () -> Unit,
    onBackClick: (() -> Unit)? = null,
) {
    TopAppBar(
        navigationIcon = {
            onBackClick?.let {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = VUIcons.ArrowBack.imageVector,
                        contentDescription = "Atrás",
                    )
                }
            }
        },
        title = title,
    )
}