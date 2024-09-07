@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.validator.navigation

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
import org.codingforanimals.veganuniverse.validator.R

@Composable
internal fun ValidatorTopAppBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MediumTopAppBar(
        modifier = modifier,
        title = {
            Text(stringResource(R.string.validator_top_app_bar_title))
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    imageVector = VUIcons.Close.imageVector,
                    contentDescription = stringResource(back)
                )
            }
        }
    )
}
