package com.mostafatamer.tasktogether.presentation.project_screen.projectDashboardScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.mostafatamer.tasktogether.Dimensions
import com.mostafatamer.tasktogether.presentation.components.HorizontalSpacer
import com.mostafatamer.tasktogether.presentation.components.VerticalSpacer
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.ProjectDashboardViewModel

@Composable
fun DashBoard(viewModel: ProjectDashboardViewModel) {
    Column(
        Modifier
            .fillMaxWidth()
    ) {
        val cardHeight =
            (LocalConfiguration.current.screenWidthDp.dp - (2 * Dimensions.SCREEN_PADDING).dp) / 2

        Row(
            Modifier
                .height(cardHeight)
                .fillMaxWidth()
        ) {
            ProgressPie(viewModel)
            HorizontalSpacer(Dimensions.SPACE_BETWEEN_CARDS)
            ProjectRemainingTime(viewModel)
        }
        VerticalSpacer(heightDp = Dimensions.SPACE_BETWEEN_CARDS)
        ColleagueProgressCard(viewModel)
        VerticalSpacer(heightDp = Dimensions.SPACE_BETWEEN_CARDS)
        DailyProgressCard(viewModel)
    }
}