package com.mostafatamer.tasktogether.presentation.project_screen.projectDashboardScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mostafatamer.tasktogether.DefaultShape
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.ProjectDashboardViewModel

@Composable
fun RowScope.ProjectRemainingTime(viewModel: ProjectDashboardViewModel) {
    var deadlineLineMaxSize by remember { mutableIntStateOf(0) }

    Card(
        Modifier.weight(1f),
        shape = DefaultShape
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(
                Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned {
                            deadlineLineMaxSize = it.size.width
                        }
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Text(
                            text = "Time Remaining",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = viewModel.remainingTime,
                            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Surface(shape = DefaultShape) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .background(Color.White),
                    )

                    Box(
                        modifier = Modifier
                            .width((deadlineLineMaxSize * viewModel.remainingTimePercentage / LocalDensity.current.density).dp)
                            .height(32.dp)
                            .background(Color.Red)
                    )
                }
            }
        }
    }
}
