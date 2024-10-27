package org.codingforanimals.veganuniverse.additives.presentation.detail.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.additives.domain.model.Additive
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveType
import org.codingforanimals.veganuniverse.additives.domain.usecase.AdditivesUseCases
import org.codingforanimals.veganuniverse.additives.presentation.AdditivesDestination
import org.codingforanimals.veganuniverse.additives.presentation.R.string.code_required_error
import org.codingforanimals.veganuniverse.additives.presentation.R.string.create_error
import org.codingforanimals.veganuniverse.additives.presentation.R.string.name_required_error
import org.codingforanimals.veganuniverse.commons.ui.R
import org.codingforanimals.veganuniverse.commons.ui.R.string.edit_screen_top_bar_title
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar

internal class AdditiveDetailEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val additivesUseCases: AdditivesUseCases,
) : ViewModel() {

    private val id = savedStateHandle.get<String>(AdditivesDestination.Edit.ARG_ID)
    private val entryPoint = id?.let { EntryPoint.Edit(it) } ?: EntryPoint.Create

    val topBarTitleRes = when (entryPoint) {
        EntryPoint.Create -> R.string.create_additive_top_bar_title
        is EntryPoint.Edit -> edit_screen_top_bar_title
    }

    var code by mutableStateOf("")
        private set

    var codeRequired by mutableStateOf(true)
        private set

    var name by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var type by mutableStateOf(AdditiveType.VEGAN)
        private set

    var errorDialogMessage by mutableStateOf<Int?>(null)
        private set


    private val resultChannel = Channel<Result>()
    val result = resultChannel.receiveAsFlow()

    init {
        runCatching {
            when (entryPoint) {
                EntryPoint.Create -> Unit
                is EntryPoint.Edit -> {
                    viewModelScope.launch {
                        val additive = additivesUseCases.getByIdFromLocal(entryPoint.id)
                        code = additive.code.orEmpty()
                        name = additive.name.orEmpty()
                        description = additive.description.orEmpty()
                        type = additive.type
                    }

                }
            }
        }.onFailure {
            val errorResult = when (entryPoint) {
                EntryPoint.Create -> Result.CreateError(Snackbar(create_error))
                is EntryPoint.Edit -> Result.EditError(Snackbar(R.string.unexpected_error_message))
            }
            resultChannel.trySend(errorResult)
        }
    }

    fun dismissErrorDialog() {
        errorDialogMessage = null
    }

    fun onCodeRequiredChange(value: Boolean) {
        codeRequired = value
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
        if (codeRequired && code.isBlank()) {
            errorDialogMessage = code_required_error
            return
        }
        if (!codeRequired && name.isBlank()) {
            errorDialogMessage = name_required_error
            return
        }

        when (entryPoint) {
            EntryPoint.Create -> {
                viewModelScope.launch {
                    val res = additivesUseCases.uploadAdditive(
                        Additive(
                            id = null,
                            code = code.takeIf { codeRequired },
                            name = name,
                            description = description,
                            type = type
                        )
                    )
                    if (res.isSuccess) {
                        resultChannel.send(Result.CreateSuccess)
                    } else {
                        resultChannel.send(Result.CreateError(Snackbar(create_error)))
                    }
                }
            }

            is EntryPoint.Edit -> {
                viewModelScope.launch {
                    val res = additivesUseCases.sendEdit(
                        Additive(
                            id = entryPoint.id,
                            code = code,
                            name = name,
                            description = description,
                            type = type
                        )
                    )
                    if (res.isSuccess) {
                        resultChannel.send(Result.EditSuccess(Snackbar(R.string.edit_success)))
                    } else {
                        resultChannel.send(Result.EditError(Snackbar(R.string.edit_error)))
                    }
                }
            }
        }
    }

    sealed class Edit {
        data class Code(val value: String) : Edit()
        data class Name(val value: String) : Edit()
        data class Description(val value: String) : Edit()
        data class Type(val value: AdditiveType) : Edit()
    }

    sealed class Result {
        data class EditSuccess(val snackbar: Snackbar) : Result()
        data class EditError(val snackbar: Snackbar) : Result()
        data class CreateError(val snackbar: Snackbar) : Result()
        data object CreateSuccess : Result()
    }

    sealed class EntryPoint {
        data class Edit(val id: String) : EntryPoint()
        data object Create : EntryPoint()
    }
}
