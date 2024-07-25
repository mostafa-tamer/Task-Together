package com.mostafatamer.tasktogether.domain.model



data class Colleague(
    val member: User = User(),
    var tasks: MutableList<Task> = mutableListOf(),
    var assignedTasks: Int = 0,
    var completedTasks: Int = 0,
    var remainingTasks: Int = 0,
    var progress: Float = 0f
)