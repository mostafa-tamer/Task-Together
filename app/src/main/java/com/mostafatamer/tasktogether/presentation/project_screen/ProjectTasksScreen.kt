package com.mostafatamer.tasktogether.presentation.project_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mostafatamer.tasktogether.DefaultShape
import com.mostafatamer.tasktogether.HorizontalPadding
import com.mostafatamer.tasktogether.R
import com.mostafatamer.tasktogether.SpacerBetweenCards
import com.mostafatamer.tasktogether.TopPadding
import com.mostafatamer.tasktogether.domain.model.Colleague
import com.mostafatamer.tasktogether.domain.model.Task
import com.mostafatamer.tasktogether.formatDate
import com.mostafatamer.tasktogether.presentation.authentication.appUser.AppUser
import com.mostafatamer.tasktogether.presentation.components.FastSwipe
import com.mostafatamer.tasktogether.presentation.components.HorizontalSpacer
import com.mostafatamer.tasktogether.presentation.components.TitleBar
import com.mostafatamer.tasktogether.presentation.components.TrailingVerticalSpacer
import com.mostafatamer.tasktogether.presentation.components.VerticalSpacer
import com.mostafatamer.tasktogether.presentation.navigation.MainNavRoutes
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.ProjectTasksViewModel
import com.mostafatamer.tasktogether.showToast
import com.mostafatamer.tasktogether.ui.theme.myColors.DarkGreen
import java.util.Date


@Composable
fun ProjectTasksScreen(
    viewModel: ProjectTasksViewModel,
    currentNavHostController: NavHostController,
    mainNavHostController: NavHostController,
) {
    LaunchedEffect(Unit) {
        viewModel.getColleagues()
    }
    FastSwipe(onRefresh = {
        viewModel.getColleagues { it() }
    }) {
        Box(
            modifier = Modifier
                .padding(horizontal = HorizontalPadding)
                .padding(top = TopPadding)
        ) {
            Column {
                TitleBar(text = "Tasks")
                ColleaguesList(viewModel, mainNavHostController)
            }
        }
    }
}

@Composable
fun ColleaguesList(viewModel: ProjectTasksViewModel, mainNavHostController: NavHostController) {
    Column {
        Card(
            shape = DefaultShape,
            colors = CardColors(
                Color.Transparent, Color.Transparent, Color.Transparent, Color.Transparent
            )
        ) {
            LazyColumn {
                items(viewModel.colleagues) { colleague ->
                    ColleagueCard(viewModel, colleague, mainNavHostController)
                    TrailingVerticalSpacer(list = viewModel.colleagues, item = colleague)
                }
                item {
                    SpacerBetweenCards()
                }
            }
        }
    }
}

@Composable
fun ColleagueCard(
    viewModel: ProjectTasksViewModel,
    colleague: Colleague,
    mainNavHostController: NavHostController,
) {
    Card(
        shape = DefaultShape,
        modifier = Modifier.fillMaxWidth()
    ) {
        val isExpanded = remember { mutableStateOf(false) }
        Box(
            Modifier
                .clickable { isExpanded.value = !isExpanded.value }
                .padding(4.dp)
        ) {
            Column {
                ColleagueCardHeader(viewModel, colleague, isExpanded, mainNavHostController)

                AnimatedVisibility(
                    visible = isExpanded.value,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    VerticalSpacer(heightDp = 16)

                    LazyColumn(modifier = Modifier.heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.5).dp)) {
                        items(colleague.tasks) { task ->
                            TaskCard(task, viewModel)
                            TrailingVerticalSpacer(list = colleague.tasks, item = task, 8)
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ColleagueCardHeader(
    viewModel: ProjectTasksViewModel,
    colleague: Colleague,
    isExpanded: MutableState<Boolean>,
    mainNavHostController: NavHostController,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val showRemoveMemberDialog = remember { mutableStateOf(false) }

                RemoveMember(showRemoveMemberDialog, viewModel, colleague)



                if (AppUser.username == viewModel.group.adminUsername) {
                    IconButton(onClick = {
                        mainNavHostController.navigate(
                            MainNavRoutes.AddUserTasks.withProjectAndUser(
                                viewModel.project,
                                colleague.member
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null
                        )
                    }
                }

                if (colleague.member.username != AppUser.username
                    && AppUser.username == viewModel.group.adminUsername
                ) {
                    IconButton(onClick = {
                        showRemoveMemberDialog.value = true
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_block_24),
                            contentDescription = null
                        )
                    }
                }

                if (colleague.member.username == viewModel.group.adminUsername) {
                    IconButton(
                        onClick = { },
                        enabled = false,
                        colors = IconButtonDefaults.iconButtonColors(
                            disabledContentColor = Color(0xFFFB8B24)
                        )
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.crown_svgrepo_com),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = colleague.member.nickname,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }

        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CompletedIcon(colleague)
                HorizontalSpacer(widthDp = 12)
                RemainingIcon(colleague)
                ExpandCollapseButton(isExpanded)
            }
        }
    }

}

@Composable
private fun RemoveMember(
    showRemoveMemberDialog: MutableState<Boolean>,
    viewModel: ProjectTasksViewModel,
    colleague: Colleague,
) {
    val context = LocalContext.current

    if (showRemoveMemberDialog.value) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(text = "Remove member")
            },
            text = {
                Text(text = "Are you sure you want to remove ${colleague.member.nickname} from the project?")
            },
            confirmButton = {

                Button(onClick = {
                    viewModel.removeMemberFromProject(colleague.member) {
                        if (it) {
                            showToast(context, "User removed successfully")
                            viewModel.getColleagues()
                            showRemoveMemberDialog.value = false
                        } else {
                            showToast(context, "failed to remove user")
                        }
                    }
                }) {
                    Text(text = "Remove")
                }

            },
            dismissButton = {
                Button(onClick = { showRemoveMemberDialog.value = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

}


@Composable
fun CompletedIcon(colleague: Colleague) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxHeight()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.completed),
            contentDescription = null,
            tint = DarkGreen,
            modifier = Modifier
                .size(20.dp)
        )
        HorizontalSpacer(widthDp = 4)
        Text(text = "${colleague.completedTasks}")
    }
}

@Composable
fun RemainingIcon(colleague: Colleague) {
    Row(
        modifier = Modifier.fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.in_progress),
            contentDescription = null,
            tint = Color.Red,
            modifier = Modifier
                .size(20.dp)
        )
        HorizontalSpacer(widthDp = 4)

        Text(text = "${colleague.remainingTasks}")
    }
}

@Composable
fun ExpandCollapseButton(isExpanded: MutableState<Boolean>) {
    IconButton(onClick = { isExpanded.value = !isExpanded.value }) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier.scale(
                scaleX = 1f,
                scaleY = if (isExpanded.value) -1f else 1f
            )
        )
    }
}

@Composable
fun TaskCard(task: Task, viewModel: ProjectTasksViewModel) {
    Card(shape = DefaultShape) {
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = task.title, fontWeight = FontWeight.Bold)


                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val painter =
                        if (task.completedDate != null) painterResource(id = R.drawable.completed)
                        else painterResource(id = R.drawable.in_progress)

                    val tint = if (task.completedDate != null) DarkGreen else Color.Red

                    Icon(
                        painter = painter, contentDescription = null,
                        modifier = Modifier
                            .size(16.dp),
                        tint = tint
                    )
                }
            }
            Text(
                text = task.description
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .size(24.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = task.weight.toString(),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                val context = LocalContext.current

                IconButton(onClick = {
                    viewModel.removeTask(task) {
                        if (it) {
                            viewModel.getColleagues()
                            showToast(context, "Task deleted")
                        } else {
                            showToast(context, "Failed to delete task")
                        }
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                }
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Text(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        text = formatDate(task.deadline, "dd-MM-yyyy"),
                        color = if (task.deadline.time < Date().time) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
