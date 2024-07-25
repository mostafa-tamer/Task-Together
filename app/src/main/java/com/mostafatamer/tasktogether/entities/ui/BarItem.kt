package com.mostafatamer.tasktogether.entities.ui

import androidx.compose.ui.graphics.vector.ImageVector

data class BarItem(
    val title: String,
    val route: String,
    val imageVector: ImageVector? = null,
    val vectorId: Int? = null
)
