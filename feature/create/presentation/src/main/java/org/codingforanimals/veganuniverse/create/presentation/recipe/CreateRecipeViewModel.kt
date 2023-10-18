package org.codingforanimals.veganuniverse.create.presentation.recipe

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.auth.model.SendVerificationEmailResult
import org.codingforanimals.veganuniverse.auth.usecase.SendVerificationEmailUseCase
import org.codingforanimals.veganuniverse.common.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.core.ui.viewmodel.PictureField
import org.codingforanimals.veganuniverse.core.ui.viewmodel.StringField
import org.codingforanimals.veganuniverse.core.ui.viewmodel.areFieldsValid
import org.codingforanimals.veganuniverse.create.presentation.R
import org.codingforanimals.veganuniverse.create.presentation.recipe.model.StringListField
import org.codingforanimals.veganuniverse.create.presentation.recipe.model.TagsField
import org.codingforanimals.veganuniverse.create.presentation.recipe.usecase.SubmitRecipeStatus
import org.codingforanimals.veganuniverse.create.presentation.recipe.usecase.SubmitRecipeUseCase
import org.codingforanimals.veganuniverse.user.R.string.verification_email_sent
import org.codingforanimals.veganuniverse.user.R.string.verification_email_too_many_requests

internal class CreateRecipeViewModel(
    private val submitRecipe: SubmitRecipeUseCase,
    private val sendVerificationEmail: SendVerificationEmailUseCase,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    private val ioDispatcher = coroutineDispatcherProvider.io()
    private var imagePickerJob: Job? = null
    private var submitRecipeJob: Job? = null
    private var sendVerificationEmailJob: Job? = null

    private val sideEffectChannel: Channel<SideEffect> = Channel()
    val sideEffect: Flow<SideEffect> = sideEffectChannel.receiveAsFlow()

    var uiState by mutableStateOf(UiState())
        private set

    fun onAction(action: Action) {
        when (action) {
            is Action.ImagePicker -> handleImagePickerAction(action)
            is Action.OnTextChange -> updateTextForm(action)
            is Action.OnIngredientChange -> updateIngredients(action)
            is Action.OnStepChange -> updateSteps(action)
            is Action.OnTagSelected -> updateTags(action)
            is Action.VerifyEmail -> handleVerifyEmailAction(action)
            Action.OnSubmitClick -> validateFieldsAndSubmit()
            Action.DialogDismissRequest -> dismissDialog()
        }
    }

    private fun handleImagePickerAction(action: Action.ImagePicker) {
        when (action) {
            Action.ImagePicker.Click -> {
                imagePickerJob?.cancel()
                imagePickerJob = viewModelScope.launch {
                    sideEffectChannel.send(SideEffect.OpenImageSelector)
                }
            }

            is Action.ImagePicker.Success -> {
                uiState = uiState.copy(pictureField = PictureField(model = action.uri))
            }
        }
    }

    private fun updateTextForm(action: Action.OnTextChange) {
        uiState = when (action) {
            is Action.OnTextChange.Title -> uiState.copy(titleField = StringField(action.text))
            is Action.OnTextChange.Description -> uiState.copy(descriptionField = StringField(action.text))
            is Action.OnTextChange.Servings -> uiState.copy(servingsField = StringField(action.text))
            is Action.OnTextChange.PrepTime -> uiState.copy(prepTimeField = StringField(action.text))
        }
    }

    private fun updateIngredients(action: Action.OnIngredientChange) {
        uiState = when (action) {
            Action.OnIngredientChange.Add -> uiState.copy(
                ingredientsField = uiState.ingredientsField.addItem(),
            )

            is Action.OnIngredientChange.Text -> uiState.copy(
                ingredientsField = uiState.ingredientsField.changeItemAt(action.index, action.text),
            )

            is Action.OnIngredientChange.Delete -> uiState.copy(
                ingredientsField = uiState.ingredientsField.deleteItemAt(action.index),
            )
        }
    }

    private fun updateSteps(action: Action.OnStepChange) {
        uiState = when (action) {
            is Action.OnStepChange.AddAt -> uiState.copy(
                stepsField = uiState.stepsField.addItemAt(action.index + 1)
            )

            is Action.OnStepChange.Delete -> uiState.copy(
                stepsField = uiState.stepsField.deleteItemAt(action.index)
            )

            is Action.OnStepChange.Text -> uiState.copy(
                stepsField = uiState.stepsField.changeItemAt(action.index, action.text)
            )

            Action.OnStepChange.Add -> uiState.copy(
                stepsField = uiState.stepsField.addItem()
            )
        }
    }

    private fun updateTags(action: Action.OnTagSelected) {
        uiState = uiState.copy(tagsField = uiState.tagsField.toggleTag(action.name))
    }

    private fun validateFieldsAndSubmit() {
        if (uiState.areFieldsValid) {
            submitRecipeAttempt()
        } else {
            uiState = uiState.copy(isValidating = true)
        }
    }

    private fun submitRecipeAttempt() {
        submitRecipeJob?.cancel()
        submitRecipeJob = viewModelScope.launch {
            submitRecipe(uiState).collectLatest { status ->
                when (status) {
                    SubmitRecipeStatus.Loading -> {
                        uiState = uiState.copy(loading = true)
                    }

                    SubmitRecipeStatus.EmailNotVerified -> {
                        uiState = uiState.copy(
                            loading = false,
                            showVerifyEmailPrompt = true,
                        )
                    }

                    SubmitRecipeStatus.Error -> {
                        uiState = uiState.copy(
                            loading = false,
                            dialog = Dialog(
                                title = R.string.generic_error_title,
                                message = R.string.unknown_error_message,
                            )
                        )
                    }

                    SubmitRecipeStatus.UnauthorizedUser -> {
                        uiState = uiState.copy(loading = false)
                        sideEffectChannel.send(SideEffect.NavigateToAuthenticationScreen)
                    }

                    SubmitRecipeStatus.Success -> {
                        uiState = uiState.copy(loading = false)
                        sideEffectChannel.send(SideEffect.NavigateToThankYouScreen)
                    }
                }
            }
        }
    }

    private fun handleVerifyEmailAction(action: Action.VerifyEmail) {
        when (action) {
            Action.VerifyEmail.Dismiss -> uiState = uiState.copy(showVerifyEmailPrompt = false)
            Action.VerifyEmail.Send -> {
                sendVerificationEmailJob?.cancel()
                sendVerificationEmailJob = viewModelScope.launch {
                    uiState = uiState.copy(loading = true)
                    uiState = when (withContext(ioDispatcher) { sendVerificationEmail() }) {
                        SendVerificationEmailResult.Success -> uiState.copy(
                            showVerifyEmailPrompt = false,
                            loading = false,
                            dialog = Dialog(
                                title = R.string.success,
                                message = verification_email_sent,
                            )
                        )

                        SendVerificationEmailResult.TooManyRequests -> uiState.copy(
                            showVerifyEmailPrompt = false,
                            loading = false,
                            dialog = Dialog(
                                title = R.string.generic_error_title,
                                message = verification_email_too_many_requests
                            )
                        )

                        SendVerificationEmailResult.UnknownError -> uiState.copy(
                            showVerifyEmailPrompt = false,
                            loading = false,
                            dialog = Dialog(
                                title = R.string.generic_error_title,
                                message = R.string.unknown_error_message
                            )
                        )
                    }
                }
            }
        }
    }

    private fun dismissDialog() {
        uiState = uiState.copy(dialog = null)
    }

    data class UiState(
        val pictureField: PictureField = PictureField(),
        val titleField: StringField = StringField(),
        val descriptionField: StringField = StringField(),
        val servingsField: StringField = StringField(),
        val prepTimeField: StringField = StringField(),
        val ingredientsField: StringListField = StringListField(),
        val stepsField: StringListField = StringListField(),
        val tagsField: TagsField = TagsField(),
        val showVerifyEmailPrompt: Boolean = false,
        val isValidating: Boolean = false,
        val dialog: Dialog? = null,
        val loading: Boolean = false,
    ) {
        val areFieldsValid: Boolean
            get() = areFieldsValid(
                pictureField,
                titleField,
                descriptionField,
                servingsField,
                prepTimeField,
                ingredientsField,
                stepsField,
                tagsField,
            )
    }

    data class Dialog(
        @StringRes val title: Int,
        @StringRes val message: Int,
    )

    sealed class Action {
        sealed class ImagePicker : Action() {
            data object Click : ImagePicker()
            data class Success(val uri: Uri?) : ImagePicker()
        }

        sealed class OnTextChange : Action() {
            data class Title(val text: String) : OnTextChange()
            data class Description(val text: String) : OnTextChange()
            data class Servings(val text: String) : OnTextChange()
            data class PrepTime(val text: String) : OnTextChange()
        }

        sealed class OnIngredientChange : Action() {
            data class Text(val index: Int, val text: String) : OnIngredientChange()
            data class Delete(val index: Int) : OnIngredientChange()
            data object Add : OnIngredientChange()
        }

        sealed class OnStepChange : Action() {
            data class Text(val index: Int, val text: String) : OnStepChange()
            data class Delete(val index: Int) : OnStepChange()
            data class AddAt(val index: Int) : OnStepChange()
            data object Add : OnStepChange()
        }

        sealed class VerifyEmail : Action() {
            data object Send : VerifyEmail()
            data object Dismiss : VerifyEmail()
        }

        data class OnTagSelected(val name: Int) : Action()

        data object OnSubmitClick : Action()

        data object DialogDismissRequest : Action()
    }

    sealed class SideEffect {
        data object OpenImageSelector : SideEffect()
        data object NavigateToAuthenticationScreen : SideEffect()
        data object NavigateToThankYouScreen : SideEffect()
    }
}