package com.mostafatamer.tasktogether.presentation.main_screen.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mostafatamer.tasktogether.presentation.components.BottomNavigationBar
import com.mostafatamer.tasktogether.presentation.components.NavBarItems
import com.mostafatamer.tasktogether.presentation.main_screen.GroupInvitationScreen
import com.mostafatamer.tasktogether.presentation.main_screen.GroupsScreen
import com.mostafatamer.tasktogether.presentation.main_screen.view_model.GroupInvitationViewModel
import com.mostafatamer.tasktogether.presentation.viewModels.GroupsViewModel


@Composable
fun MainScreenNavGraph(navHostController: NavHostController) {

    val mainScreenNavHostController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navHostController = mainScreenNavHostController,
                NavBarItems.mainScreenNavBar
            )
        }
    ) {
        Box(Modifier.padding(it)) {
            NavHost(
                mainScreenNavHostController,
                startDestination = MainScreenNavRouts.Groups.route
            ) {
                composable(
                    route = MainScreenNavRouts.Groups.route
                ) {
                    val viewModel = hiltViewModel<GroupsViewModel>()
                    GroupsScreen(viewModel, navHostController)
                }

                composable(route = MainScreenNavRouts.GroupInvitation.route) {
                    val viewModel = hiltViewModel<GroupInvitationViewModel>()
                    GroupInvitationScreen(viewModel)
                }
            }
        }
    }


}