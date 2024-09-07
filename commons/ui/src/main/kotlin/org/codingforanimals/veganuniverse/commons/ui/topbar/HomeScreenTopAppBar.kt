@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.commons.ui.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

@Composable
fun HomeScreenTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null
) {
    MediumTopAppBar(
        modifier = modifier,
        title = {
            Text(title)
        },
        navigationIcon = {
            onBackClick?.let {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = VUIcons.ArrowBack.imageVector,
                        contentDescription = stringResource(back)
                    )
                }
            }
        }
    )
}