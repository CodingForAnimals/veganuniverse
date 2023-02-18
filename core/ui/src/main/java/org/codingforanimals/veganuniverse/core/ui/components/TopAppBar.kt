@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.R

@Composable
fun VeganUniverseTopAppBar(
    modifier: Modifier = Modifier,
    visible: Boolean,
    @StringRes titleRes: Int,
    actions: @Composable RowScope.() -> Unit,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
) {
    AnimatedVisibility(visible = visible) {
        TopAppBar(
            modifier = modifier,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(R.drawable.logo), contentDescription = "logo"
                    )
                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = stringResource(titleRes)
                    )
                }
            },
            actions = actions,
            colors = colors,
        )
    }
}

data class TopBarAction(
    val imageVector: ImageVector,
    val onClick: () -> Unit,
)