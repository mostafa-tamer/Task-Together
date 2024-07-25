package com.mostafatamer.tasktogether.data.remote

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitInstance {
    private var apiBaseUrl: String? = null
    private var userToken: String? = null
    fun setBaseUrl(baseUrl: String) {
        apiBaseUrl = baseUrl
    }

    fun setUserToken(token: String) {
        userToken = token
    }

    val retrofit: Retrofit by lazy {
//        requireNotNull(apiBaseUrl) { "Base URL must be set before creating Retrofit instance" }
//        requireNotNull(userToken) { "User Token must be set before creating Retrofit instance" }

        Retrofit.Builder()
            .baseUrl(apiBaseUrl!!)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor { chain ->
                        val chainRequestBuilder: Request.Builder = chain.request().newBuilder()
                        validateHeader(chainRequestBuilder, "authorization", userToken)
                        chain.proceed(chainRequestBuilder.build())
                    }
                    .build()
            ).build()
    }

    private fun validateHeader(builder: Request.Builder, key: String, value: String?) {
        if (value != null) {
            builder.header(key, value)
        }
    }
}