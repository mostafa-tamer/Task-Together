package com.mostafatamer.tasktogether.presentation.project_screen.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mostafatamer.tasktogether.data.repositories.ProjectRepository
import com.mostafatamer.tasktogether.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyTasksViewModel @Inject constructor(private val projectRepository: ProjectRepository) :
    ProjectViewModel() {

    var showEmptyList: Boolean by mutableStateOf(false)

    var tasks = mutableStateListOf<Task>()
        private set

    fun loadTasks(onLoad: () -> Unit = {}) {
        projectRepository.getTasks(project.id!!)
            .setOnSuccess {
                it?.data?.let {
                    tasks.clear()
                    tasks.addAll(it)
                    onLoad()
                    showEmptyList = tasks.isEmpty()
                }
            }.execute()
    }

    fun tick(index: Int, isChecked: Boolean) {
        projectRepository.tick(tasks[index].id!!)
            .setOnSuccess {
                it?.data?.let { task ->
                    tasks[index] = task
//                    tasks[index] = tasks[index].copy(completedDate = if (isChecked) Date() else null)
                }
            }.execute()
    }
}