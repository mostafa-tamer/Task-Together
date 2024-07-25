package com.mostafatamer.tasktogether.domain.model;

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Project(
    @SerializedName("_id")
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    var startDate: Date = Date(),
    var deadline: Date = Date(),
//    var colleagues: MutableList<Colleague>? = null,
//    var projectStatistics: ProjectStatistics? = null
)




