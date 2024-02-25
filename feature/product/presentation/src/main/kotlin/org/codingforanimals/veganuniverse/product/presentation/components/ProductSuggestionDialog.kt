package org.codingforanimals.veganuniverse.product.presentation.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.product.domain.usecase.ProductSuggestionUseCases
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.components.ProductSuggestionDialogViewModel.Action
import org.codingforanimals.veganuniverse.product.presentation.components.ProductSuggestionDialogViewModel.SideEffect
import org.codingforanimals.veganuniverse.product.presentation.model.Product
import org.codingforanimals.veganuniverse.ui.R.string.back
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_05
import org.codingforanimals.veganuniverse.ui.viewmodel.StringField
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

sealed class ProductSuggestionType(open val product: Product) {
    data class Edit(override val product: Product) : ProductSuggestionType(product)
    data class Report(override val product: Product) : ProductSuggestionType(product)
}

@Composable
internal fun ProductSuggestionDialog(
    type: ProductSuggestionType,
    dismissDialog: (snackbarMessage: Int?, actionLabel: Int?, action: (suspend () -> Unit)?) -> Unit,
    navigateToAuthScreen: () -> Unit,
    viewModel: ProductSuggestionDialogViewModel = koinViewModel(
        parameters = { parametersOf(type) },
        key = "${type.product.id}-${type::class.simpleName}",
    ),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProductSuggestionDialog(
        uiState = uiState,
        onAction = viewModel::onAction,
        onDismissRequest = { dismissDialog(null, null, null) },
    )

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        dismissDialog = dismissDialog,
        navigateToAuthScreen = navigateToAuthScreen,
    )
}

@Composable
private fun ProductSuggestionDialog(
    uiState: ProductSuggestionDialogViewModel.UiState,
    onAction: (Action) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        content = {
            Card(
                shape = ShapeDefaults.Medium,
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(Spacing_04),
                    contentPadding = PaddingValues(Spacing_05)
                ) {
                    item {
                        Text(
                            text = stringResource(id = uiState.title),
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    item {
                        Text(
                            text = stringResource(
                                R.string.product_suggestion_dialog_productname,
                                uiState.productName
                            ),
                        )
                        Text(
                            text = stringResource(
                                R.string.product_suggestion_dialog_productbrand,
                                uiState.productBrand
                            ),
                        )
                    }
                    uiState.error?.let { error ->
                        item {
                            Text(
                                text = stringResource(error),
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                    item {
                        TextField(
                            value = uiState.text.value,
                            onValueChange = { onAction(Action.OnEditText(it)) },
                            placeholder = { Text(text = stringResource(id = uiState.textFieldPlaceholder)) },
                            isError = uiState.error != null || uiState.messageError != null,
                            supportingText = {
                                uiState.messageError?.let { messageError ->
                                    Text(text = stringResource(messageError))
                                }
                            }
                        )
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Button(onClick = { onAction(Action.Send) }) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(Spacing_04),
                                ) {
                                    Text(text = stringResource(id = R.string.send))
                                    AnimatedVisibility(visible = uiState.loading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            strokeWidth = 3.dp,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                        )
                                    }
                                }
                            }
                            TextButton(onClick = onDismissRequest) {
                                Text(text = stringResource(id = back))
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    dismissDialog: (snackbarMessage: Int?, actionLabel: Int?, action: (suspend () -> Unit)?) -> Unit,
    navigateToAuthScreen: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is SideEffect.DismissDialog -> dismissDialog(
                    sideEffect.snackbarMessage,
                    sideEffect.actionLabel,
                    sideEffect.action
                )

                SideEffect.NavigateToAuthScreen -> navigateToAuthScreen()
            }
        }.collect()
    }
}

internal class ProductSuggestionDialogViewModel(
    private val type: ProductSuggestionType,
    private val useCases: ProductSuggestionUseCases,
) : ViewModel() {

    private var sendJob: Job? = null

    private val _sideEffects: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = _sideEffects.receiveAsFlow()

    private val _uiState = MutableStateFlow(UiState.fromType(type))
    val uiState = _uiState.asStateFlow()

    fun onAction(action: Action) {
        when (action) {
            is Action.OnEditText -> {
                _uiState.value =
                    uiState.value.copy(text = StringField(action.text), messageError = null)
            }

            Action.Send -> {
                if (uiState.value.loading) return
                val message = uiState.value.text.value
                if (message.isEmpty()) {
                    _uiState.value =
                        uiState.value.copy(messageError = R.string.product_suggestion_dialog_error_message_empty)
                } else {
                    _uiState.value = uiState.value.copy(loading = true)
                    sendJob?.cancel()
                    sendJob = viewModelScope.launch {
                        val result = when (type) {
                            is ProductSuggestionType.Edit -> useCases.sendEdit(message)
                            is ProductSuggestionType.Report -> useCases.sendReport(message)
                        }
                        when (result) {
                            ProductSuggestionUseCases.Result.Error -> _uiState.value =
                                uiState.value.copy(
                                    error = R.string.product_suggestion_dialog_error_try_again,
                                    loading = false,
                                )

                            is ProductSuggestionUseCases.Result.Success -> {
                                _uiState.value = uiState.value.copy(loading = false)
                                _sideEffects.send(
                                    SideEffect.DismissDialog(
                                        snackbarMessage = R.string.thank_you_for_contributing,
                                    )
                                )
                            }

                            ProductSuggestionUseCases.Result.GuestUser -> {
                                _uiState.value = uiState.value.copy(loading = false)
                                _sideEffects.send(SideEffect.NavigateToAuthScreen)
                            }

                            ProductSuggestionUseCases.Result.UnverifiedEmail -> {
                                _uiState.value = uiState.value.copy(loading = false)
                                _sideEffects.send(
                                    SideEffect.DismissDialog(
                                        snackbarMessage = R.string.user_has_unverified_email,
                                        actionLabel = R.string.resend_email,
                                        action = useCases::sendVerificationEmail,
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    sealed class Action {
        data class OnEditText(val text: String) : Action()
        data object Send : Action()
    }

    sealed class SideEffect {
        data class DismissDialog(
            @StringRes val snackbarMessage: Int? = null,
            @StringRes val actionLabel: Int? = null,
            val action: (suspend () -> Unit)? = null,
        ) : SideEffect()

        data object NavigateToAuthScreen : SideEffect()
    }

    data class UiState(
        val productName: String,
        val productBrand: String,
        @StringRes val title: Int,
        @StringRes val message: Int,
        @StringRes val textFieldPlaceholder: Int,
        @StringRes val error: Int? = null,
        @StringRes val messageError: Int? = null,
        val text: StringField = StringField(),
        val loading: Boolean = false,
    ) {

        companion object {
            fun fromType(type: ProductSuggestionType): UiState {
                return UiState(
                    productName = type.product.name,
                    productBrand = type.product.brand,
                    title = when (type) {
                        is ProductSuggestionType.Edit -> R.string.product_suggestion_dialog_edit_title
                        is ProductSuggestionType.Report -> R.string.product_suggestion_dialog_report_title
                    },
                    message = when (type) {
                        is ProductSuggestionType.Edit -> R.string.product_suggestion_dialog_edit_message
                        is ProductSuggestionType.Report -> R.string.product_suggestion_dialog_report_message
                    },
                    textFieldPlaceholder = when (type) {
                        is ProductSuggestionType.Edit -> R.string.product_suggestion_dialog_edit_textField_placeholder
                        is ProductSuggestionType.Report -> R.string.product_suggestion_dialog_report_textField_placeholder
                    }
                )
            }
        }
    }
}