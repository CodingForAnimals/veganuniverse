package org.codingforanimals.veganuniverse.product.list.presentation.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.GetUserInfo
import org.codingforanimals.veganuniverse.product.list.presentation.model.Product
import org.codingforanimals.veganuniverse.product.list.presentation.model.ProductAdditionalInfo

class ProductRowViewModel(
    private val getUserInfo: GetUserInfo,
) : ViewModel() {

    var additionalInfo by mutableStateOf<ProductAdditionalInfoState>(ProductAdditionalInfoState.Idle)

    fun onAction(action: Action) {
        when (action) {
            is Action.OnProductClick -> {
                if (additionalInfo == ProductAdditionalInfoState.Idle) {
                    additionalInfo = ProductAdditionalInfoState.Loading
                    viewModelScope.launch {
                        val userInfo = getUserInfo(action.product.userId)
                        val additionalInfo = ProductAdditionalInfo(
                            username = userInfo?.name,
                            createdAt = action.product.creationDate?.time,
                            comment = action.product.comment
                        )
                        this@ProductRowViewModel.additionalInfo =
                            ProductAdditionalInfoState.Success(additionalInfo)
                    }
                }
            }
        }
    }

    sealed class Action {
        data class OnProductClick(val product: Product) : Action()
    }

    sealed class ProductAdditionalInfoState {
        data object Idle : ProductAdditionalInfoState()
        data object Loading : ProductAdditionalInfoState()
        data class Success(val additionalInfo: ProductAdditionalInfo) : ProductAdditionalInfoState()
    }
}