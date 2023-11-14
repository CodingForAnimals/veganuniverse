@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.create.presentation.place.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import org.codingforanimals.veganuniverse.core.common.R.string.back
import org.codingforanimals.veganuniverse.core.common.R.string.closed
import org.codingforanimals.veganuniverse.core.common.R.string.edit
import org.codingforanimals.veganuniverse.create.presentation.R
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.Action
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.OpeningHoursTimePickerState
import org.codingforanimals.veganuniverse.create.presentation.place.model.OpeningHoursField
import org.codingforanimals.veganuniverse.create.presentation.place.model.PeriodEnd
import org.codingforanimals.veganuniverse.create.presentation.place.model.PeriodType
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

@Composable
internal fun OpeningHours(
    openingHoursField: OpeningHoursField,
    openingHoursTimePickerState: OpeningHoursTimePickerState?,
    onAction: (Action) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing_06),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing_04)
        ) {
            Text(
                text = stringResource(R.string.place_opening_hours_field_placeholder),
                fontWeight = FontWeight.Medium,
            )
            TextButton(
                onClick = { onAction(Action.OnOpeningHoursEditButtonClick) },
                content = { Text(text = stringResource(edit)) },
            )
            Spacer(modifier = Modifier.weight(1f))
            VUIcon(
                icon = openingHoursField.expandIcon,
                contentDescription = "",
                onIconClick = { onAction(Action.OnHideExpandOpeningHoursClick) })
        }
        AnimatedVisibility(
            visible = openingHoursField.isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing_04)
            ) {
                openingHoursField.sortedOpeningHours.forEach { openingHour ->
                    key(openingHour.dayOfWeek.name) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                text = stringResource(openingHour.dayStringRes),
                            )
                            Crossfade(
                                modifier = Modifier.weight(1f),
                                targetState = openingHour.isClosed, label = ""
                            ) { isClosed ->
                                if (isClosed) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        text = stringResource(id = closed)
                                    )
                                } else {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        Text(
                                            textAlign = TextAlign.Center,
                                            text = openingHour.mainPeriod.displayPeriod,
                                        )
                                        if (openingHour.isSplit) {
                                            Text(
                                                textAlign = TextAlign.Center,
                                                text = openingHour.secondaryPeriod.displayPeriod,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (openingHoursField.isEditing) {
        EditOpeningHoursDialog(
            openingHoursField = openingHoursField,
            onAction = onAction,
        )

        openingHoursTimePickerState?.let {
            Dialog(onDismissRequest = { onAction(Action.OnTimePickerDismissed) }) {
                Card {
                    TimePicker(
                        modifier = Modifier.padding(Spacing_06),
                        state = it.state,
                        colors = TimePickerDefaults.colors(clockDialColor = MaterialTheme.colorScheme.secondaryContainer)
                    )
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .padding(end = Spacing_04),
                        onClick = { onAction(Action.OnTimePickerDismissed) }) {
                        Text(text = stringResource(back))
                    }
                }
            }
        }
    }
}

@Composable
private fun EditOpeningHoursDialog(
    openingHoursField: OpeningHoursField,
    onAction: (Action) -> Unit,
) {
    Dialog(
        onDismissRequest = { onAction(Action.OnOpeningHoursDismissEditDialog) },
        content = {
            Card(modifier = Modifier.padding(vertical = Spacing_06)) {
                Column {
                    Text(
                        modifier = Modifier.padding(
                            top = Spacing_04,
                            start = Spacing_04,
                        ),
                        text = stringResource(R.string.place_opening_hours_field_placeholder),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(Spacing_04),
                        verticalArrangement = Arrangement.spacedBy(Spacing_04),
                        content = {
                            items(openingHoursField.sortedOpeningHours) { period ->
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            textAlign = TextAlign.Center,
                                            text = stringResource(period.dayStringRes),
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Switch(
                                            checked = !period.isClosed,
                                            onCheckedChange = {
                                                onAction(Action.OnDayOpenCloseSwitchClick(period.dayOfWeek))
                                            },
                                        )
                                    }
                                    Crossfade(
                                        modifier = Modifier.animateContentSize(),
                                        targetState = period.isClosed, label = "",
                                    ) { isClosed ->
                                        if (isClosed) {
                                            Text(
                                                modifier = Modifier.padding(start = Spacing_04),
                                                text = stringResource(closed)
                                            )
                                        } else {
                                            Column {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(
                                                        Spacing_04
                                                    ),
                                                ) {
                                                    OutlinedButton(
                                                        onClick = {
                                                            onAction(
                                                                Action.EditPeriodButtonClick(
                                                                    day = period.dayOfWeek,
                                                                    periodEnd = PeriodEnd.FROM,
                                                                    periodType = PeriodType.MAIN,
                                                                )
                                                            )
                                                        },
                                                        content = { Text(text = period.mainPeriod.fromDisplayPeriod) },
                                                    )
                                                    Text(text = stringResource(R.string.place_opening_hours_open_close_connector))
                                                    OutlinedButton(onClick = {
                                                        onAction(
                                                            Action.EditPeriodButtonClick(
                                                                day = period.dayOfWeek,
                                                                periodEnd = PeriodEnd.TO,
                                                                periodType = PeriodType.MAIN,
                                                            )
                                                        )
                                                    }) {
                                                        Text(text = period.mainPeriod.toDisplayPeriod)
                                                    }
                                                    if (!period.isSplit) {
                                                        Spacer(modifier = Modifier.weight(1f))
                                                        VUIcon(
                                                            icon = VUIcons.Add,
                                                            contentDescription = "",
                                                            onIconClick = {
                                                                onAction(
                                                                    Action.OnChangeSplitPeriodClick(
                                                                        period.dayOfWeek
                                                                    )
                                                                )
                                                            }
                                                        )
                                                    }
                                                }
                                                if (period.isSplit) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(
                                                            Spacing_04
                                                        ),
                                                    ) {
                                                        OutlinedButton(onClick = {
                                                            onAction(
                                                                Action.EditPeriodButtonClick(
                                                                    day = period.dayOfWeek,
                                                                    periodEnd = PeriodEnd.FROM,
                                                                    periodType = PeriodType.SECONDARY,
                                                                )
                                                            )
                                                        }) {
                                                            Text(text = period.secondaryPeriod.fromDisplayPeriod)
                                                        }
                                                        Text(text = stringResource(R.string.place_opening_hours_open_close_connector))
                                                        OutlinedButton(onClick = {
                                                            onAction(
                                                                Action.EditPeriodButtonClick(
                                                                    day = period.dayOfWeek,
                                                                    periodEnd = PeriodEnd.TO,
                                                                    periodType = PeriodType.SECONDARY,
                                                                )
                                                            )
                                                        }) {
                                                            Text(text = period.secondaryPeriod.toDisplayPeriod)
                                                        }
                                                        Spacer(modifier = Modifier.weight(1f))
                                                        TextButton(
                                                            onClick = {
                                                                onAction(
                                                                    Action.OnChangeSplitPeriodClick(
                                                                        period.dayOfWeek
                                                                    )
                                                                )
                                                            },
                                                            content = { Text(text = " - ") },
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        },
                    )
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .padding(bottom = Spacing_04, end = Spacing_04),
                        onClick = { onAction(Action.OnOpeningHoursDismissEditDialog) },
                        content = { Text(text = stringResource(back)) },
                    )
                }
            }
        },
    )
}