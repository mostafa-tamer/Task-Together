package com.mostafatamer.tasktogether.presentation.authentication

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mostafatamer.tasktogether.HorizontalPadding
import com.mostafatamer.tasktogether.presentation.authentication.view_model.SignUpViewModel
import com.mostafatamer.tasktogether.presentation.components.ImagePicker
import com.mostafatamer.tasktogether.presentation.components.NormalTextField
import com.mostafatamer.tasktogether.presentation.components.PasswordTextField
import com.mostafatamer.tasktogether.presentation.components.VerticalSpacer
import com.mostafatamer.tasktogether.presentation.navigation.MainNavRoutes
import com.mostafatamer.tasktogether.showToast


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(viewModel: SignUpViewModel, navHostController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Sign Up", fontWeight = FontWeight.Bold)
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = HorizontalPadding)
                .padding(horizontal = HorizontalPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Body(viewModel, navHostController)
        }
    }
}


@Composable
private fun Body(
    viewModel: SignUpViewModel,
    navHostController: NavHostController,
) {
    val nickname = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    Column(Modifier.verticalScroll(rememberScrollState())) {
        var imageUri by remember { mutableStateOf<Uri?>(null) }

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            ImagePicker {
                imageUri = it
            }
        }
        VerticalSpacer(heightDp = 16)
        NormalTextField(username, "Username", "Enter your username")
        VerticalSpacer(heightDp = 16)
        NormalTextField(nickname, "Nickname", "Enter your nickname")
        VerticalSpacer(heightDp = 16)
        PasswordTextField(password, "Password", "Enter your password")
        VerticalSpacer(heightDp = 16)
        PasswordTextField(confirmPassword, "Confirm Password", "Confirm your password")
        VerticalSpacer(heightDp = 32)

        val context = LocalContext.current



        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                if (listOf(
                        username.value,
                        password.value,
                        confirmPassword.value,
                        nickname.value
                    ).all { it.isNotEmpty() }
                ) {

                    if (username.value.contains(' '))
                    {
                        showToast(context,"Username can not contain spaces")
                    }


                    if (password.value == confirmPassword.value) {

                        if (password.value.length >= 8) {
                            viewModel.signUp(
                                username.value,
                                password.value,
                                nickname.value,
                                imageUri
                            ) {
                                if (it) {
                                    navHostController.navigate(
                                        MainNavRoutes.MainScreen.route
                                    )
                                } else {
                                    showToast(context, "Authentication failed")
                                }
                            }
                        } else {
                            showToast(context, "Password must be 8 or more characters")
                        }

                    } else {
                        showToast(context, "Passwords do not match")
                    }
                }
            }
        ) {
            Text(text = "Sign Up")
        }
        VerticalSpacer(heightDp = 8)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = "Already have an account? ")
            Text(
                modifier = Modifier.clickable {
                    navHostController.navigate(MainNavRoutes.Login.route) {
                        popUpTo(MainNavRoutes.SignUp.route) {
                            inclusive = true
                        }
                    }
                },
                text = "Login", color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    textDecoration = TextDecoration.Underline,
                )
            )
        }
    }
}


