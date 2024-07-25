package com.mostafatamer.tasktogether.domain.model

import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("_id")
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var members: List<User>? = null,
    var photo: String? = null,
    var adminUsername: String? = null
)