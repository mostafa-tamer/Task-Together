package com.mostafatamer.tasktogether.domain.model;

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("_id")
    val id: String = "",
    val nickname: String = "",
    val username: String = "",
) {
    var photo: String? = null
}
