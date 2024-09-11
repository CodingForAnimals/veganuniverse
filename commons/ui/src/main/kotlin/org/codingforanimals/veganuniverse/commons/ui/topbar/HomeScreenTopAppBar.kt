@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.commons.ui.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.designsystem.Primary
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.ui.R
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

@Composable
fun HomeScreenTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null
) {
    val userFontScale = LocalContext.current.resources.configuration.fontScale
    when (userFontScale) {
        in 0.0..1.0 -> {
            MediumTopAppBar(
                modifier = modifier,
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing_04)
                    ) {
                        if (onBackClick == null) {
                            Image(
                                modifier = Modifier.size(32.dp),
                                painter = painterResource(R.drawable.logo),
                                contentDescription = null,
                            )
                        }
                        Text(
                            text = title,
                            color = Primary,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
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
        else -> {
            TopAppBar(
                modifier = modifier,
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing_04)
                    ) {
                        if (onBackClick == null) {
                            Image(
                                modifier = Modifier.size(32.dp),
                                painter = painterResource(R.drawable.logo),
                                contentDescription = null,
                            )
                        }
                        Text(
                            text = title,
                            color = Primary,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
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
    }
}