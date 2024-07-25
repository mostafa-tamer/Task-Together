package com.mostafatamer.tasktogether.presentation.authentication.view_model

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.mostafatamer.tasktogether.SharedPreferencesConstants
import com.mostafatamer.tasktogether.data.remote.RetrofitInstance
import com.mostafatamer.tasktogether.data.repositories.AuthenticationRepository
import com.mostafatamer.tasktogether.domain.model.AuthRequest
import com.mostafatamer.tasktogether.domain.model.User
import com.mostafatamer.tasktogether.lib.SharedPreferencesHelper
import com.mostafatamer.tasktogether.presentation.authentication.appUser.AppUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject





@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sharedPreferencesConstantsHelper: SharedPreferencesHelper,
    private val authenticationRepository: AuthenticationRepository,
) : ViewModel() {


    fun login(user: AuthRequest, onSucceed: (Boolean) -> Unit) {
        authenticationRepository.login(user)
            .setOnSuccess {
                it?.data?.let { authResponse ->
                    RetrofitInstance.setUserToken(authResponse.token)
                    sharedPreferencesConstantsHelper.setValue(
                        SharedPreferencesConstants.Authentication.USER_TOKEN,
                        authResponse.token
                    )

                    AppUser =
                        User(username = authResponse.username, nickname = authResponse.nickname)

                    sharedPreferencesConstantsHelper.setValue(
                        SharedPreferencesConstants.Authentication.USER,
                        Gson().toJson(AppUser)
                    )
                }
                onSucceed.invoke(it?.data != null)
            }.execute()
    }
}