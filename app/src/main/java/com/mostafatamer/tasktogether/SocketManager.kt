package com.mostafatamer.tasktogether

import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException

class SocketManager(private val token: String) {
    private lateinit var mSocket: Socket

    private val lazyInvokedTasks = mutableListOf<() -> Any>()
    private val runningTasks = mutableListOf<() -> Any>()


    init {
        try {
            val opts = IO.Options().apply {
                secure = true
                extraHeaders = mapOf(
                    "authorization" to listOf(token)
                )
            }

            mSocket = IO.socket("https://task-together-2020.onrender.com", opts)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun connect() {
        mSocket.connect()

        mSocket.on(Socket.EVENT_CONNECT) {
            println("${mSocket.hashCode()} Connected")

            if (runningTasks.isNotEmpty()) {
                runningTasks.forEach { task ->
                    println("task ${task.hashCode()} is re triggered")
                    task.invoke()
                }
            }

            if (lazyInvokedTasks.isNotEmpty()) {
                lazyInvokedTasks.forEach { task ->
                    println("task ${task.hashCode()} is done")
                    task.invoke()
                    runningTasks.add(task)
                }
                lazyInvokedTasks.clear()
            }
        }
        mSocket.on(Socket.EVENT_DISCONNECT) {
            println("${mSocket.hashCode()} disconnected")
        }
    }

    fun listen(event: String, onReceive: (JSONObject) -> Unit) {
        mSocket.on(event) { args ->
            println("task")
            val task: () -> Unit = {
                val jsonString = args[0] as JSONObject
                println(jsonString)
                onReceive(jsonString)
            }

            validate(task)
        }
    }

    fun sendMessage(eventName: String, message: String) {
        val task: () -> Unit = {
            println(mSocket.connected())
            mSocket.emit(eventName, message)
        }
        validate(task)
    }

    fun disconnect() {
        mSocket.disconnect()
    }

    private fun validate(task: () -> Unit) {
        if (isConnected()) {
            println("task ${task.hashCode()} is done")
            task.invoke()
        } else {
            lazyInvokedTasks.add(task)
        }
    }

    fun isConnected() = mSocket.connected()
}
