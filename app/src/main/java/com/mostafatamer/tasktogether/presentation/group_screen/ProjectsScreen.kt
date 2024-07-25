package com.mostafatamer.tasktogether.presentation.group_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mostafatamer.tasktogether.DefaultShape
import com.mostafatamer.tasktogether.convertMillisToDate
import com.mostafatamer.tasktogether.domain.model.Project
import com.mostafatamer.tasktogether.presentation.authentication.appUser.AppUser
import com.mostafatamer.tasktogether.presentation.components.FastSwipe
import com.mostafatamer.tasktogether.presentation.components.HeadlineIcons
import com.mostafatamer.tasktogether.presentation.components.TitleBar
import com.mostafatamer.tasktogether.presentation.components.TrailingVerticalSpacer
import com.mostafatamer.tasktogether.presentation.components.VerticalSpacer
import com.mostafatamer.tasktogether.presentation.group_screen.viewModels.ProjectsViewModel
import com.mostafatamer.tasktogether.presentation.navigation.MainNavRoutes
import com.mostafatamer.tasktogether.showToast
import java.util.Date

@Composable
fun ProjectsScreen(
    viewModel: ProjectsViewModel,
    mainNavHostController: NavHostController,
) {

    LaunchedEffect(Unit) {
        viewModel.getProjects()
    }

    FastSwipe(onRefresh = {
        viewModel.getProjects { it() }
    }) { Body(viewModel, mainNavHostController) }
}


@Composable
private fun Body(
    viewModel: ProjectsViewModel,
    mainNavHostController: NavHostController,
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .padding(horizontal = 4.dp)
    ) {

        if (viewModel.showEmptyList) {
            Text(
                text = "No projects yet",
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Column {
            TitleBar(viewModel, mainNavHostController)
            List(viewModel, mainNavHostController)
        }
    }
}

@Composable
private fun TitleBar(
    viewModel: ProjectsViewModel,
    navHostController: NavHostController,
) {
    val showSheet = remember {
        mutableStateOf(false)
    }

    CreateGroupModelSheet(showSheet, viewModel)

    Box {
        TitleBar("Projects") {
            if (AppUser.username == viewModel.group.adminUsername) {

                HeadlineIcons(
                    Icons.Filled.Add, null,
                ) {
                    showSheet.value = true
                }
            }
        }
    }
    VerticalSpacer(heightDp = 16)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit = {},
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis >= System.currentTimeMillis()
        }
    })

    val selectedDate = datePickerState.selectedDateMillis

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate ?: Date().time)
                onDismiss()
            }) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupModelSheet(showSheet: MutableState<Boolean>, viewModel: ProjectsViewModel) {
    var showStartDatePicker = remember {
        mutableStateOf(false)
    }
    var showEndDatePicker = remember {
        mutableStateOf(false)
    }

    var startDate: Date by remember {
        mutableStateOf(Date())
    }

    val dayMillis = 86400000
    var endDate: Date by remember {
        mutableStateOf(Date(System.currentTimeMillis() + dayMillis))
    }

    if (startDate > endDate)
        endDate = Date(startDate.time + dayMillis)


    if (showStartDatePicker.value) {
        MyDatePickerDialog(
            onDateSelected = {
                startDate = Date(it)
            }
        ) {
            showStartDatePicker.value = false
        }
    }

    if (showEndDatePicker.value) {
        MyDatePickerDialog(
            onDateSelected = {
                endDate = Date(it)
            }
        ) {
            showEndDatePicker.value = false
        }
    }


    val context = LocalContext.current
    var projectsName by remember { mutableStateOf("") }
    var projectsDescription by remember { mutableStateOf("") }

    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showSheet.value) {
        ModalBottomSheet(
            sheetState = state,
            onDismissRequest = { showSheet.value = false }
        ) {
            Column(
                Modifier
                    .height((LocalConfiguration.current.screenHeightDp * 0.85).dp)
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    shape = DefaultShape,
                    value = projectsName,
                    onValueChange = {
                        projectsName = it
                    },
                    placeholder = {
                        Text("Project title")
                    }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    shape = DefaultShape,
                    minLines = 3, maxLines = 3,
                    value = projectsDescription,
                    onValueChange = {
                        projectsDescription = it
                    },
                    placeholder = {
                        Text("Project description")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DatePickerButton(showStartDatePicker, "Start Date")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = convertMillisToDate(startDate.time)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))


                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DatePickerButton(showEndDatePicker, "End Date")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = convertMillisToDate(endDate.time)
                        )
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (projectsName.isNotBlank()) {
                            viewModel.createProject(
                                viewModel.group.id!!,
                                Project(
                                    title = projectsName,
                                    description = projectsDescription,
                                    startDate = startDate,
                                    deadline = endDate
                                )
                            ) {
                                if (it) {
                                    showToast(context, "Group created successfully")
                                    showSheet.value = false
                                    startDate = Date()
                                    endDate = Date()
                                    projectsName = ""
                                    projectsDescription = ""
                                } else {
                                    showToast(context, "Error creating group")
                                }
                            }
                        } else {
                            showToast(context, "Please enter the project name")
                        }
                    }) {
                    Text(text = "Create Project")
                }
            }
        }
    }

}

@Composable
private fun DatePickerButton(showStartDatePicker: MutableState<Boolean>, text: String) {
    Surface(
        shape = DefaultShape,
        modifier = Modifier
            .size(72.dp)
    ) {
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    showStartDatePicker.value = true
                }
                .padding(4.dp),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = text, fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
private fun List(viewModel: ProjectsViewModel, navHostController: NavHostController) {
    Column {
        LazyColumn(Modifier.fillMaxSize()) {
            items(viewModel.projects) { project ->
                ProjectCard(viewModel, navHostController, project)
                TrailingVerticalSpacer(viewModel.projects, project)
            }
            item {
                VerticalSpacer(heightDp = 8)
            }
        }
    }
}


@Composable
private fun ProjectCard(
    viewModel: ProjectsViewModel,
    navHostController: NavHostController,
    project: Project,
) {
    Card(shape = DefaultShape) {
        Column(
            Modifier
                .clickable {
                    navHostController.navigate(
                        MainNavRoutes.ProjectNavRoute.navigateToTasks(viewModel.group, project)
                    )
                }
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = project.title,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Text(
                text = project.description
            )
        }
    }
}
