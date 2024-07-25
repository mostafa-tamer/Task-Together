package com.mostafatamer.tasktogether.data.repositories;

import com.mostafatamer.tasktogether.data.remote.api_service.AuthenticationApiService
import com.mostafatamer.tasktogether.domain.model.AuthRequest
import com.mostafatamer.tasktogether.domain.model.AuthResponse
import com.mostafatamer.tasktogether.domain.model.Response
import com.mostafatamer.tasktogether.lib.CallDecorator
import okhttp3.MultipartBody


class AuthenticationRepository(private val apiService: AuthenticationApiService) {
    fun login(authRequest: AuthRequest): CallDecorator<Response<AuthResponse>> {
        return CallDecorator(
            apiService.login(
                authRequest
            )
        )
    }

    fun signUp(authRequest: AuthRequest): CallDecorator<Response<AuthResponse>> {
        return CallDecorator(apiService.signUp(authRequest))
    }

    fun signUp(
        username: MultipartBody.Part,
        password: MultipartBody.Part,
        nickname: MultipartBody.Part,
        image: MultipartBody.Part?,
    ): CallDecorator<Response<AuthResponse>> {
        return CallDecorator(apiService.signUp(username, password, nickname, image))
    }
}
