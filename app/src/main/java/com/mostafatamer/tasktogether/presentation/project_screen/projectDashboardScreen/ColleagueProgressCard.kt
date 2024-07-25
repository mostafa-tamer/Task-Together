package com.mostafatamer.tasktogether.presentation.project_screen.projectDashboardScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mostafatamer.tasktogether.Dimensions
import com.mostafatamer.tasktogether.presentation.components.HorizontalSpacer
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.ProjectDashboardViewModel
import kotlin.random.Random

@Composable
fun ColumnScope.ColleagueProgressCard(viewModel: ProjectDashboardViewModel) {
    Card(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        shape = RoundedCornerShape(Dimensions.CARD_ROUNDNESS.dp)
    ) {
        LazyColumn(
            Modifier
                .padding(16.dp),
        ) {
            items(viewModel.colleagues!!.sortedByDescending { it.progress }) { colleague ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = colleague.member.nickname, fontWeight = FontWeight.Bold)
                    HorizontalSpacer(widthDp = 8)
                    var boxContainerWidth by remember { mutableStateOf(0.dp) }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .onSizeChanged {
                                boxContainerWidth = (it.width * colleague.progress!! / 100).dp
                            }
                            .fillMaxWidth()
                    ) {
                        Card(shape = RoundedCornerShape(16.dp)) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        Color(
                                            Random.nextInt(0, 200),
                                            Random.nextInt(0, 200),
                                            Random.nextInt(0, 200)
                                        )
                                    )
                                    .height(8.dp)
                                    .width(boxContainerWidth / LocalDensity.current.density)
                            )
                        }
                    }
                    HorizontalSpacer(widthDp = 8)
                    Text(text = (colleague.progress).toInt().toString() + '%')

                }
            }
        }
    }
}
