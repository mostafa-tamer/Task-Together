package com.mostafatamer.tasktogether.presentation.project_screen.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mostafatamer.tasktogether.DefaultShape
import com.mostafatamer.tasktogether.R
import com.mostafatamer.tasktogether.domain.model.Group
import com.mostafatamer.tasktogether.domain.model.Project
import com.mostafatamer.tasktogether.presentation.authentication.appUser.AppUser
import com.mostafatamer.tasktogether.presentation.components.ActionBar
import com.mostafatamer.tasktogether.presentation.components.ActionBarLabel
import com.mostafatamer.tasktogether.presentation.components.BottomNavigationBar
import com.mostafatamer.tasktogether.presentation.components.FastAlertDialog
import com.mostafatamer.tasktogether.presentation.components.NavBarItems
import com.mostafatamer.tasktogether.presentation.components.NavigateUp
import com.mostafatamer.tasktogether.presentation.navigation.MainNavRoutes
import com.mostafatamer.tasktogether.presentation.project_screen.MyTasksScreen
import com.mostafatamer.tasktogether.presentation.project_screen.ProjectTasksScreen
import com.mostafatamer.tasktogether.presentation.project_screen.projectDashboardScreen.ProjectDashboardScreen
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.MyTasksViewModel
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.ProjectDashboardViewModel
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.ProjectManagementViewModel
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.ProjectTasksViewModel
import com.mostafatamer.tasktogether.showToast


@Composable
fun ProjectNavHost(mainNavHostController: NavHostController, project: Project, group: Group) {
    val currentNavHostController = rememberNavController()


    Scaffold(
        topBar = {
            val projectManagementViewModel = hiltViewModel<ProjectManagementViewModel>()
            projectManagementViewModel.init(project, group)
            ScreenActionBar(projectManagementViewModel, mainNavHostController)
        },
        bottomBar = {
            BottomNavigationBar(
                navHostController = currentNavHostController,
                NavBarItems.topicNavBar
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            NavHost(
                mainNavHostController, currentNavHostController, group, project
            )
        }
    }
}

@Composable
private fun NavHost(
    mainNavHostController: NavHostController,
    currentNavHostController: NavHostController,
    group: Group,
    project: Project,
) {


    NavHost(
        navController = currentNavHostController,
        startDestination = TopicNavRoutes.MyTasks.route
    ) {
        composable(route = TopicNavRoutes.MyTasks.route) {
            val viewModel = hiltViewModel<MyTasksViewModel>()
            viewModel.init(project, group)
            MyTasksScreen(viewModel, currentNavHostController)
        }
        composable(route = TopicNavRoutes.Dashboard.route) {
            val viewModel = hiltViewModel<ProjectDashboardViewModel>()
            viewModel.init(project, group)
            ProjectDashboardScreen(viewModel, currentNavHostController)
        }
        composable(route = TopicNavRoutes.ProjectTasks.route) {
            val viewModel = hiltViewModel<ProjectTasksViewModel>()
            viewModel.init(project, group)
            ProjectTasksScreen(viewModel, currentNavHostController, mainNavHostController)
        }
    }
}


@Composable
private fun ScreenActionBar(
    viewModel: ProjectManagementViewModel,
    navHostController: NavHostController,
) {
    var projectName by remember { mutableStateOf(viewModel.project.title) }

    ActionBar(
        label = {
            ActionBarLabel(text = projectName)
        },
        prefixComposable = {
            NavigateUp(navHostController)
        },
        suffixComposable = {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                val showProjectDeleteAlertDialog = remember { mutableStateOf(false) }
                val showProjectUpdateAlertDialog = remember { mutableStateOf(false) }

                val name = remember { mutableStateOf(viewModel.project.title) }
                val description = remember { mutableStateOf(viewModel.project.description) }

                val context = LocalContext.current
                FastAlertDialog(
                    showAlertDialog = showProjectUpdateAlertDialog,
                    title = "Update Project", body = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            OutlinedTextField(
                                value = name.value,
                                onValueChange = {
                                    if (it.length <= 15) {
                                        name.value = it
                                    }
                                },
                                label = { Text("Name") },
                                maxLines = 1,
                                modifier = Modifier.fillMaxWidth(),
                                shape = DefaultShape
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                minLines = 3, maxLines = 3,
                                value = description.value,
                                onValueChange = { description.value = it },
                                label = { Text("Description") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = DefaultShape
                            )
                        }
                    }
                ) {
                    viewModel.updateProject(name.value, description.value) {
                        if (it) {
                            showToast(context, "Project updated successfully")
                            projectName = name.value
                            showProjectUpdateAlertDialog.value = false
                        } else {
                            showToast(context, "Failed to update the project")
                        }
                    }
                }

                DeleteProjectDialog(showProjectDeleteAlertDialog, viewModel, navHostController)

                if (viewModel.group.adminUsername == AppUser.username) {
                    IconButton(onClick = {
                        navHostController.navigate(
                            MainNavRoutes.AddMembersToProject.withProjectAndGroup(
                                viewModel.project,
                                viewModel.group
                            )
                        )
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.group_add_svgrepo_com),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    var expanded by remember { mutableStateOf(false) }

                    Column {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            DropdownMenuItem(
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    showProjectUpdateAlertDialog.value = true
                                },
                                text = {
                                    Text(text = "Edit")
                                }
                            )

                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                    showProjectDeleteAlertDialog.value = true
                                }, trailingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.delete_clipboard_svgrepo_com),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }, text = {
                                    Text(text = "Delete")
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun DeleteProjectDialog(
    showGroupDeleteAlertDialog: MutableState<Boolean>,
    viewModel: ProjectManagementViewModel,
    navHostController: NavHostController,
) {
    val context = LocalContext.current

    FastAlertDialog(
        showAlertDialog = showGroupDeleteAlertDialog,
        title = "Delete Project",
        positiveText = "Delete",
        text = "Are you sure you want to delete this project?\n${viewModel.project.title} will be deleted",
    ) {
        viewModel.deleteProject {
            if (it) {
                showToast(context, "Project deleted successfully")
                navHostController.navigateUp()
            } else {
                showToast(context, "Failed to delete the project")
            }
        }
    }
}