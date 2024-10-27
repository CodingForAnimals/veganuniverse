package org.codingforanimals.veganuniverse.recipes.presentation.create

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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeTag
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.PictureField
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.StringField
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.areFieldsValid
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_not_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailResult
import org.codingforanimals.veganuniverse.recipes.domain.model.RecipeForm
import org.codingforanimals.veganuniverse.recipes.domain.usecase.SubmitRecipe
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.recipes.presentation.create.model.StringListField
import org.codingforanimals.veganuniverse.recipes.presentation.create.model.TagsField

internal class CreateRecipeViewModel(
    private val submitRecipe: SubmitRecipe,
) : ViewModel() {

    private var imagePickerJob: Job? = null
    private var submitRecipeJob: Job? = null

    private val sideEffectChannel: Channel<SideEffect> = Channel()
    val sideEffect: Flow<SideEffect> = sideEffectChannel.receiveAsFlow()

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    private val navigationEffectsChannel = Channel<NavigationEffect>()
    val navigationEffects = navigationEffectsChannel.receiveAsFlow()

    sealed class NavigationEffect {
        data object NavigateUp : NavigationEffect()
        data object NavigateToThankYouScreen : NavigationEffect()
    }

    var showUnverifiedEmailDialog by mutableStateOf(false)
        private set

    var uiState by mutableStateOf(UiState())
        private set

    fun onAction(action: Action) {
        when (action) {
            is Action.ImagePicker -> handleImagePickerAction(action)
            is Action.OnTextChange -> updateTextForm(action)
            is Action.OnIngredientChange -> updateIngredients(action)
            is Action.OnStepChange -> updateSteps(action)
            is Action.OnTagSelected -> updateTags(action)
            Action.OnSubmitClick -> validateFieldsAndSubmit()
            Action.DialogDismissRequest -> dismissDialog()
            Action.OnBackClick -> {
                viewModelScope.launch {
                    navigationEffectsChannel.send(NavigationEffect.NavigateUp)
                }
            }
        }
    }

    fun onUnverifiedEmailResult(result: UnverifiedEmailResult) {
        showUnverifiedEmailDialog = false
        when (result) {
            UnverifiedEmailResult.Dismissed -> Unit
            UnverifiedEmailResult.UnexpectedError -> {
                viewModelScope.launch {
                    snackbarEffectsChannel.send(
                        Snackbar(message = verification_email_not_sent)
                    )
                }
            }

            UnverifiedEmailResult.VerificationEmailSent -> {
                viewModelScope.launch {
                    snackbarEffectsChannel.send(
                        Snackbar(message = verification_email_sent)
                    )
                }
            }
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
            is Action.OnTextChange.Name -> uiState.copy(nameField = StringField(action.text))
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
            val recipeForm = uiState.recipeForm() ?: run {
                uiState = uiState.copy(isValidating = true)
                return@launch
            }

            uiState = uiState.copy(loading = true)

            submitRecipe(recipeForm)
                .onSuccess {
                    uiState = uiState.copy(loading = false)
                    navigationEffectsChannel.send(NavigationEffect.NavigateToThankYouScreen)
                }
                .onFailure {
                    uiState = uiState.copy(loading = false)
                    snackbarEffectsChannel.send(Snackbar(R.string.create_recipe_error))
                }
        }
    }

    private fun dismissDialog() {
        uiState = uiState.copy(dialog = null)
    }

    data class UiState(
        val pictureField: PictureField = PictureField(),
        val nameField: StringField = StringField(),
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
                nameField,
                descriptionField,
                servingsField,
                prepTimeField,
                ingredientsField,
                stepsField,
                tagsField,
            )

        fun recipeForm(): RecipeForm? {
            return RecipeForm(
                imageModel = pictureField.model ?: return null,
                name = nameField.value.ifBlank { return null },
                description = descriptionField.value.ifBlank { return null },
                tags = tagsField.tags.mapNotNull {
                    if (!it.selected) return@mapNotNull null
                    RecipeTag.fromString(it.name)
                }.ifEmpty { return null },
                ingredients = ingredientsField.list.ifEmpty { return null },
                steps = stepsField.list.ifEmpty { return null },
                prepTime = prepTimeField.value.ifEmpty { return null },
                servings = servingsField.value.ifEmpty { return null },
            )
        }
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
            data class Name(val text: String) : OnTextChange()
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

        data class OnTagSelected(val name: Int) : Action()

        data object OnSubmitClick : Action()

        data object DialogDismissRequest : Action()
        data object OnBackClick : Action()
    }

    sealed class SideEffect {
        data object OpenImageSelector : SideEffect()
    }
}