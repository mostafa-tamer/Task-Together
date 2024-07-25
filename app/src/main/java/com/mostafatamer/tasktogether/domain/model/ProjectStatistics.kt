package com.mostafatamer.tasktogether.domain.model

import java.util.Date

data class ProjectStatistics(
    var totalCompletedTasksWeightInterval: List<CompletedTasksWeightPerDay> = emptyList(),
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    var projectProgress: Float = 0f,
    val maxDayWeight: Int = 0
) {
    data class CompletedTasksWeightPerDay(
        val date: Date = Date(0),
        val totalWeight: Int = 0,
    )
}