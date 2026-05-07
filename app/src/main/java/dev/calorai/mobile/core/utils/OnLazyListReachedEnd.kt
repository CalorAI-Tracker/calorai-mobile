package dev.calorai.mobile.core.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun OnLazyListReachedEnd(
    listState: LazyListState,
    itemsCount: Int,
    threshold: Int = 0,
    enabled: Boolean = true,
    onReachedEnd: () -> Unit,
) {
    LaunchedEffect(listState, itemsCount, threshold, enabled) {
        if (!enabled) return@LaunchedEffect

        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .map { lastVisibleItemIndex ->
                val lastDataIndex = itemsCount - 1
                lastVisibleItemIndex != null &&
                    lastDataIndex >= 0 &&
                    lastVisibleItemIndex >= lastDataIndex - threshold
            }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                onReachedEnd()
            }
    }
}
