package com.mostafatamer.tasktogether.domain.model

import com.google.gson.annotations.SerializedName
import java.util.Date


data class Message(
    @SerializedName("_id")
    val id: String,
    val group: String,
    val content: String,
    val sender: User,
    val timestamp: Date,
)
