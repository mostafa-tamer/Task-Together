package com.mostafatamer.tasktogether.domain.model

data class MessagePage(
    val messages: List<Message>,
    val isLast: Boolean,
    val totalMessages: Int,
)
