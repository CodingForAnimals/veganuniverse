package org.codingforanimals.veganuniverse.product.presentation.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductSorter
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_not_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailResult
import org.codingforanimals.veganuniverse.product.domain.usecase.EditProduct
import org.codingforanimals.veganuniverse.product.domain.usecase.GetLatestProducts
import org.codingforanimals.veganuniverse.product.domain.usecase.ReportProduct
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.model.toView
import org.codingforanimals.veganuniverse.product.presentation.model.Product

internal class ProductHomeViewModel(
    val getLatestProducts: GetLatestProducts,
    val reportProduct: ReportProduct,
    val editProduct: EditProduct,
) : ViewModel() {

    private val navigationEffectsChannel = Channel<NavigationEffect>()
    val navigationEffects = navigationEffectsChannel.receiveAsFlow()

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    val latestProductsState = flow {
        val result = runCatching {
            val latestProducts = getLatestProducts().map { it.toView() }
            LatestProductsState.Success(latestProducts)
        }.getOrElse {
            Log.e(TAG, it.stackTraceToString())
            LatestProductsState.Error
        }
        emit(result)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LatestProductsState.Loading,
    )

    var showReportDialog: String? by mutableStateOf(null)
        private set

    var showSuggestionDialog: String? by mutableStateOf(null)
        private set

    var showUnverifiedEmailDialog: Boolean by mutableStateOf(false)
        private set

    fun onAction(action: Action) {
        when (action) {
            is Action.OnProductCategorySelected -> {
                navigateToProductBrowsing(category = action.category)
            }

            Action.OnMostRecentShowMoreClick -> {
                navigateToProductBrowsing(sorter = ProductSorter.DATE)
            }

            Action.OnCreateProductClick -> {
                viewModelScope.launch {
                    navigationEffectsChannel.send(NavigationEffect.NavigateToCreateProduct)
                }
            }

            is Action.OpenReportDialog -> {
                showReportDialog = action.productId
            }
            is Action.OpenSuggestDialog -> {
                showSuggestionDialog = action.productId
            }

            is Action.OnProductClick -> {
                action.product.id?.let { id ->
                    viewModelScope.launch {
                        navigationEffectsChannel.send(NavigationEffect.NavigateToProductDetail(id))
                    }
                }
            }

            is Action.OnProductTypeClick -> {
                navigateToProductBrowsing(
                    type = action.type
                )
            }
            Action.OnShowAllClick -> {
                navigateToProductBrowsing()
            }
        }
    }

    fun onReportResult(action: ReportContentDialogResult) {
        when (action) {
            ReportContentDialogResult.Dismiss -> {
                showReportDialog = null
            }

            ReportContentDialogResult.SendReport -> {
                viewModelScope.launch {
                    showReportDialog?.let {
                        val result = reportProduct(it)
                        showReportDialog = null
                        when (result) {
                            ReportProduct.Result.UnauthenticatedUser -> {
                                navigationEffectsChannel.send(NavigationEffect.NavigateToAuthentication)
                            }

                            ReportProduct.Result.Success -> {
                                snackbarEffectsChannel.send(
                                    Snackbar(R.string.product_report_success)
                                )
                            }

                            ReportProduct.Result.UnexpectedError -> {
                                snackbarEffectsChannel.send(Snackbar(R.string.product_report_error))
                            }

                            ReportProduct.Result.UnverifiedEmail -> {
                                showUnverifiedEmailDialog = true
                            }
                        }
                    }
                }
            }
        }
    }

    fun onEditResult(action: EditContentDialogResult) {
        when (action) {
            EditContentDialogResult.Dismiss -> {
                showSuggestionDialog = null
            }

            is EditContentDialogResult.SendEdit -> {
                viewModelScope.launch {
                    showSuggestionDialog?.let {
                        val result = editProduct(it, action.edition)
                        showSuggestionDialog = null
                        when (result) {
                            EditProduct.Result.UnauthenticatedUser -> {
                                navigationEffectsChannel.send(NavigationEffect.NavigateToAuthentication)
                            }

                            EditProduct.Result.UnverifiedEmail -> {
                                showUnverifiedEmailDialog = true
                            }

                            EditProduct.Result.UnexpectedError -> {
                                snackbarEffectsChannel.send(Snackbar(R.string.product_edit_error))
                            }

                            EditProduct.Result.Success -> {
                                snackbarEffectsChannel.send(Snackbar(R.string.product_edit_success))
                            }
                        }
                    }
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

    private fun navigateToProductBrowsing(
        category: ProductCategory? = null,
        type: ProductType? = null,
        sorter: ProductSorter? = null,
    ) {
        val navigationEffect = NavigationEffect.NavigateToProductBrowsing(
            category = category,
            type = type,
            sorter = sorter
        )
        viewModelScope.launch {
            navigationEffectsChannel.send(navigationEffect)
        }
    }

    sealed class LatestProductsState {
        data object Loading : LatestProductsState()
        data object Error : LatestProductsState()
        data class Success(val products: List<Product>) : LatestProductsState()
    }

    sealed class Action {
        data class OnProductCategorySelected(val category: ProductCategory) : Action()
        data object OnMostRecentShowMoreClick : Action()
        data object OnCreateProductClick : Action()
        data object OnShowAllClick : Action()

        data class OpenReportDialog(val productId: String) : Action()
        data class OpenSuggestDialog(val productId: String) : Action()
        data class OnProductClick(val product: Product) : Action()
        data class OnProductTypeClick(val type: ProductType) : Action()
    }

    sealed class NavigationEffect {
        data class NavigateToProductBrowsing(
            val category: ProductCategory? = null,
            val type: ProductType? = null,
            val sorter: ProductSorter? = null,
        ) : NavigationEffect()

        data class NavigateToProductDetail(val id: String) : NavigationEffect()

        data object NavigateToCreateProduct : NavigationEffect()
        data object NavigateToAuthentication : NavigationEffect()
    }

    companion object {
        private const val TAG = "ProductHomeViewModel"
    }
}