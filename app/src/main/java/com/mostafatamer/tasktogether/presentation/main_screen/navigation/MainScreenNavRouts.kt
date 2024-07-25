package com.mostafatamer.tasktogether.presentation.main_screen.navigation

sealed class MainScreenNavRouts(val route: String) {
    data object Groups : MainScreenNavRouts("groups")
    data object GroupInvitation : MainScreenNavRouts("group_invitation")
}