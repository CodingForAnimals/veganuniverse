@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package org.codingforanimals.veganuniverse.search.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04

@Composable
internal fun SearchScreen(
    onBackClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = FocusRequester()
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }
        VUTopAppBar(
            title = {
                QueryTextField(
                    searchQuery,
                    setSearchQuery,
                    focusRequester,
                )
            },
            onBackClick = onBackClick,
        )

        val pagerState = rememberPagerState()
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ) {
            repeat(10) {
                Column(
                    modifier = Modifier
                        .clickable { coroutineScope.launch { pagerState.animateScrollToPage(it) } }
                        .padding(top = Spacing_04, bottom = Spacing_02),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing_02)
                ) {
                    VUIcon(icon = VUIcons.Community, contentDescription = "")
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Comunidad",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }

        HorizontalPager(state = pagerState, pageCount = 10) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
            ) {
                Text(text = "This is pager screen ${pagerState.currentPage}")
            }
        }
    }
}

@Composable
private fun QueryTextField(
    value: String,
    setValue: (String) -> Unit,
    focusRequester: FocusRequester,
) {
    TextField(
        modifier = Modifier
            .padding(vertical = Spacing_02)
            .focusRequester(focusRequester),
        value = value,
        onValueChange = setValue,
        leadingIcon = {
            VUIcon(
                icon = VUIcons.Search,
                contentDescription = ""
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                modifier = Modifier.wrapContentWidth(),
                visible = value.isNotEmpty(),
                enter = expandIn(expandFrom = Alignment.CenterStart),
                exit = shrinkOut(shrinkTowards = Alignment.CenterStart),
            ) {
                VUIcon(
                    icon = VUIcons.Close,
                    onIconClick = { setValue("") },
                    contentDescription = "",
                )
            }
        },
        placeholder = { Text("Buscar...") },
        textStyle = MaterialTheme.typography.bodyMedium,
    )
}