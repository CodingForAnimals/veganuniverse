package org.codingforanimals.veganuniverse.product.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.GetUserInfo
import org.codingforanimals.veganuniverse.product.presentation.components.ProductAdditionalInfoViewModel.ProductAdditionalInfoState
import org.codingforanimals.veganuniverse.product.presentation.model.Product
import org.codingforanimals.veganuniverse.product.presentation.model.ProductAdditionalInfo
import org.codingforanimals.veganuniverse.ui.Spacing_03
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.animation.ShimmerItem
import org.codingforanimals.veganuniverse.ui.animation.shimmer
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.components.VUIconDefaults
import org.codingforanimals.veganuniverse.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.utils.TimeAgo
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class ProductAdditionalInfoViewModel(
    private val product: Product,
    private val getUserInfo: GetUserInfo,
) : ViewModel() {

    val additionalInfo = flow<ProductAdditionalInfoState> {
        val userInfo = getUserInfo(product.userId)
        val additionalInfo = ProductAdditionalInfo(
            username = userInfo?.name,
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
        data class Content(val additionalInfo: ProductAdditionalInfo) : ProductAdditionalInfoState()
    }
}

@Composable
fun ProductAdditionalInfo(
    product: Product,
    onEditClick: () -> Unit,
    onReportClick: () -> Unit,
    onBookmarkClick: () -> Unit,
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
                                    text = TimeAgo.getTimeAgo(createdAt),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing_03),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            modifier = Modifier.size(VUIconDefaults.defaultIconSize),
                            painter = painterResource(VUIcons.Edit.id),
                            contentDescription = null,
                        )
                    }
                    IconButton(onClick = onReportClick) {
                        Icon(
                            modifier = Modifier.size(VUIconDefaults.defaultIconSize),
                            painter = painterResource(VUIcons.Report.id),
                            contentDescription = null,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = onBookmarkClick) {
                        Icon(
                            modifier = Modifier.size(VUIconDefaults.defaultIconSize),
                            painter = painterResource(VUIcons.Bookmark.id),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    )
}