package com.mostafatamer.tasktogether.domain.model;

import com.google.gson.annotations.SerializedName


data class Announcement(
    @SerializedName("_id")
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var createdBy: User? = null,
    val editable: Boolean? = null
)