package com.mostafatamer.tasktogether.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mostafatamer.tasktogether.R
import com.mostafatamer.tasktogether.entities.ui.BarItem
import com.mostafatamer.tasktogether.presentation.group_screen.navigation.GroupNavRoutes
import com.mostafatamer.tasktogether.presentation.main_screen.navigation.MainScreenNavRouts
import com.mostafatamer.tasktogether.presentation.project_screen.navigation.TopicNavRoutes

object NavBarItems {

    val mainScreenNavBar = listOf(
        BarItem(
            title = "Groups",
            route = MainScreenNavRouts.Groups.route,
            vectorId = R.drawable.groups_svgrepo_com,
        ),
        BarItem(
            title = "Group Invitations",
            route = MainScreenNavRouts.GroupInvitation.route,
            vectorId = R.drawable.invite_svgrepo_com,
        )
    )

    val groupNavBar = listOf(
        BarItem(
            title = "Projects",
            route = GroupNavRoutes.Projects.route,
            vectorId = R.drawable.dashboard_svgrepo_com,
        ),
        BarItem(
            title = "Announcements",
            route = GroupNavRoutes.Announcements.route,
            imageVector = Icons.Filled.Notifications,
        ),
        BarItem(
            title = "Chat",
            route = GroupNavRoutes.Chat.route,
            vectorId = R.drawable.chat_round_line_svgrepo_com,
        ),
        BarItem(
            title = "Configuration",
            route = GroupNavRoutes.GroupOverview.route,
            vectorId = R.drawable.group_svgrepo_com,
        ),
    )
    val topicNavBar = listOf(
        BarItem(
            title = "My Tasks",
            route = TopicNavRoutes.MyTasks.route,
            vectorId = R.drawable.my_tasks
        ),
        BarItem(
            title = "Dashboard",
            route = TopicNavRoutes.Dashboard.route,
            vectorId = R.drawable.chart_pie_svgrepo_com
        ),
        BarItem(
            title = "Project Tasks",
            route = TopicNavRoutes.ProjectTasks.route,
            vectorId = R.drawable.topic_tasks
        ),
    )
}

@Composable
fun BottomNavigationBar(
    navHostController: NavHostController,
    navbarItems: List<BarItem>,
    navItemsToHide: Set<String> = emptySet(),
) {
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRout = backStackEntry?.destination?.route

    println(currentRout)

    BottomNavigation(backgroundColor = MaterialTheme.colorScheme.primary) {
        navbarItems.forEach { navItem ->
            if (navItem.route in navItemsToHide) {
                return@forEach
            }

            BottomNavigationItem(
                selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                selected = currentRout == navItem.route,
                onClick = {
                    navHostController.navigate(navItem.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    val modifier = Modifier.size(28.dp)
                    navItem.imageVector?.let {
                        Icon(
                            imageVector = navItem.imageVector,
                            contentDescription = null,
                            modifier = modifier,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    navItem.vectorId?.let {
                        Icon(
                            painter = painterResource(id = navItem.vectorId),
                            contentDescription = null,
                            modifier = modifier,
                            MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
//                label = {
//                    Text(text = navItem.title, fontSize = 12.sp)
//                }
            )
        }
    }
}