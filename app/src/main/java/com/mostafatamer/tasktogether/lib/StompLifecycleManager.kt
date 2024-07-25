package com.mostafatamer.tasktogether.lib

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.mostafatamer.tasktogether.SocketManager

class StompLifecycleManager(private vararg val socketManagers: SocketManager) :
    LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> connect()
            Lifecycle.Event.ON_STOP -> disconnect()
            else -> {}
        }
    }

    private fun connect() {
        socketManagers.forEach { socketManager ->
            if (!socketManager.isConnected()) {
                socketManager.connect()
            }
        }
    }

    private fun disconnect() {
        socketManagers.forEach { socketManager ->
            if (socketManager.isConnected()) {
                socketManager.disconnect()
            }
        }
    }
}