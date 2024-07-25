package com.mostafatamer.tasktogether.presentation.project_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mostafatamer.tasktogether.DefaultShape
import com.mostafatamer.tasktogether.HorizontalPadding
import com.mostafatamer.tasktogether.SpacerBetweenCards
import com.mostafatamer.tasktogether.domain.model.Task
import com.mostafatamer.tasktogether.formatDate
import com.mostafatamer.tasktogether.presentation.components.FastSwipe
import com.mostafatamer.tasktogether.presentation.components.TitleBar
import com.mostafatamer.tasktogether.presentation.components.TrailingVerticalSpacer
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.MyTasksViewModel


@Composable
fun MyTasksScreen(
    viewModel: MyTasksViewModel,
    navHostController: NavHostController,
) {
    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }
    FastSwipe(onRefresh = {
        viewModel.loadTasks{ it() }
    }){
        Scaffold {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                if (viewModel.showEmptyList) {
                    Text(
                        text = "No tasks yet",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Content(viewModel)
            }
        }
    }
}

@Composable
fun Content(viewModel: MyTasksViewModel) {
    Box(modifier = Modifier.padding(HorizontalPadding)) {
        Column(Modifier.fillMaxSize()) {
            TitleBar(text = "My Tasks")
            LazyColumn(Modifier.fillMaxSize()) {
                itemsIndexed(viewModel.tasks) { index, task ->
                    TaskCard(viewModel, index, task)
                    TrailingVerticalSpacer(list = viewModel.tasks, item = task)
                }
                item {
                    SpacerBetweenCards()
                }
            }
        }
    }
}

@Composable
private fun TaskCard(
    viewModel: MyTasksViewModel,
    index: Int,
    task: Task,
) {
    Card(shape = DefaultShape) {
        Box(
            Modifier
                .clickable {
                    viewModel.tick(index, task.completedDate == null)
                }
                .padding(16.dp)
                .fillMaxSize()) {
            Row {
                Checkbox(
                    checked = task.completedDate != null,
                    onCheckedChange = { isChecked ->
                        viewModel.tick(index, isChecked)
                    },
                )
                Column {
                    Row {
                        Text(
                            text = task.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(shape = CircleShape, modifier = Modifier.size(24.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = task.weight.toString(),
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                    Text(
                        text = task.description ?: "",
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = formatDate(task.deadline, "dd-MM-yyyy"),
//                                            color = if (task.deadline.time < Date().time) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun Preview() {
//    MyTasksScreen(MyTasksViewModel())
//}
