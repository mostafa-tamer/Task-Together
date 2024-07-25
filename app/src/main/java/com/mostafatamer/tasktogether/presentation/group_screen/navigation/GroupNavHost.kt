package com.mostafatamer.tasktogether.presentation.group_screen.navigation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mostafatamer.tasktogether.DefaultShape
import com.mostafatamer.tasktogether.R
import com.mostafatamer.tasktogether.RealtimeLifeCycle
import com.mostafatamer.tasktogether.domain.model.Group
import com.mostafatamer.tasktogether.presentation.authentication.appUser.AppUser
import com.mostafatamer.tasktogether.presentation.components.ActionBar
import com.mostafatamer.tasktogether.presentation.components.BottomNavigationBar
import com.mostafatamer.tasktogether.presentation.components.FastAlertDialog
import com.mostafatamer.tasktogether.presentation.components.HorizontalSpacer
import com.mostafatamer.tasktogether.presentation.components.ImagePicker
import com.mostafatamer.tasktogether.presentation.components.NavBarItems
import com.mostafatamer.tasktogether.presentation.group_screen.AnnouncementsScreen
import com.mostafatamer.tasktogether.presentation.group_screen.GroupChatScreen
import com.mostafatamer.tasktogether.presentation.group_screen.GroupOverviewScreen
import com.mostafatamer.tasktogether.presentation.group_screen.ProjectsScreen
import com.mostafatamer.tasktogether.presentation.group_screen.viewModels.AnnouncementViewModel
import com.mostafatamer.tasktogether.presentation.group_screen.viewModels.GroupChatViewModel
import com.mostafatamer.tasktogether.presentation.group_screen.viewModels.GroupConfigViewModel
import com.mostafatamer.tasktogether.presentation.group_screen.viewModels.GroupManagerViewModel
import com.mostafatamer.tasktogether.presentation.group_screen.viewModels.GroupViewModel
import com.mostafatamer.tasktogether.presentation.group_screen.viewModels.ProjectsViewModel
import com.mostafatamer.tasktogether.showToast


@Composable
fun GroupNavHost(mainNavHostController: NavHostController, group: Group) {
    val groupNavHostController = rememberNavController()

    val groupName = remember { mutableStateOf(group.name ?: "") }

    Scaffold(
        topBar = {
            val viewModel = hiltViewModel<GroupManagerViewModel>()
            viewModel.init(group)
            ActionBar(viewModel, mainNavHostController, groupName)
        },
        bottomBar = {
            BottomNavigationBar(
                navHostController = groupNavHostController,
                NavBarItems.groupNavBar,
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            NavHost(mainNavHostController, groupNavHostController, group)
        }
    }
}

@Composable
private fun NavHost(
    mainNavHostController: NavHostController,
    groupNavHostController: NavHostController,
    group: Group,
) {
    NavHost(
        navController = groupNavHostController,
        startDestination = GroupNavRoutes.Projects.route
    ) {

        composable(route = GroupNavRoutes.Projects.route) {
            val viewModel = hiltViewModel<ProjectsViewModel>()
            viewModel.init(group)
            ProjectsScreen(viewModel = viewModel, mainNavHostController = mainNavHostController)
        }

        composable(route = GroupNavRoutes.Announcements.route) {
            val viewModel = hiltViewModel<AnnouncementViewModel>()
            viewModel.init(group)
            AnnouncementsScreen(viewModel, groupNavHostController)
        }

        composable(route = GroupNavRoutes.GroupOverview.route) {
            val viewModel = hiltViewModel<GroupConfigViewModel>()
            viewModel.init(group)
            GroupOverviewScreen(viewModel)
        }

        composable(route = GroupNavRoutes.Chat.route) {
            val viewModel = hiltViewModel<GroupChatViewModel>()
            viewModel.init(group)
            RealtimeLifeCycle(viewModel.stompLifecycleManager)
            GroupChatScreen(viewModel)
        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
private fun NavigateUp(
    viewModel: GroupViewModel,
    navHostController: NavHostController,
    navigateUpEvent: () -> Unit = { navHostController.navigateUp() },
) {
    Surface(
        modifier = Modifier.background(MaterialTheme.colorScheme.primary),
        shape = CircleShape
    ) {
        Box(
            Modifier
                .clickable {
                    navigateUpEvent.invoke()
                }
                .background(MaterialTheme.colorScheme.primary)
                .padding(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                HorizontalSpacer(widthDp = 4)
                Surface(
                    modifier = Modifier.size(36.dp), shape = CircleShape
                ) {
                    Image(
                        modifier = Modifier.size(50.dp),
                        painter = rememberImagePainter(viewModel.group.photo),
                        contentDescription = null
                    )
                }
            }
        }
    }
}


@Composable
private fun DeleteGroup(
    showGroupDeleteAlertDialog: MutableState<Boolean>,
    viewModel: GroupManagerViewModel,
    navHostController: NavHostController,
) {
    val context = LocalContext.current

    FastAlertDialog(
        showAlertDialog = showGroupDeleteAlertDialog,
        title = "Delete Group",
        positiveText = "Delete",
        text = "Are you sure you want to delete this group?\n${viewModel.group.name} will be deleted",
    ) {
        viewModel.deleteGroup {
            if (it) {
                showToast(context, "Group deleted successfully")
                navHostController.navigateUp()
            } else {
                showToast(context, "Failed to delete the group")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
private fun UpdateGroup(
    showGroupDeleteAlertDialog: MutableState<Boolean>,
    viewModel: GroupManagerViewModel,
    groupName: MutableState<String>,
) {
    val context = LocalContext.current
    val name = remember { mutableStateOf(viewModel.group.name ?: "") }
    val description = remember { mutableStateOf(viewModel.group.description ?: "") }

    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val height = (LocalConfiguration.current.screenHeightDp * 0.80).dp

    if (showGroupDeleteAlertDialog.value) {
        ModalBottomSheet(
            sheetState = state,
            onDismissRequest = {
                showGroupDeleteAlertDialog.value = false
            },
        ) {
            Box(
                modifier = Modifier
                    .height(height)
                    .fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {

                    var imageUri by remember { mutableStateOf<Uri?>(null) }
                    val photo = viewModel.group.photo.let { it?.replace('\\', '/') }
              println(photo)
                    ImagePicker(rememberImagePainter(photo)) {
                        imageUri = it
                    }

                    Spacer(modifier = Modifier.height(4.dp))

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
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.updateGroup(name.value, description.value, imageUri) {
                                if (it != null) {
                                    showToast(context, "Group updated successfully")
                                    showGroupDeleteAlertDialog.value = false
                                    groupName.value = it.name ?: ""
                                } else {
                                    showToast(context, "Failed to update the group")
                                }
                            }
                        }) {
                        Text(text = "Update")
                    }
                }
            }
        }
    }
}


@Composable
private fun ActionBar(
    viewModel: GroupManagerViewModel,
    navHostController: NavHostController,
    groupName: MutableState<String>,
) {

    ActionBar(
        label = groupName.value,
        prefixComposable = {
            NavigateUp(viewModel, navHostController) {
                navHostController.navigateUp()
            }
        }, suffixComposable = {
            val showGroupDeleteAlertDialog = remember { mutableStateOf(false) }
            val showGroupUpdateAlertDialog = remember { mutableStateOf(false) }

            DeleteGroup(showGroupDeleteAlertDialog, viewModel, navHostController)
            UpdateGroup(showGroupUpdateAlertDialog, viewModel, groupName)

            Spacer(modifier = Modifier.weight(1f))

            if (viewModel.group.adminUsername == AppUser.username) {


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
                            onClick = {
                                expanded = false
                                showGroupUpdateAlertDialog.value = true
                            },
                            text = {
                                Text(text = "Edit")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text(text = "Delete")
                            },
                            onClick = {
                                expanded = false
                                showGroupDeleteAlertDialog.value = true
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.group_delete_svgrepo_com),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}