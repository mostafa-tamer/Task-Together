package com.mostafatamer.tasktogether.domain.model

class AuthRequest(
    val username: String,
    val password: String,
) {
    var nickname: String? = null
}