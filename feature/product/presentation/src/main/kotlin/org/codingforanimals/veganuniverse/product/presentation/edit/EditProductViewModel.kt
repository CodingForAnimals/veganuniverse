package org.codingforanimals.veganuniverse.product.presentation.edit

import android.net.Uri
import android.os.Parcelable
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
import org.codingforanimals.veganuniverse.commons.ui.dialog.Dialog
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_not_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailResult
import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory
import org.codingforanimals.veganuniverse.product.domain.model.ProductType
import org.codingforanimals.veganuniverse.product.domain.usecase.GetProductDetail
import org.codingforanimals.veganuniverse.product.domain.usecase.UploadProduct
import org.codingforanimals.veganuniverse.product.domain.usecase.UploadProductEdit
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.model.toUI
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination.Edit.Companion.ID

class EditProductViewModel(
    savedStateHandle: SavedStateHandle,
    private val getProductDetail: GetProductDetail,
    private val uploadProductUseCase: UploadProduct,
    private val uploadProductEditUseCase: UploadProductEdit,
) : ViewModel() {

    private var imagePickerJob: Job? = null

    private val sideEffectsChannel = Channel<SideEffect>()
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    private val navigationEffectsChannel = Channel<NavigationEffect>()
    val navigationEffects = navigationEffectsChannel.receiveAsFlow()

    sealed class NavigationEffect {
        data class NavigateUp(val snackbar: Snackbar? = null) : NavigationEffect()
        data object NavigateToThankYouScreen : NavigationEffect()
    }

    var uiState by mutableStateOf(UiState())
        private set

    var showUnverifiedEmailDialog: Boolean by mutableStateOf(false)
        private set

    val id = savedStateHandle.get<String>(ID)
    var entryPoint: EntryPoint? by mutableStateOf(null)
        private set

    sealed class EntryPoint {
        data class Edit(val originalProduct: Product) : EntryPoint()
        data object Create : EntryPoint()
    }

    init {
        if (id != null) {
            viewModelScope.launch {
                val product = getProductDetail(id) ?: return@launch
                entryPoint = EntryPoint.Edit(product)
                uiState = uiState.copy(
                    category = product.category,
                    name = product.name,
                    brand = product.brand,
                    description = product.description.orEmpty(),
                    type = product.type,
                    imageUrl = product.imageUrl,
                    sourceUrl = product.sourceUrl.orEmpty(),
                    loading = false,
                )
            }
        } else {
            entryPoint = EntryPoint.Create
            uiState = uiState.copy(showCategories = true)
        }
    }

    fun onAction(action: Action) {
        when (action) {
            Action.OnBackClick -> navigateUp()
            is Action.ImagePicker -> handleImagePickerAction(action)
            is Action.OnTextChange -> updateTextForm(action)
            is Action.OnProductTypeSelected -> handleProductTypeSelect(action)
            Action.OnProductCategoryClick -> {
                uiState = uiState.copy(showCategories = true)
            }

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
            navigationEffectsChannel.send(NavigationEffect.NavigateUp())
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
                uiState = uiState.copy(imageModel = action.uri)
            }
        }
    }

    private fun updateTextForm(action: Action.OnTextChange) {
        uiState = when (action) {
            is Action.OnTextChange.Name -> uiState.copy(name = action.text)
            is Action.OnTextChange.Brand -> uiState.copy(brand = action.text)
            is Action.OnTextChange.Description -> uiState.copy(description = action.text)
            is Action.OnTextChange.SourceUrl -> uiState.copy(sourceUrl = action.text)
        }
    }

    private fun handleProductTypeSelect(action: Action.OnProductTypeSelected) {
        uiState = uiState.copy(type = action.type)
    }

    private fun handleProductCategorySelect(action: Action.OnProductCategorySelected) {
        uiState = uiState.copy(category = action.category, showCategories = false)
    }

    private fun attemptSubmitProduct() {
        val isImageValid =
            (uiState.imageModel != null).takeIf { entryPoint is EntryPoint.Create } ?: true
        val type = uiState.type
        val category = uiState.category
        if (uiState.name.isBlank() || uiState.brand.isBlank() || !isImageValid || type == null || category == null) {
            return showFormAsInvalid()
        }

        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            val product = Product(
                id = null,
                name = uiState.name.trim(),
                brand = uiState.brand.trim(),
                description = uiState.description.trim(),
                type = type,
                category = category,
                userId = null,
                username = null,
                createdAt = null,
                lastUpdatedAt = null,
                imageUrl = null,
                sourceUrl = uiState.sourceUrl,
            )

            when (val entryPoint = entryPoint) {
                EntryPoint.Create -> {
                    uploadProduct(
                        product = product,
                    )
                }

                is EntryPoint.Edit -> {
                    uploadProductEdit(
                        product = product,
                        originalProduct = entryPoint.originalProduct,
                    )
                }

                null -> Unit
            }
        }
    }

    private suspend fun uploadProductEdit(
        product: Product,
        originalProduct: Product,
    ) {
        uploadProductEditUseCase(
            edit = product.copy(
                id = originalProduct.id,
                userId = originalProduct.userId,
                username = originalProduct.username,
                createdAt = originalProduct.createdAt,
                lastUpdatedAt = null,
                imageUrl = originalProduct.imageUrl,
            ),
            imageModel = uiState.imageModel,
        )
            .onSuccess {
                uiState = uiState.copy(loading = false)
                navigationEffectsChannel.send(NavigationEffect.NavigateUp(Snackbar(R.string.product_edit_send_success)))
            }
            .onFailure { exception ->
                uiState = uiState.copy(loading = false)
                when (exception) {
                    is UploadProduct.ProductConflictException -> {
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

    private suspend fun uploadProduct(product: Product) {
        uploadProductUseCase(
            product = product,
            imageModel = uiState.imageModel,
        )
            .onSuccess {
                uiState = uiState.copy(loading = false)
                navigationEffectsChannel.send(NavigationEffect.NavigateToThankYouScreen)
            }
            .onFailure { exception ->
                uiState = uiState.copy(loading = false)
                when (exception) {
                    is UploadProduct.ProductConflictException -> {
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

    private fun showFormAsInvalid() {
        uiState = uiState.copy(loading = false, isValidating = true)
    }

    private fun dismissDialog() {
        uiState = uiState.copy(dialog = null)
    }

    data class UiState(
        val category: ProductCategory? = null,
        val imageModel: Parcelable? = null,
        val imageUrl: String? = null,
        val name: String = "",
        val brand: String = "",
        val description: String = "",
        val type: ProductType? = null,
        val sourceUrl: String = "",
        val isValidating: Boolean = false,
        val dialog: Dialog? = null,
        val loading: Boolean = false,
        val showCategories: Boolean = false,
    ) {
        val heroAnchorIcon = type?.toUI()?.icon
    }

    sealed class Action {
        sealed class ImagePicker : Action() {
            data object Click : ImagePicker()
            data class Success(val uri: Uri?) : ImagePicker()
        }

        sealed class OnTextChange : Action() {
            data class Name(val text: String) : OnTextChange()
            data class Brand(val text: String) : OnTextChange()
            data class Description(val text: String) : OnTextChange()
            data class SourceUrl(val text: String) : OnTextChange()
        }

        data class OnProductTypeSelected(val type: ProductType) : Action()
        data class OnProductCategorySelected(val category: ProductCategory) : Action()
        data object OnSubmitClick : Action()
        data object DismissDialog : Action()
        data object OnBackClick : Action()
        data object OnProductCategoryClick : Action()
    }

    sealed class SideEffect {
        data object OpenImageSelector : SideEffect()
    }

    companion object {
        private const val CATEGORY = "category"
    }
}
