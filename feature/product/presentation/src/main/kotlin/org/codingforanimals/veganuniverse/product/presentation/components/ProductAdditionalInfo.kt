package org.codingforanimals.veganuniverse.product.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.GetUser
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.components.ProductAdditionalInfoViewModel.ProductAdditionalInfoState
import org.codingforanimals.veganuniverse.product.presentation.model.Product
import org.codingforanimals.veganuniverse.product.presentation.model.ProductAdditionalInfo
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.ui.animation.ShimmerItem
import org.codingforanimals.veganuniverse.commons.ui.animation.shimmer
import org.codingforanimals.veganuniverse.commons.ui.components.VUAssistChip
import org.codingforanimals.veganuniverse.commons.ui.components.VUAssistChipDefaults
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.utils.DateUtils
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class ProductAdditionalInfoViewModel(
    private val product: Product,
    private val getUser: GetUser,
) : ViewModel() {

    val additionalInfo = flow {
        val userInfo = product.userId?.let { getUser(it) } ?: return@flow emit(
            ProductAdditionalInfoState.NoContent
        )
        val additionalInfo = ProductAdditionalInfo(
            username = userInfo.name,
            createdAt = product.creationDate?.time,
            comment = product.comment
        )
        emit(ProductAdditionalInfoState.Content(additionalInfo))
    }.stateIn(
        initialValue = ProductAdditionalInfoState.Loading,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000)
    )

    sealed class ProductAdditionalInfoState {
        data object Loading : ProductAdditionalInfoState()
        data object NoContent : ProductAdditionalInfoState()
        data class Content(val additionalInfo: ProductAdditionalInfo) : ProductAdditionalInfoState()
    }
}

@Composable
fun ProductAdditionalInfo(
    product: Product,
    onEditClick: () -> Unit,
    onReportClick: () -> Unit,
    viewModel: ProductAdditionalInfoViewModel = koinViewModel(
        parameters = { parametersOf(product) },
        key = "${product.id}_additional_details"
    ),
) {
    val state by viewModel.additionalInfo.collectAsStateWithLifecycle()
    Crossfade(
        targetState = state,
        label = "",
        content = { currentState ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(
                        start = Spacing_06,
                        end = Spacing_06,
                        bottom = Spacing_04
                    ),
                verticalArrangement = Arrangement.spacedBy(Spacing_04),
            ) {
                when (currentState) {
                    ProductAdditionalInfoState.Loading -> {
                        Box(
                            modifier = Modifier.shimmer(),
                        ) {
                            ShimmerItem(
                                modifier = Modifier
                                    .height(46.dp)
                                    .fillMaxWidth()
                                    .clip(ShapeDefaults.Small)
                            )
                        }
                        Box(
                            modifier = Modifier.shimmer(),
                        ) {
                            ShimmerItem(
                                modifier = Modifier
                                    .height(20.dp)
                                    .fillMaxWidth(0.5f)
                                    .clip(ShapeDefaults.Small)
                            )
                        }

                        Box(
                            modifier = Modifier.shimmer(),
                        ) {
                            ShimmerItem(
                                modifier = Modifier
                                    .height(20.dp)
                                    .fillMaxWidth(0.3f)
                                    .clip(ShapeDefaults.Small)
                            )
                        }
                    }

                    is ProductAdditionalInfoState.Content -> {
                        currentState.additionalInfo.comment?.let { comment ->
                            Text(
                                text = comment,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        currentState.additionalInfo.username?.let { username ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Spacing_03),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                VUIcon(icon = VUIcons.Profile, contentDescription = "")
                                Text(
                                    modifier = Modifier.wrapContentHeight(),
                                    text = username,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                        currentState.additionalInfo.createdAt?.let { createdAt ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Spacing_03),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                VUIcon(icon = VUIcons.Clock, contentDescription = "")
                                Text(
                                    modifier = Modifier.wrapContentHeight(),
                                    text = DateUtils.getTimeAgo(createdAt),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }

                    ProductAdditionalInfoState.NoContent -> Unit
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing_03),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    VUAssistChip(
                        onClick = onEditClick,
                        label = stringResource(R.string.product_additional_info_suggest_changes),
                        icon = VUIcons.Edit,
                        colors = VUAssistChipDefaults.secondaryColors(),
                        chipElevation = VUAssistChipDefaults.elevatedAssistChipElevation(),
                    )
                    VUAssistChip(
                        onClick = onReportClick,
                        label = stringResource(R.string.product_additional_info_report),
                        icon = VUIcons.Report,
                        colors = VUAssistChipDefaults.secondaryColors(),
                        chipElevation = VUAssistChipDefaults.elevatedAssistChipElevation(),
                    )
                }
            }
        }
    )
}