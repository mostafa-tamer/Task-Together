package com.mostafatamer.tasktogether.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.mostafatamer.tasktogether.DefaultShape
import com.mostafatamer.tasktogether.R
import com.mostafatamer.tasktogether.presentation.components.ActionBar
import com.mostafatamer.tasktogether.presentation.components.HorizontalSpacer
import com.mostafatamer.tasktogether.presentation.components.NavigateUp
import com.mostafatamer.tasktogether.presentation.components.VerticalSpacer
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.AddUserTaskViewModel
import com.mostafatamer.tasktogether.showToast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun AddUserTaskScreen(viewModel: AddUserTaskViewModel, navHostController: NavHostController) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            ActionBar(
                label = "Assign Tasks",
                prefixComposable = {
                    NavigateUp(navHostController = navHostController)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.addUserTask {
                    if (it) {
                        navHostController.navigateUp()
                    } else {
                        showToast(context, "Failed to add task")
                    }
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.add_task_list_svgrepo_com),
                    contentDescription = null,
                    Modifier.size(24.dp)
                )
            }
        }

    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            Content(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(viewModel: AddUserTaskViewModel) {

    var showDatePickerDialog by remember {
        mutableStateOf(false)
    }
    var deadlineText by remember {
        mutableStateOf("Deadline")
    }
    val state = rememberDatePickerState()
    state.selectedDateMillis = System.currentTimeMillis()

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                Button(onClick = {
                    viewModel.deadlineDate = Date(state.selectedDateMillis!!)
                    deadlineText = SimpleDateFormat(
                        "dd-MM-yyyy",
                        Locale.getDefault()
                    ).format(viewModel.deadlineDate)
                    showDatePickerDialog = false
                }) {
                    Text(text = "Ok")
                }
            }
        ) {
            DatePicker(state = state)
        }
    }

    Column {
        Text(
            text = viewModel.user.nickname,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        VerticalSpacer(heightDp = 32)
        Row {
            OutlinedTextField(
                shape = DefaultShape,
                value = viewModel.taskTitle,
                onValueChange = {
                    viewModel.taskTitle = it
                },
                placeholder = {
                    Text(text = "Task Title")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f),
                singleLine = true
            )
            HorizontalSpacer(widthDp = 8)
            OutlinedTextField(
                shape = DefaultShape,
                value = viewModel.taskWeight,
                onValueChange = {
                    if (it.isDigitsOnly())
                        viewModel.taskWeight = it
                },
                placeholder = {
                    Text(text = "Weight")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                singleLine = true
            )
        }

        VerticalSpacer(heightDp = 16)
        OutlinedTextField(
            shape = DefaultShape,
            value = viewModel.taskDescription,
            minLines = 6,
            maxLines = 6,
            onValueChange = {
                viewModel.taskDescription = it
            },
            placeholder = {
                Text(text = "Task Description (Optional)")
            },
            modifier = Modifier.fillMaxWidth()
        )
        VerticalSpacer(heightDp = 16)

        Surface(
            modifier = Modifier,
            shape = CircleShape
        ) {
            IconButton(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth(),
                onClick = { showDatePickerDialog = true }
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    HorizontalSpacer(widthDp = 8)
                    Text(
                        text = deadlineText,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

    }
}
