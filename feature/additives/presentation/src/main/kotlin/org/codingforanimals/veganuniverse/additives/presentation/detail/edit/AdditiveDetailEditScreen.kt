@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.additives.presentation.detail.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.additives.domain.model.Additive
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveType
import org.codingforanimals.veganuniverse.additives.presentation.R
import org.codingforanimals.veganuniverse.additives.presentation.detail.edit.AdditiveDetailEditViewModel.Edit
import org.codingforanimals.veganuniverse.additives.presentation.model.AdditiveTypeUI
import org.codingforanimals.veganuniverse.additives.presentation.model.toUI
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.R.string.edit_screen_top_bar_title
import org.codingforanimals.veganuniverse.commons.ui.R.string.send
import org.codingforanimals.veganuniverse.commons.ui.components.VURadioButton
import org.codingforanimals.veganuniverse.commons.ui.dialog.ErrorDialog
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.snackbar.LocalSnackbarSender
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun AdditiveDetailEditScreen(
    navigateUp: () -> Unit,
    navigateToThankYouScreen: () -> Unit,
) {
    val viewModel: AdditiveDetailEditViewModel = koinViewModel()

    AdditiveDetailEditScreen(
        modifier = Modifier.fillMaxSize(),
        code = viewModel.code,
        name = viewModel.name,
        description = viewModel.description,
        topBarTitle = viewModel.topBarTitleRes,
        selectedType = viewModel.type,
        onEdit = viewModel::onEdit,
        onSend = viewModel::onSend,
        codeRequired = viewModel.codeRequired,
        onCodeRequiredChange = viewModel::onCodeRequiredChange,
        navigateUp = navigateUp,
    )

    viewModel.errorDialogMessage?.let {
        ErrorDialog(
            onDismissRequest = viewModel::dismissErrorDialog,
            message = it
        )
    }

    val snackbarSender = LocalSnackbarSender.current
    LaunchedEffect(Unit) {
        viewModel.result.onEach { result ->
            when (result) {
                is AdditiveDetailEditViewModel.Result.EditError -> {
                    snackbarSender(result.snackbar)
                }

                is AdditiveDetailEditViewModel.Result.EditSuccess -> {
                    snackbarSender(result.snackbar)
                    navigateUp()
                }

                is AdditiveDetailEditViewModel.Result.CreateError -> {
                    snackbarSender(result.snackbar)
                }

                AdditiveDetailEditViewModel.Result.CreateSuccess -> navigateToThankYouScreen()
            }
        }.collect()
    }
}

@Composable
private fun AdditiveDetailEditScreen(
    code: String,
    name: String,
    description: String,
    selectedType: AdditiveType,
    modifier: Modifier = Modifier,
    codeRequired: Boolean = true,
    onCodeRequiredChange: (Boolean) -> Unit = {},
    topBarTitle: Int = edit_screen_top_bar_title,
    onEdit: (Edit) -> Unit = {},
    onSend: () -> Unit = {},
    navigateUp: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(topBarTitle))
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateUp
                    ) {
                        Icon(
                            imageVector = VUIcons.ArrowBack.imageVector,
                            contentDescription = stringResource(back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(Spacing_05)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing_06),
        ) {
            Column {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = code.takeIf { codeRequired }.orEmpty(),
                    onValueChange = { onEdit(Edit.Code(it)) },
                    textStyle = MaterialTheme.typography.headlineLarge,
                    label = {
                        Text(stringResource(R.string.additive_code))
                    },
                    enabled = codeRequired
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCodeRequiredChange(!codeRequired) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = codeRequired,
                        onCheckedChange = onCodeRequiredChange,
                    )
                    Text(
                        text = stringResource(R.string.code_required)
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing_04)
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = { onEdit(Edit.Name(it)) },
                    textStyle = MaterialTheme.typography.titleMedium,
                    label = {
                        Text(stringResource(R.string.additive_name))
                    },
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = description,
                    onValueChange = { onEdit(Edit.Description(it)) },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    label = {
                        Text(stringResource(R.string.additive_description))
                    },
                )
            }
            Column {
                AdditiveType.entries.forEach { type ->
                    val ui = remember { type.toUI() }
                    if (ui == AdditiveTypeUI.UNKNOWN) return@forEach
                    VURadioButton(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(ui.label),
                        selected = type == selectedType,
                        icon = ui.icon,
                        onClick = { onEdit(Edit.Type(type)) },
                    )
                }
            }
            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = onSend,
            ) {
                Text(text = stringResource(send))
            }
        }
    }
}


@Preview
@Composable
private fun PreviewAdditiveDetailEditScreen() {
    val additive = Additive.mock()
    VeganUniverseTheme {
        AdditiveDetailEditScreen(
            code = additive.code.orEmpty(),
            name = additive.name.orEmpty(),
            description = additive.description.orEmpty(),
            selectedType = additive.type,
        )
    }
}
