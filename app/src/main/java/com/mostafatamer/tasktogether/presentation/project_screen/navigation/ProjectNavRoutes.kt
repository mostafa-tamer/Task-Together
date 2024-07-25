package com.mostafatamer.tasktogether.presentation.project_screen.navigation

sealed class TopicNavRoutes(val route: String) {
    data object Dashboard : TopicNavRoutes("dashboard")
    data object MyTasks : TopicNavRoutes("my_tasks")
    data object ProjectTasks : TopicNavRoutes("project_tasks")
}