package com.mostafatamer.tasktogether.presentation.authentication.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.mostafatamer.tasktogether.HttpStatusCode
import com.mostafatamer.tasktogether.SharedPreferencesConstants
import com.mostafatamer.tasktogether.data.remote.RetrofitInstance
import com.mostafatamer.tasktogether.data.repositories.AuthenticationRepository
import com.mostafatamer.tasktogether.data.repositories.ImageRepository
import com.mostafatamer.tasktogether.domain.model.User
import com.mostafatamer.tasktogether.lib.SharedPreferencesHelper
import com.mostafatamer.tasktogether.match
import com.mostafatamer.tasktogether.presentation.authentication.appUser.AppUser
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val sharedPreferencesConstantsHelper: SharedPreferencesHelper,
    private val authenticationRepository: AuthenticationRepository,
    private val imageRepository: ImageRepository,
) : ViewModel() {

    fun signUp(
        username: String,
        password: String,
        nickname: String,
        imageUri: Uri?,
        onSucceed: (Boolean) -> Unit,
    ) {

        val userPart = MultipartBody.Part.createFormData("username", username)
        val passwordPart = MultipartBody.Part.createFormData("password", password)
        val nicknamePart = MultipartBody.Part.createFormData("nickname", nickname)
        val imagePart = imageUri?.let {
            imageRepository.getImageMultiPart(
                "photo",
                "image", it
            )
        }


        authenticationRepository.signUp(userPart, passwordPart, nicknamePart, imagePart)
            .setOnResponse { response, code ->

                response?.data?.let { authResponse ->
                    RetrofitInstance.setUserToken(authResponse.token)
                    AppUser =
                        User(username = authResponse.username, nickname = authResponse.nickname)

                    sharedPreferencesConstantsHelper.setValue(
                        SharedPreferencesConstants.Authentication.USER_TOKEN,
                        authResponse.token
                    )
                    sharedPreferencesConstantsHelper.setValue(
                        SharedPreferencesConstants.Authentication.USER,
                        Gson().toJson(AppUser)
                    )
                }
                onSucceed.invoke(HttpStatusCode.CREATED.match(code))
            }.execute()
    }
}

