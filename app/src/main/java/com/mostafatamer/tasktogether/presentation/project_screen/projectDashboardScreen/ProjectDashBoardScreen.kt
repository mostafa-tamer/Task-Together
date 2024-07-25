package com.mostafatamer.tasktogether.presentation.project_screen.projectDashboardScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.mostafatamer.tasktogether.presentation.components.FastSwipe
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.ProjectDashboardViewModel


@Composable
fun ProjectDashboardScreen(
    viewModel: ProjectDashboardViewModel,
    navHostController: NavHostController,
) {

    LaunchedEffect(Unit) {
        viewModel.launchRemainingTimeTimer()
        viewModel.getStatistics()
        viewModel.getColleagues()
    }

    FastSwipe(onRefresh = {
        viewModel.launchRemainingTimeTimer()
        viewModel.getStatistics {
            viewModel.getColleagues {
                it()
            }
        }
    }) {
        Column(
            Modifier
//                .padding(HorizontalPadding)
//                .padding(vertical = 4.dp)
                .fillMaxSize()
        ) {
            DashBoard(viewModel)
        }
        LazyColumn(Modifier.fillMaxSize()) { }
    }
}



