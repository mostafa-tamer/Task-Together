package com.mostafatamer.tasktogether.domain.model

import com.google.gson.annotations.SerializedName

data class GroupInvitation(
    @SerializedName("description")
    val message: String?,
) {
    @SerializedName("_id")
    var id: String? = null
    var group: Group? = null
}
