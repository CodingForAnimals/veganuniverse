package org.codingforanimals.veganuniverse.ui.grid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.cards.VUCardDefaults

@Composable
fun StaggeredItemsGrid(
    items: List<StaggeredItem>,
    layoutType: ContainerLayoutType,
    onClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        horizontalArrangement = Arrangement.spacedBy(Spacing_04)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            when (layoutType) {
                ContainerLayoutType.VERTICAL_LEFT -> {
                    items.getOrNull(0)?.let { item ->
                        StaggeredItemCard(
                            title = item.title,
                            imageRef = item.imageRef,
                            layoutType = SimpleCardLayoutType.Vertical,
                            onClick = { onClick(item.id) },
                            elevation = VUCardDefaults.elevatedCardElevation(),
                        )
                    }
                }

                ContainerLayoutType.VERTICAL_RIGHT -> {
                    items.getOrNull(1)?.let { item ->
                        StaggeredItemCard(
                            title = item.title,
                            imageRef = item.imageRef,
                            layoutType = SimpleCardLayoutType.Squared,
                            onClick = { onClick(item.id) },
                            elevation = VUCardDefaults.elevatedCardElevation(),
                        )
                    }
                    items.getOrNull(2)?.let { item ->
                        StaggeredItemCard(
                            title = item.title,
                            imageRef = item.imageRef,
                            layoutType = SimpleCardLayoutType.Squared,
                            onClick = { onClick(item.id) },
                            elevation = VUCardDefaults.elevatedCardElevation(),
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            when (layoutType) {
                ContainerLayoutType.VERTICAL_LEFT -> {
                    items.getOrNull(1)?.let { item ->
                        StaggeredItemCard(
                            title = item.title,
                            imageRef = item.imageRef,
                            layoutType = SimpleCardLayoutType.Squared,
                            onClick = { onClick(item.id) },
                            elevation = VUCardDefaults.elevatedCardElevation(),
                        )
                    }
                    items.getOrNull(2)?.let { item ->
                        StaggeredItemCard(
                            title = item.title,
                            imageRef = item.imageRef,
                            layoutType = SimpleCardLayoutType.Squared,
                            onClick = { onClick(item.id) },
                            elevation = VUCardDefaults.elevatedCardElevation(),
                        )
                    }
                }

                ContainerLayoutType.VERTICAL_RIGHT -> {
                    items.getOrNull(0)?.let { item ->
                        StaggeredItemCard(
                            title = item.title,
                            imageRef = item.imageRef,
                            layoutType = SimpleCardLayoutType.Vertical,
                            onClick = { onClick(item.id) },
                            elevation = VUCardDefaults.elevatedCardElevation(),
                        )
                    }
                }
            }
        }
    }
}
