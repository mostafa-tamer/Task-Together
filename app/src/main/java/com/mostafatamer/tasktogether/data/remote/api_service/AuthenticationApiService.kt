package com.mostafatamer.tasktogether.data.remote.api_service

import com.mostafatamer.tasktogether.domain.model.AuthRequest
import com.mostafatamer.tasktogether.domain.model.AuthResponse
import com.mostafatamer.tasktogether.domain.model.Response
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthenticationApiService {
    @POST("/login")
    fun login(@Body authRequest: AuthRequest): Call<Response<AuthResponse>>

    @POST("/signup")
    fun signUp(@Body authRequest: AuthRequest): Call<Response<AuthResponse>>

    @Multipart
    @POST("/signup")
    fun signUp(
        @Part username: MultipartBody.Part,
        @Part password: MultipartBody.Part,
        @Part nickname: MultipartBody.Part,
        @Part image: MultipartBody.Part?,
    ): Call<Response<AuthResponse>>
}