@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.additives.presentation.detail.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.additives.domain.model.Additive
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveType
import org.codingforanimals.veganuniverse.additives.domain.usecase.AdditivesUseCases
import org.codingforanimals.veganuniverse.additives.presentation.AdditivesDestination
import org.codingforanimals.veganuniverse.additives.presentation.R
import org.codingforanimals.veganuniverse.additives.presentation.detail.edit.AdditiveDetailEditViewModel.Edit
import org.codingforanimals.veganuniverse.additives.presentation.model.AdditiveTypeUI
import org.codingforanimals.veganuniverse.additives.presentation.model.toUI
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.R.string.edit_error
import org.codingforanimals.veganuniverse.commons.ui.R.string.edit_screen_top_bar_title
import org.codingforanimals.veganuniverse.commons.ui.R.string.edit_success
import org.codingforanimals.veganuniverse.commons.ui.R.string.send
import org.codingforanimals.veganuniverse.commons.ui.components.VURadioButton
import org.codingforanimals.veganuniverse.commons.ui.dialog.ErrorDialog
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.snackbar.LocalSnackbarSender
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.koin.androidx.compose.koinViewModel

internal class AdditiveDetailEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val additivesUseCases: AdditivesUseCases,
) : ViewModel() {

    private val id = savedStateHandle.get<String>(AdditivesDestination.Edit.ARG_ID)

    var code by mutableStateOf("")
        private set

    var name by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var type by mutableStateOf(AdditiveType.VEGAN)
        private set

    var showErrorDialog by mutableStateOf(false)
        private set

    private val resultChannel = Channel<EditResult>()
    val result = resultChannel.receiveAsFlow()

    init {
        runCatching {
            checkNotNull(id) {
                "Additive id is required".also {
                    Analytics.logNonFatalException(IllegalStateException(it))
                }
            }
            viewModelScope.launch {
                val additive = additivesUseCases.getByIdFromLocal(id)
                code = additive.code
                name = additive.name.orEmpty()
                description = additive.description.orEmpty()
                type = additive.type
            }

        }.onFailure {
            showErrorDialog = true
        }
    }

    fun onEdit(edit: Edit) {
        when (edit) {
            is Edit.Code -> code = edit.value
            is Edit.Description -> description = edit.value
            is Edit.Name -> name = edit.value
            is Edit.Type -> type = edit.value
        }
    }

    fun onSend() {
        id ?: return
        viewModelScope.launch {
            val res = additivesUseCases.sendEdit(
                Additive(
                    id = id,
                    code = code,
                    name = name,
                    description = description,
                    type = type
                )
            )
            if (res.isSuccess) {
                resultChannel.send(EditResult.Success(Snackbar(edit_success)))
            } else {
                resultChannel.send(EditResult.Error(Snackbar(edit_error)))
            }
        }
    }

    sealed class Edit {
        data class Code(val value: String) : Edit()
        data class Name(val value: String) : Edit()
        data class Description(val value: String) : Edit()
        data class Type(val value: AdditiveType) : Edit()
    }

    sealed class EditResult {
        data class Success(val snackbar: Snackbar) : EditResult()
        data class Error(val snackbar: Snackbar) : EditResult()
    }
}

@Composable
internal fun AdditiveDetailEditScreen(
    navigateUp: () -> Unit,
) {
    val viewModel: AdditiveDetailEditViewModel = koinViewModel()

    AdditiveDetailEditScreen(
        modifier = Modifier.fillMaxSize(),
        code = viewModel.code,
        name = viewModel.name,
        description = viewModel.description,
        selectedType = viewModel.type,
        onEdit = viewModel::onEdit,
        onSend = viewModel::onSend,
        navigateUp = navigateUp,
    )

    if (viewModel.showErrorDialog) {
        ErrorDialog(navigateUp)
    }

    val snackbarSender = LocalSnackbarSender.current
    LaunchedEffect(Unit) {
        viewModel.result.onEach { result ->
            when (result) {
                is AdditiveDetailEditViewModel.EditResult.Error -> {
                    snackbarSender(result.snackbar)
                }

                is AdditiveDetailEditViewModel.EditResult.Success -> {
                    snackbarSender(result.snackbar)
                    navigateUp()
                }
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
    onEdit: (Edit) -> Unit = {},
    onSend: () -> Unit = {},
    navigateUp: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(edit_screen_top_bar_title))
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
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = code,
                onValueChange = { onEdit(Edit.Code(it)) },
                textStyle = MaterialTheme.typography.headlineLarge,
                label = {
                    Text(stringResource(R.string.additive_code))
                }
            )
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
            code = additive.code,
            name = additive.name.orEmpty(),
            description = additive.description.orEmpty(),
            selectedType = additive.type,
        )
    }
}
