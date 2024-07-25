package com.mostafatamer.tasktogether.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mostafatamer.tasktogether.domain.model.Group
import com.mostafatamer.tasktogether.domain.model.Project
import com.mostafatamer.tasktogether.domain.model.User
import com.mostafatamer.tasktogether.jsonConverter
import com.mostafatamer.tasktogether.presentation.AddProjectMembersScreen
import com.mostafatamer.tasktogether.presentation.AddUserTaskScreen
import com.mostafatamer.tasktogether.presentation.CreateGroupScreen
import com.mostafatamer.tasktogether.presentation.JoinGroupScreen
import com.mostafatamer.tasktogether.presentation.authentication.LoginScreen
import com.mostafatamer.tasktogether.presentation.authentication.SignupScreen
import com.mostafatamer.tasktogether.presentation.authentication.view_model.LoginViewModel
import com.mostafatamer.tasktogether.presentation.authentication.view_model.SignUpViewModel
import com.mostafatamer.tasktogether.presentation.group_screen.navigation.GroupNavHost
import com.mostafatamer.tasktogether.presentation.main_screen.navigation.MainScreenNavGraph
import com.mostafatamer.tasktogether.presentation.project_screen.navigation.ProjectNavHost
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.AddMembersToProjectViewModel
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.AddUserTaskViewModel
import com.mostafatamer.tasktogether.presentation.viewModels.CreateGroupViewModel
import com.mostafatamer.tasktogether.presentation.viewModels.JoinGroupViewModel


@Composable
fun NavHostInitializer(
    navHostController: NavHostController,
) {
    NavHost(
        navController = navHostController, startDestination = MainNavRoutes.Login.route
    ) {
        composable(route = MainNavRoutes.Login.route) {

            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(viewModel = viewModel, navHostController = navHostController)
        }

        composable(route = MainNavRoutes.SignUp.route) {
            val viewModel = hiltViewModel<SignUpViewModel>()
            SignupScreen(viewModel = viewModel, navHostController = navHostController)
        }

        composable(MainNavRoutes.MainScreen.route) {
            MainScreenNavGraph(navHostController)
        }
        composable(
            route = MainNavRoutes.JoinGroup.route
        ) {
            val viewModel = hiltViewModel<JoinGroupViewModel>()
            JoinGroupScreen(viewModel, navHostController)
        }
        composable(
            route = MainNavRoutes.CreateGroup.route
        ) {
            val viewModel = hiltViewModel<CreateGroupViewModel>()

            CreateGroupScreen(viewModel, navHostController)
        }

        composable(
            route = MainNavRoutes.GroupMainNavHost.route
        ) {
            val groupJsonString = it.arguments?.getString(GROUP)
            val group = jsonConverter.fromJson(groupJsonString, Group::class.java)
            GroupNavHost(navHostController, group)
        }

        composable(
            route = MainNavRoutes.ProjectNavRoute.route
        ) {
            val groupJsonString = it.arguments?.getString(GROUP)
            val topicJsonString = it.arguments?.getString(PROJECT)

            val group = jsonConverter.fromJson(groupJsonString, Group::class.java)
            val project = jsonConverter.fromJson(topicJsonString, Project::class.java)

            ProjectNavHost(mainNavHostController = navHostController, project, group)
        }

//        composable(route = MainNavRoutes.CreateTopic.route) {
//            val groupJsonString = it.arguments?.getString(GROUP)
//            val group = jsonConverter.fromJson(groupJsonString, Group::class.java)
//            val viewModel by remember { mutableStateOf(CreateProjectViewModel(group)) }
//            CreateTopicScreen(viewModel, navHostController)
//        }
        composable(route = MainNavRoutes.AddMembersToProject.route) {
            val projectJsonString = it.arguments?.getString(PROJECT)
            val groupJsonString = it.arguments?.getString(GROUP)
            val project = jsonConverter.fromJson(projectJsonString, Project::class.java)
            val group = jsonConverter.fromJson(groupJsonString, Group::class.java)
            val viewModel = hiltViewModel<AddMembersToProjectViewModel>()
            viewModel.init(project, group)
            AddProjectMembersScreen(viewModel, navHostController)
        }

//        composable(route = MainNavRoutes.ProjectInfo.route) {
//            val project = it.arguments?.let { bundle -> getProject(bundle.getString(PROJECT)!!) }!!
//            val viewModel by remember { mutableStateOf(ProjectInfoViewModel(project)) }
//            ProjectMembersScreen(viewModel, navHostController)
//        }

        composable(route = MainNavRoutes.AddUserTasks.route) {
            val project =
                jsonConverter.fromJson(it.arguments!!.getString(PROJECT), Project::class.java)
            val user =
                jsonConverter.fromJson(it.arguments!!.getString(USER), User::class.java)
            val viewModel = hiltViewModel<AddUserTaskViewModel>()

            viewModel.init(user, project, Group())
            AddUserTaskScreen(viewModel, navHostController)
        }
    }
}

