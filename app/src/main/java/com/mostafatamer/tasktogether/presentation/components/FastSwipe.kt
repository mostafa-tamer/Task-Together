package com.mostafatamer.tasktogether.presentation.components

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun FastSwipe(
    onRefresh: (() -> Unit) -> Unit,
    content: @Composable () -> Unit,
) {
    var isRefreshing by remember { mutableStateOf(false) }

    SwipeRefresh(
        refreshTriggerDistance = 100.dp,
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            isRefreshing = true
            onRefresh {
                isRefreshing = false
            }
        }
    ) {
        content()
    }
}

@Composable
fun FastSwipeReverse(
    onRefresh: (() -> Unit) -> Unit,
    content: @Composable () -> Unit,
) {
    var isRefreshing by remember { mutableStateOf(false) }

    SwipeRefresh(
        refreshTriggerDistance = 100.dp,
        modifier = Modifier.scale(scaleY = -1f, scaleX = 1f),
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            isRefreshing = true
            onRefresh {
                isRefreshing = false
            }
        }
    ) {
        Box(
            modifier = Modifier.scale(scaleY = -1f, scaleX = 1f),
        ) { content() }
    }
}


@Composable
fun CustomReverseSwipeRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
    val refreshState = rememberSwipeRefreshState(isRefreshing)
    val scope = rememberCoroutineScope()

    var dragOffset by remember { mutableFloatStateOf(0f) }
    val threshold = 150f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    dragOffset += dragAmount
                    if (dragOffset <= -threshold) {
                        onRefresh()
                        scope.launch {
                            dragOffset = 0f
                        }
                    }
                }
            }
    ) {
        SwipeRefresh(
            state = refreshState,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }
    }
}
