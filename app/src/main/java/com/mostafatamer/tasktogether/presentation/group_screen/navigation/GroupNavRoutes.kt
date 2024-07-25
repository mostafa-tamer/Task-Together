package com.mostafatamer.tasktogether.presentation.group_screen.navigation

sealed class GroupNavRoutes(val route: String) {
    data object Announcements : GroupNavRoutes("announcements")
    data object Projects : GroupNavRoutes("subjects")
    data object Chat : GroupNavRoutes("chat")
    data object GroupOverview : GroupNavRoutes("members")
}