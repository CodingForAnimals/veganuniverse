@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.places.presentation.details.composables

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import org.codingforanimals.veganuniverse.places.presentation.R
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

@Composable
internal fun PlaceDetailsTopAppBar(
    onBackClick: () -> Unit,
    onReportPlaceClick: () -> Unit,
    onEditPlaceClick: () -> Unit,
) {
    VUTopAppBar(
        onBackClick = onBackClick,
        actions = {
            var showMenu by rememberSaveable { mutableStateOf(false) }
            VUIcon(
                icon = VUIcons.MoreOptions,
                contentDescription = "",
                onIconClick = { showMenu = !showMenu },
            )
            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.place_details_report_place_button_label)) },
                    onClick = onReportPlaceClick,
                    leadingIcon = {
                        VUIcon(icon = VUIcons.Report, contentDescription = "")
                    })
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.place_details_suggest_edit_button_label)) },
                    onClick = onEditPlaceClick,
                    leadingIcon = {
                        VUIcon(icon = VUIcons.Edit, contentDescription = "")
                    })
            }
        }
    )
}