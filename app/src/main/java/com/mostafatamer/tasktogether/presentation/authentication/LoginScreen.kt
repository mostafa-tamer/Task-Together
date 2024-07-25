package com.mostafatamer.tasktogether.presentation.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mostafatamer.tasktogether.HorizontalPadding
import com.mostafatamer.tasktogether.domain.model.AuthRequest
import com.mostafatamer.tasktogether.presentation.authentication.view_model.LoginViewModel
import com.mostafatamer.tasktogether.presentation.components.NormalTextField
import com.mostafatamer.tasktogether.presentation.components.PasswordTextField
import com.mostafatamer.tasktogether.presentation.components.VerticalSpacer
import com.mostafatamer.tasktogether.presentation.navigation.MainNavRoutes
import com.mostafatamer.tasktogether.showToast


@Composable
fun LoginScreen(viewModel: LoginViewModel, navHostController: NavHostController) {
    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp
            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
            val scrollState = rememberScrollState()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Top(screenHeight, screenWidth)
                Box(
                    Modifier
                        .padding(horizontal = HorizontalPadding)
                        .padding(horizontal = HorizontalPadding),
                ) {
                    Body(viewModel, navHostController)
                }
            }
        }
    }
}

@Composable
private fun Top(
    screenHeight: Dp,
    screenWidth: Dp,
) {
    Box {
        Card(
            modifier = Modifier
                .height(screenHeight / 4f)
                .width(screenWidth)
                .scale(scaleY = 1f, scaleX = 1.5f),
            shape = RoundedCornerShape(
                bottomEnd = screenWidth,
                bottomStart = screenWidth
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        Text(
            text = "Task Together",
            Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun Body(
    viewModel: LoginViewModel,
    navHostController: NavHostController,
) {
    Column {
        Text(
            text = "Welcome",
            Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
        )
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }

        VerticalSpacer(heightDp = 32)
        NormalTextField(username, "Username", "Enter your username")
        VerticalSpacer(heightDp = 16)
        PasswordTextField(password, "Password", "Enter your password")
        VerticalSpacer(heightDp = 32)

        val context = LocalContext.current


        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (listOf(username.value, password.value).all { it.isNotEmpty() }) {
                    viewModel.login(
                        AuthRequest(
                            username.value,
                            password.value
                        )
                    ) {
                        if (it) {
                            navHostController.navigate(
                                MainNavRoutes.MainScreen.route
                            )
                        } else {
                            showToast(context, "Authentication failed")
                        }
                    }
                }
            }
        ) {
            Text(text = "Login")
        }
        VerticalSpacer(heightDp = 8)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = "Don't have an account? ")
            Text(
                modifier = Modifier.clickable {
                    navHostController.navigate(MainNavRoutes.SignUp.route) {
                        popUpTo(MainNavRoutes.Login.route) {
                            inclusive = true
                        }
                    }
                },
                text = "Sign Up", color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    textDecoration = TextDecoration.Underline,
                )
            )
        }
    }
}




