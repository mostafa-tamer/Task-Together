package com.mostafatamer.tasktogether.di

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.mostafatamer.tasktogether.SharedPreferencesConstants
import com.mostafatamer.tasktogether.SocketManager
import com.mostafatamer.tasktogether.StompRepository
import com.mostafatamer.tasktogether.data.remote.RetrofitInstance
import com.mostafatamer.tasktogether.data.remote.api_service.AuthenticationApiService
import com.mostafatamer.tasktogether.data.remote.api_service.GroupsApiService
import com.mostafatamer.tasktogether.data.remote.api_service.ProjectApiService
import com.mostafatamer.tasktogether.data.repositories.AuthenticationRepository
import com.mostafatamer.tasktogether.data.repositories.GroupsRepository
import com.mostafatamer.tasktogether.data.repositories.ImageRepository
import com.mostafatamer.tasktogether.data.repositories.ProjectRepository
import com.mostafatamer.tasktogether.lib.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Named("token")
    fun provideToken(application: Application): String {
        val sharedPreferencesConstantsHelper =
            SharedPreferencesHelper(application, SharedPreferencesConstants.Authentication.name)
        return sharedPreferencesConstantsHelper.getString(SharedPreferencesConstants.Authentication.USER_TOKEN)!!
    }

    @Provides
    @Singleton
    @Named("ws_url")
    fun provideWsUrl(): String {
        return "wss://task-together-2020.onrender.com"
    }

    @Provides
    fun provideStompService(stompClient: StompClient, gson: Gson): StompRepository {
        return StompRepository(stompClient, gson)
    }

    @Provides
    fun provideImageRepository(application: Application): ImageRepository {
        return ImageRepository(application)
    }

    @Provides
    fun provideSocketManager(
        @Named("token") token: String,
    ): SocketManager {
        return SocketManager(token)
    }

    @Provides
    fun provideStompClient(
        @Named("ws_url") wsUrl: String,
        @Named("token") token: String,
    ): StompClient {
        return Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            wsUrl,
            mutableMapOf("authorization" to token)
        )
    }

    @Provides
    fun provideLoginSharedPreferencesRepository(application: Application): SharedPreferencesHelper {
        return SharedPreferencesHelper(application, SharedPreferencesConstants.Authentication.name)
    }

    @Provides
    fun providesProjectRepository(): ProjectRepository {
        val apiService = RetrofitInstance.retrofit.create(ProjectApiService::class.java)
        return ProjectRepository(apiService)
    }


    @Provides
    fun providesAuthenticationRepository(): AuthenticationRepository {
        val apiService = RetrofitInstance.retrofit.create(AuthenticationApiService::class.java)
        return AuthenticationRepository(apiService)
    }

    @Provides
    fun providesGroupRepository(): GroupsRepository {
        val apiService = RetrofitInstance.retrofit.create(GroupsApiService::class.java)
        return GroupsRepository(apiService)
    }


    @Provides
    @Singleton
    fun provideGson(): Gson {
        val dateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }

        return GsonBuilder()
            .registerTypeAdapter(Date::class.java, JsonSerializer<Date> { src, _, _ ->
                JsonPrimitive(dateFormat.format(src))
            }).registerTypeAdapter(Date::class.java, JsonDeserializer { json, _, _ ->
                dateFormat.parse(json.asString)
            }).create()
    }
}