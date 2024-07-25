package com.mostafatamer.tasktogether.domain.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Task(
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("assignedMember")
    val assignedMemberId: String = "",
    val title: String = "",
    val description: String = "",
    val deadline: Date = Date(0),
    val weight: Int = 0,
    var completedDate: Date? = null,
)