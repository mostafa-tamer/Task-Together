package com.mostafatamer.tasktogether

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.mostafatamer.tasktogether.data.remote.RetrofitInstance
import com.mostafatamer.tasktogether.presentation.navigation.NavHostInitializer
import dagger.hilt.android.AndroidEntryPoint

const val SERVER_URL = "https://task-together-2020.onrender.com"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navHostController = rememberNavController()

            AppTheme {
                Surface {
                    RetrofitInstance.setBaseUrl(SERVER_URL)


//                    val scaffoldState = rememberBottomSheetScaffoldState()

//                    val scope = rememberCoroutineScope()


//                    BottomSheetScaffold(
//                        scaffoldState = scaffoldState,
//                        sheetContent = {
//                            Text(text = "Mostafa ثفغيبلTamer")
//                        },
//                        sheetPeekHeight = 0.dp,
//                    ) {
//                    }

//                    val sheetState = rememberModalBottomSheetState()

//                    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
//
//
//
//
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Button(onClick = {
//                            isSheetOpen = true
//
//                        }) {
//                            Text(text = "Open sheet")
//                        }
//                    }
//
//
//                    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
//                    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
//                    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
//
//                    if (isSheetOpen) {
//                        ModalBottomSheet(
//                            sheetState = state,
//                            onDismissRequest = {
//                                isSheetOpen = false
//                            },
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .height(screenHeight)
//                                    .width(screenWidth)
//                                    .background(Color.Red)
//                            )
//                        }
//                    }


                    NavHostInitializer(
                        navHostController = navHostController
                    )
                }
            }
        }
    }
}


