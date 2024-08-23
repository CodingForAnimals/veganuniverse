package org.codingforanimals.veganuniverse.create.product.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.create.product.domain.model.ProductForm
import org.codingforanimals.veganuniverse.create.product.domain.usecase.SaveProduct
import org.codingforanimals.veganuniverse.create.product.domain.usecase.SaveProductStatus
import org.codingforanimals.veganuniverse.create.product.presentation.model.ProductCategoryField
import org.codingforanimals.veganuniverse.create.product.presentation.model.ProductTypeField
import org.codingforanimals.veganuniverse.product.ui.ProductCategory
import org.codingforanimals.veganuniverse.product.ui.ProductType
import org.codingforanimals.veganuniverse.ui.dialog.Dialog
import org.codingforanimals.veganuniverse.ui.viewmodel.PictureField
import org.codingforanimals.veganuniverse.ui.viewmodel.StringField
import org.codingforanimals.veganuniverse.ui.viewmodel.areFieldsValid

class CreateProductViewModel(
    private val saveProduct: SaveProduct,
) : ViewModel() {

    private var imagePickerJob: Job? = null

    private val sideEffectsChannel = Channel<SideEffect>()
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: Action) {
        when (action) {
            Action.OnBackClick -> navigateUp()
            is Action.ImagePicker -> handleImagePickerAction(action)
            is Action.OnTextChange -> updateTextForm(action)
            is Action.OnProductTypeSelected -> handleProductTypeSelect(action)
            is Action.OnProductCategorySelected -> handleProductCategorySelect(action)
            Action.OnSubmitClick -> handleSubmitClick()
            Action.DismissDialog -> dismissDialog()
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            sideEffectsChannel.send(SideEffect.NavigateUp)
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
                _uiState.value = uiState.value.copy(pictureField = PictureField(model = action.uri))
            }
        }
    }

    private fun updateTextForm(action: Action.OnTextChange) {
        _uiState.value = when (action) {
            is Action.OnTextChange.Name -> uiState.value.copy(nameField = StringField(action.text))
            is Action.OnTextChange.Brand -> uiState.value.copy(brandField = StringField(action.text))
            is Action.OnTextChange.Comments -> uiState.value.copy(commentsField = StringField(action.text))
        }
    }

    private fun handleProductTypeSelect(action: Action.OnProductTypeSelected) {
        _uiState.value = uiState.value.copy(productTypeField = ProductTypeField(action.type))
    }

    private fun handleProductCategorySelect(action: Action.OnProductCategorySelected) {
        _uiState.value = uiState.value.copy(productCategoryField = ProductCategoryField(action.category))
    }

    private var saveProductJob: Job? = null
    private fun handleSubmitClick() {
        if (!uiState.value.isFormValid()) {
            showFormAsInvalid()
        } else {
            saveProductJob?.cancel()
            saveProductJob = viewModelScope.launch {
                val category = uiState.value.productCategoryField.category?.name
                    ?: return@launch showFormAsInvalid()
                val type = uiState.value.productTypeField.type?.name
                    ?: return@launch showFormAsInvalid()
                val imageModel = uiState.value.pictureField.model
                    ?: return@launch showFormAsInvalid()
                saveProduct(
                    productForm = ProductForm(
                        name = uiState.value.nameField.value.trim(),
                        brand = uiState.value.brandField.value.trim(),
                        category = category,
                        type = type,
                        comment = uiState.value.commentsField.value.trim().ifBlank { null },
                        imageModel = imageModel,
                    )
                ).collectLatest { status ->
                    when (status) {
                        SaveProductStatus.Loading -> {
                            _uiState.value = uiState.value.copy(loading = true)
                        }

                        is SaveProductStatus.Success -> {
                            _uiState.value = uiState.value.copy(loading = false)
                            sideEffectsChannel.send(SideEffect.NavigateToThankYouScreen)
                        }

                        is SaveProductStatus.Error -> {
                            handleSaveProductError(status)
                        }
                    }
                }
            }
        }
    }

    private fun showFormAsInvalid() {
        _uiState.value = uiState.value.copy(loading = false, isValidating = true)
    }

    private suspend fun handleSaveProductError(status: SaveProductStatus.Error) {
        val titleMessage = when (status) {
            is SaveProductStatus.Error.ProductAlreadyExists -> Pair(
                org.codingforanimals.veganuniverse.ui.R.string.generic_error_title,
                R.string.product_already_exists
            )

            is SaveProductStatus.Error.UnexpectedError -> Pair(
                org.codingforanimals.veganuniverse.ui.R.string.generic_error_title,
                org.codingforanimals.veganuniverse.ui.R.string.unknown_error_message,
            )

            SaveProductStatus.Error.UnregisteredUser -> {
                sideEffectsChannel.send(SideEffect.NavigateToAuthenticateScreen)
                null
            }

            SaveProductStatus.Error.UnverifiedEmail -> Pair(
                org.codingforanimals.veganuniverse.ui.R.string.generic_error_title,
                org.codingforanimals.veganuniverse.core.ui.R.string.email_not_yet_verified,
            )
        }
        titleMessage?.let { (title, message) ->
            _uiState.value = uiState.value.copy(
                dialog = Dialog(
                    title = title,
                    message = message,
                )
            )
        }
    }

    private fun dismissDialog() {
        _uiState.value = uiState.value.copy(dialog = null)
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
        val heroAnchorIcon = productTypeField.type?.icon
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
        data object DismissDialog : Action()
        data object OnBackClick : Action()
    }

    sealed class SideEffect {
        data object NavigateUp : SideEffect()
        data object OpenImageSelector : SideEffect()
        data object NavigateToAuthenticateScreen : SideEffect()
        data object NavigateToThankYouScreen : SideEffect()

    }
}