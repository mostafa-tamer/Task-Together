package com.mostafatamer.tasktogether.presentation.project_screen.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mostafatamer.tasktogether.HttpStatusCode
import com.mostafatamer.tasktogether.data.repositories.ProjectRepository
import com.mostafatamer.tasktogether.domain.model.Group
import com.mostafatamer.tasktogether.domain.model.Project
import com.mostafatamer.tasktogether.domain.model.Task
import com.mostafatamer.tasktogether.domain.model.User
import com.mostafatamer.tasktogether.match
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddUserTaskViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
) : ProjectViewModel() {

    lateinit var user: User

    fun init(user: User, project: Project, group: Group) {
        super.init(project, group)
        this.user = user
    }

    var taskTitle by mutableStateOf("")
    var taskDescription by mutableStateOf("")
    var deadlineDate by mutableStateOf(Date())
    var taskWeight by mutableStateOf("")

    fun addUserTask(onSuccess: (Boolean) -> Unit) {
        if (taskWeight.isNotBlank() && taskWeight.toInt() > 0 && taskTitle.isNotBlank()) {
            val task = Task(
                title = taskTitle,
                description = taskDescription,
                deadline = deadlineDate,
                weight = taskWeight.toInt(),
                assignedMemberId = user.id
            )

            println(task)
            println(user)
            projectRepository.assignTask(project.id!!, task)
                .setOnResponse { _, code ->
                    onSuccess(HttpStatusCode.CREATED.match(code))
                }.execute()
        }
    }
}
