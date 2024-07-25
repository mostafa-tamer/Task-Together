package com.mostafatamer.tasktogether.presentation.project_screen.viewModels

import androidx.compose.runtime.mutableStateListOf
import com.mostafatamer.tasktogether.HttpStatusCode
import com.mostafatamer.tasktogether.data.repositories.ProjectRepository
import com.mostafatamer.tasktogether.domain.model.Colleague
import com.mostafatamer.tasktogether.domain.model.Task
import com.mostafatamer.tasktogether.domain.model.User
import com.mostafatamer.tasktogether.match
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectTasksViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
) : ProjectViewModel() {

    val colleagues = mutableStateListOf<Colleague>()

    fun getColleagues(onLoad: () -> Unit = {}) {
        projectRepository.getMembersStatistics(project.id!!)
            .setOnResponse { response, code ->
                if (HttpStatusCode.OK.match(code)) {
                    colleagues.clear()
                    colleagues.addAll(response.data)
                    onLoad()
                }
            }.execute()
    }

    fun removeMemberFromProject(user: User, onRemoved: (Boolean) -> Unit) {
        projectRepository.removeMemberFromProject(project.id!!, user.id)
            .setOnResponse { _, code ->
                onRemoved(code == HttpStatusCode.NO_CONTENT.code)
            }.execute()
    }

    fun removeTask(task: Task, onRemoved: (Boolean) -> Unit) {
        projectRepository.deleteTask(task)
            .setOnResponse { _, code ->
                onRemoved(HttpStatusCode.NO_CONTENT.match(code))
            }.execute()
    }
}