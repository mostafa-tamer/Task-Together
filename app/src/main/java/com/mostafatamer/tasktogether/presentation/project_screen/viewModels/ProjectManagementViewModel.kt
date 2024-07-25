package com.mostafatamer.tasktogether.presentation.project_screen.viewModels

import com.mostafatamer.tasktogether.HttpStatusCode
import com.mostafatamer.tasktogether.data.repositories.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectManagementViewModel @Inject constructor(private val projectRepository: ProjectRepository) :
    ProjectViewModel() {

    fun deleteProject(function: (Boolean) -> Unit) {
        projectRepository.deleteProject(project.id!!)
            .setOnResponse { _, code ->
                println(code)
                function(code == HttpStatusCode.NO_CONTENT.code)
            }.execute()
    }

    fun updateProject(name: String, description: String, onSucceed: (Boolean) -> Unit) {

        val project = project.copy(title = name, description = description)

        projectRepository.updateProject(project)
            .setOnResponse { _, code ->
                println(code)
                onSucceed(code == HttpStatusCode.OK.code)
            }.execute()
    }
}
