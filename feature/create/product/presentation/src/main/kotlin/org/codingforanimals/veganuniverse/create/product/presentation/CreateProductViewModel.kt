package org.codingforanimals.veganuniverse.create.product.presentation

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.product.presentation.toUI
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.ui.dialog.Dialog
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.PictureField
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.StringField
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.areFieldsValid
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_not_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailResult
import org.codingforanimals.veganuniverse.create.product.domain.model.ProductForm
import org.codingforanimals.veganuniverse.create.product.domain.usecase.SubmitProduct
import org.codingforanimals.veganuniverse.create.product.presentation.model.ProductTypeField

class CreateProductViewModel(
    savedStateHandle: SavedStateHandle,
    private val submitProduct: SubmitProduct,
) : ViewModel() {

    private var imagePickerJob: Job? = null

    private val sideEffectsChannel = Channel<SideEffect>()
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    private val navigationEffectsChannel = Channel<NavigationEffect>()
    val navigationEffects = navigationEffectsChannel.receiveAsFlow()

    sealed class NavigationEffect {
        data object NavigateUp : NavigationEffect()
        data object NavigateToThankYouScreen : NavigationEffect()
    }

    var uiState by mutableStateOf(UiState.fromCategory(savedStateHandle[CATEGORY]))
        private set

    var showUnverifiedEmailDialog: Boolean by mutableStateOf(false)
        private set

    fun onAction(action: Action) {
        when (action) {
            Action.OnBackClick -> navigateUp()
            is Action.ImagePicker -> handleImagePickerAction(action)
            is Action.OnTextChange -> updateTextForm(action)
            is Action.OnProductTypeSelected -> handleProductTypeSelect(action)
            is Action.OnProductCategorySelected -> handleProductCategorySelect(action)
            Action.OnSubmitClick -> attemptSubmitProduct()
            Action.DismissDialog -> dismissDialog()
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

    private fun navigateUp() {
        viewModelScope.launch {
            navigationEffectsChannel.send(NavigationEffect.NavigateUp)
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
//        uiState =
//            uiState.copy(productCategoryField = ProductCategoryField(action.category))
    }

    private fun attemptSubmitProduct() {
        if (!uiState.isFormValid()) {
            return showFormAsInvalid()
        }

        with(uiState) {
//            val category = productCategoryField.category ?: return@with showFormAsInvalid()
            val type = productTypeField.type ?: return@with showFormAsInvalid()
            val productForm = ProductForm(
                name = nameField.value.trim(),
                brand = brandField.value.trim().takeIf { productSupportsBrand },
                category = category,
                type = type,
                comment = commentsField.value.trim().ifBlank { null },
                imageModel = pictureField.model.takeIf { productSupportsImage },
            )
            viewModelScope.launch {
                uiState = uiState.copy(loading = true)
                submitProduct(productForm)
                    .onSuccess {
                        uiState = uiState.copy(loading = false)
                        navigationEffectsChannel.send(NavigationEffect.NavigateToThankYouScreen)
                    }
                    .onFailure { exception ->
                        uiState = uiState.copy(loading = false)
                        when (exception) {
                            is SubmitProduct.ProductConflictException -> {
                                uiState = uiState.copy(
                                    dialog = Dialog(
                                        title = R.string.product_already_exists,
                                        message = R.string.product_already_exists_message
                                    )
                                )
                            }

                            else -> {
                                snackbarEffectsChannel.send(Snackbar(R.string.create_product_error))
                            }
                        }
                    }
            }
        }
    }

    private fun showFormAsInvalid() {
        uiState = uiState.copy(loading = false, isValidating = true)
    }

    private fun dismissDialog() {
        uiState = uiState.copy(dialog = null)
    }

    data class UiState(
        val category: ProductCategory,
        val pictureField: PictureField = PictureField(),
        val nameField: StringField = StringField(),
        val brandField: StringField = StringField(),
        val commentsField: StringField = StringField(),
        val productTypeField: ProductTypeField = ProductTypeField(),
        val isValidating: Boolean = false,
        val dialog: Dialog? = null,
        val loading: Boolean = false,
    ) {
        val title: Int = when (category) {
            ProductCategory.ADDITIVE -> R.string.create_additive_title
            else -> R.string.create_product_title
        }
        val heroAnchorIcon = productTypeField.type?.toUI()?.icon
        fun isFormValid() = areFieldsValid(
            pictureField.takeIf { productFormRequiresImage },
            nameField,
            brandField.takeIf { productSupportsBrand },
            productTypeField,
        )

        @StringRes
        val productNamePlaceholder: Int = when (category) {
            ProductCategory.ADDITIVE -> R.string.additive_name
            else -> R.string.product_name
        }

        val productSupportsImage: Boolean = when (category) {
            ProductCategory.ADDITIVE -> false
            else -> true
        }

        private val productFormRequiresImage: Boolean = when (category) {
            ProductCategory.ADDITIVE,
            ProductCategory.OTHER -> false

            else -> true
        }

        val productSupportsBrand: Boolean = when (category) {
            ProductCategory.ADDITIVE -> false
            else -> true
        }

        companion object {
            fun fromCategory(value: String?): UiState {
                val category =
                    ProductCategory.fromString(value) ?: ProductCategory.OTHER
                return UiState(
                    category = category
                )
            }
        }
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
        data object OpenImageSelector : SideEffect()
    }

    companion object {
        private const val CATEGORY = "category"
    }
}