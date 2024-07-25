package com.mostafatamer.tasktogether.presentation.project_screen.projectDashboardScreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mostafatamer.tasktogether.Dimensions
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.ProjectDashboardViewModel

@Composable
internal fun RowScope.ProgressPie(viewModel: ProjectDashboardViewModel) {
    Card(
        Modifier.weight(1f),
        shape = RoundedCornerShape(0)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            ProgressPie(viewModel)
        }
    }
}

@Composable
fun BoxScope.ProgressPie(viewModel: ProjectDashboardViewModel) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surfaceContainerHighest


    Surface(shadowElevation = 8.dp, shape = CircleShape) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                radius = size.width / 2,
                color = Color.White,
            )
            drawArc(
                startAngle = 90f,
                sweepAngle = (viewModel.projectStatistics!!.projectProgress!! / 100f) * 360f,
                size = size,
                color = primaryColor,
                useCenter = true
            )

            drawCircle(
                color = surfaceColor,
                radius = size.width * 0.85f * 0.5f
            )
        }
    }
    Column(modifier = Modifier.align(Alignment.Center)) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            text = "${(viewModel.projectStatistics!!.projectProgress!!).toInt()}%"
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            text = "Progress"
        )
    }
}
