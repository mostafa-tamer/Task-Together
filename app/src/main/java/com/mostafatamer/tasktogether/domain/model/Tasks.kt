package com.mostafatamer.tasktogether.domain.model

data class Tasks(
    val completedTasks: List<Task>,
    val inCompletedTasks: List<Task>
)