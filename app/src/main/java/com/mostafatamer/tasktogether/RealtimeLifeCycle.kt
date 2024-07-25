package com.mostafatamer.tasktogether

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.mostafatamer.tasktogether.lib.StompLifecycleManager

@Composable
fun RealtimeLifeCycle(stompService: StompLifecycleManager) {
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(Unit) {
        val lifecycle = lifecycleOwner.value.lifecycle

        lifecycle.addObserver(stompService)

        onDispose {
            lifecycle.removeObserver(stompService)
        }
    }
}