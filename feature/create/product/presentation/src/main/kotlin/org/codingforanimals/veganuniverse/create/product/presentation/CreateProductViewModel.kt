package org.codingforanimals.veganuniverse.create.product.presentation

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.create.product.presentation.model.ProductCategoryField
import org.codingforanimals.veganuniverse.create.product.presentation.model.ProductTypeField
import org.codingforanimals.veganuniverse.create.product.presentation.usecase.SubmitProductUseCase
import org.codingforanimals.veganuniverse.product.ui.ProductCategory
import org.codingforanimals.veganuniverse.product.ui.ProductType
import org.codingforanimals.veganuniverse.ui.dialog.Dialog
import org.codingforanimals.veganuniverse.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.ui.viewmodel.PictureField
import org.codingforanimals.veganuniverse.ui.viewmodel.StringField
import org.codingforanimals.veganuniverse.ui.viewmodel.areFieldsValid

class CreateProductViewModel(
    private val submitProduct: SubmitProductUseCase,
) : ViewModel() {

    private var imagePickerJob: Job? = null

    private val sideEffectsChannel = Channel<SideEffect>()
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    var uiState by mutableStateOf(UiState())
        private set

    fun onAction(action: Action) {
        when (action) {
            is Action.ImagePicker -> handleImagePickerAction(action)
            is Action.OnTextChange -> updateTextForm(action)
            is Action.OnProductTypeSelected -> handleProductTypeSelect(action)
            is Action.OnProductCategorySelected -> handleProductCategorySelect(action)
            Action.OnSubmitClick -> handleSubmitClick()
        }
    }

    private fun handleImagePickerAction(action: Action.ImagePicker) {
        when (action) {
            Action.ImagePicker.Click -> {
                imagePickerJob?.cancel()
                imagePickerJob = viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.OpenImageSelector)
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
            is Action.OnTextChange.Brand -> uiState.copy(brandField = StringField(action.text))
            is Action.OnTextChange.Comments -> uiState.copy(commentsField = StringField(action.text))
        }
    }

    private fun handleProductTypeSelect(action: Action.OnProductTypeSelected) {
        uiState = uiState.copy(productTypeField = ProductTypeField(action.type))
    }

    private fun handleProductCategorySelect(action: Action.OnProductCategorySelected) {
        uiState = uiState.copy(productCategoryField = ProductCategoryField(action.category))
    }

    private var submitProductJob: Job? = null
    private fun handleSubmitClick() {
        submitProductJob?.cancel()
        submitProductJob = viewModelScope.launch {
            submitProduct(uiState).collectLatest { status ->
                when (status) {
                    SubmitProductUseCase.Status.Error -> {
                        uiState = uiState.copy(
                            loading = false,
                            dialog = Dialog.unknownErrorDialog(),
                        )
                    }

                    SubmitProductUseCase.Status.GuestUser -> {
                        sideEffectsChannel.send(SideEffect.NavigateToAuthenticateScreen)
                    }

                    SubmitProductUseCase.Status.InvalidForm -> {
                        uiState = uiState.copy(loading = false, isValidating = true)
                    }

                    SubmitProductUseCase.Status.Loading -> {
                        uiState = uiState.copy(loading = true)
                    }

                    SubmitProductUseCase.Status.Success -> {
                        uiState = uiState.copy(loading = false)
                        sideEffectsChannel.send(SideEffect.NavigateToThankYouScreen)
                    }
                }

            }
        }
    }

    data class UiState(
        val pictureField: PictureField = PictureField(),
        val nameField: StringField = StringField(),
        val brandField: StringField = StringField(),
        val commentsField: StringField = StringField(),
        val productTypeField: ProductTypeField = ProductTypeField(),
        val productCategoryField: ProductCategoryField = ProductCategoryField(),
        val isValidating: Boolean = false,
        val dialog: Dialog? = null,
        val loading: Boolean = false,
    ) {
        val heroAnchorIcon = productTypeField.type?.icon ?: VUIcons.ProductConfirmedVegan
        fun isFormValid() = areFieldsValid(
            pictureField,
            nameField,
            brandField,
            productTypeField,
            productCategoryField,
        )
    }

    sealed class Action {
        sealed class ImagePicker : Action() {
            data object Click : ImagePicker()
            data class Success(val uri: Uri?) : ImagePicker()
        }

        sealed class OnTextChange : Action() {
            data class Name(val text: String) : OnTextChange()
            data class Brand(val text: String) : OnTextChange()
            data class Comments(val text: String) : OnTextChange()
        }

        data class OnProductTypeSelected(val type: ProductType) : Action()
        data class OnProductCategorySelected(val category: ProductCategory) : Action()
        data object OnSubmitClick : Action()
    }

    sealed class SideEffect {
        data object OpenImageSelector : SideEffect()
        data object NavigateToAuthenticateScreen : SideEffect()
        data object NavigateToThankYouScreen : SideEffect()

    }
}