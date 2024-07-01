package org.codingforanimals.veganuniverse.create.product.presentation

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.navigation.Deeplink
import org.codingforanimals.veganuniverse.commons.navigation.DeeplinkNavigator
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_title
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_message
import org.codingforanimals.veganuniverse.create.product.domain.model.ProductForm
import org.codingforanimals.veganuniverse.create.product.domain.usecase.SubmitProduct
import org.codingforanimals.veganuniverse.create.product.presentation.model.ProductCategoryField
import org.codingforanimals.veganuniverse.create.product.presentation.model.ProductTypeField
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.product.presentation.toUI
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.commons.ui.dialog.Dialog
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.PictureField
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.StringField
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.areFieldsValid
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_not_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailResult

class CreateProductViewModel(
    private val submitProduct: SubmitProduct,
    private val deeplinkNavigator: DeeplinkNavigator,
) : ViewModel() {

    private var imagePickerJob: Job? = null

    private val sideEffectsChannel = Channel<SideEffect>()
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

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
        _uiState.value =
            uiState.value.copy(productCategoryField = ProductCategoryField(action.category))
    }

    private fun attemptSubmitProduct() {
        if (!uiState.value.isFormValid()) {
            return showFormAsInvalid()
        }

        with(uiState.value) {
            val category = productCategoryField.category ?: return@with showFormAsInvalid()
            val type = productTypeField.type ?: return@with showFormAsInvalid()
            val imageModel = pictureField.model ?: return@with showFormAsInvalid()
            val productForm = ProductForm(
                name = nameField.value.trim(),
                brand = brandField.value.trim(),
                category = category,
                type = type,
                comment = commentsField.value.trim().ifBlank { null },
                imageModel = imageModel,
            )
            viewModelScope.launch {
                _uiState.value = uiState.value.copy(loading = true)
                val result = submitProduct(productForm)
                _uiState.value = uiState.value.copy(loading = false)
                when (result) {
                    SubmitProduct.Result.GuestUser -> {
                        sideEffectsChannel.send(SideEffect.NavigateToAuthenticateScreen)
                    }

                    SubmitProduct.Result.UnexpectedError -> {
                        _uiState.value = uiState.value.copy(
                            dialog = Dialog(
                                title = unexpected_error_title,
                                message = unexpected_error_message,
                            )
                        )
                    }

                    SubmitProduct.Result.UnverifiedEmail -> {
                        showUnverifiedEmailDialog = true
                    }

                    is SubmitProduct.Result.AlreadyExists -> {
                        _uiState.value = uiState.value.copy(
                            dialog = Dialog(
                                title = unexpected_error_title,
                                message = R.string.product_already_exists
                            )
                        )

                    }

                    is SubmitProduct.Result.Success -> {
                        sideEffectsChannel.send(SideEffect.NavigateToThankYouScreen)
                    }

                    SubmitProduct.Result.UserMustReauthenticate -> {
                        deeplinkNavigator.navigate(Deeplink.Reauthentication)
                    }
                }
            }
        }
    }

    private fun showFormAsInvalid() {
        _uiState.value = uiState.value.copy(loading = false, isValidating = true)
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
        val heroAnchorIcon = productTypeField.type?.toUI()?.icon
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