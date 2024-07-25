package com.mostafatamer.tasktogether.presentation.project_screen.projectDashboardScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mostafatamer.tasktogether.Dimensions
import com.mostafatamer.tasktogether.domain.model.ProjectStatistics
import com.mostafatamer.tasktogether.presentation.components.HorizontalSpacer
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.ProjectDashboardViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.round

@Composable
fun ColumnScope.DailyProgressCard(viewModel: ProjectDashboardViewModel) {

    Card(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        shape = RoundedCornerShape(Dimensions.CARD_ROUNDNESS.dp)
    ) {
        Box(
            Modifier
                .padding(16.dp)
        ) {
            Row {
                Y_Axis(viewModel)
                HorizontalSpacer(widthDp = 4)
                Bars(viewModel)
            }
        }
    }
}

@Composable
private fun Bars(
    viewModel: ProjectDashboardViewModel,
) {
    val listState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    if (viewModel.projectStatistics.totalCompletedTasksWeightInterval.size - 1 >= 0) {
        coroutineScope.launch {
            listState.scrollToItem(
                viewModel.projectStatistics.totalCompletedTasksWeightInterval.size - 1
            )
        }
    }

    val space = (Dimensions.DEFAULT_TEXT_HEIGHT / 2 / LocalDensity.current.density).dp - 0.5.dp

    Box {
        Column {
            Box(modifier = Modifier.height(space))
            BackGroundLines()
        }
        Column {
            Box(modifier = Modifier.height(space))
            BarsLazyRow(listState, viewModel)

        }
    }
}

@Composable
private fun BarsLazyRow(
    listState: LazyListState,
    viewModel: ProjectDashboardViewModel,
) {
    LazyRow(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxSize(),
        state = listState
    ) {
        items(viewModel.projectStatistics.totalCompletedTasksWeightInterval.sortedBy { it.date }) { interval ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Bar(interval, viewModel)

                val space = Dimensions.DEFAULT_TEXT_HEIGHT / 2 / LocalDensity.current.density

                Box(modifier = Modifier.height(space.dp))

                Text(

//                    text = interval.totalWeight.toString(),
                    text = SimpleDateFormat(
                        "d MMM",
                        Locale.getDefault()
                    ).format(interval.date)
                )
            }
            HorizontalSpacer(widthDp = 6)
        }
    }
}

@Composable
private fun ColumnScope.Bar(
    interval: ProjectStatistics.CompletedTasksWeightPerDay,
    viewModel: ProjectDashboardViewModel,
) {

    var barMaxHeight by remember { mutableDoubleStateOf(0.0) }

    val density = LocalDensity.current.density
    Box(

        modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .onGloballyPositioned {
                val percentage =
                    interval.totalWeight / (viewModel.projectStatistics.maxDayWeight.toDouble()* 1.2)
                barMaxHeight = percentage * (it.size.height.toDouble() / density)
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            shape = RoundedCornerShape(topEnd = 4.dp, topStart = 4.dp),
        ) {
            Box(
                Modifier
                    .height(
                        (barMaxHeight.dp)
                    )
                    .background(MaterialTheme.colorScheme.primary)
                    .width(30.dp)
            )
        }
    }
}

@Composable
private fun BackGroundLines() {
    Column {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            LineSeparator()
            LineSeparator()
            LineSeparator()
            LineSeparator()
            LineSeparator()


        }
        val space = (Dimensions.DEFAULT_TEXT_HEIGHT / 2 / LocalDensity.current.density).dp - 0.5.dp
        Box(modifier = Modifier.height(space))

        Text(text = "")
    }
}

@Composable
private fun Y_Axis(
    viewModel: ProjectDashboardViewModel
) {
    Column {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            val upperBound = viewModel.projectStatistics.maxDayWeight.toDouble() * 1.2

            val formattedUpperBound = round(upperBound).toInt().toString()
            val formatted75Percent = round(upperBound * 0.75).toInt().toString()
            val formatted50Percent = round(upperBound * 0.5).toInt().toString()
            val formatted25Percent = round(upperBound * 0.25).toInt().toString()

            Y_Axis_Value(formattedUpperBound)
            Y_Axis_Value(formatted75Percent)
            Y_Axis_Value(formatted50Percent)
            Y_Axis_Value(formatted25Percent)
            Y_Axis_Value("0")


        }
        Text(text = "")
    }
}

@Composable
private fun LineSeparator() {
    Box(contentAlignment = Alignment.TopCenter) {
        Surface(shape = RoundedCornerShape(2.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )
        }
//        Text(text = "")
    }
}

@Composable
private fun Y_Axis_Value(text: String) {
    Column {
        Text(text = text, textAlign = TextAlign.Center)
    }
}
