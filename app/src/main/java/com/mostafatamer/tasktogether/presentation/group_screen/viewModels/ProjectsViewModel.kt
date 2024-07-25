package com.mostafatamer.tasktogether.presentation.group_screen.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mostafatamer.tasktogether.data.repositories.ProjectRepository
import com.mostafatamer.tasktogether.domain.model.Project
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
) : GroupViewModel() {
    var showEmptyList: Boolean by mutableStateOf(false)

    var projects = mutableStateListOf<Project>()
        private set


    fun getProjects(onLoaded: () -> Unit = {}) {
        projectRepository.getProjects(group.id!!)
            .setOnSuccess {
                it?.data?.let {
                    projects.clear()
                    projects.addAll(it)
                    showEmptyList = projects.isEmpty()

                    onLoaded()
                }
            }.execute()
    }


    var showAddAlertDialog by mutableStateOf(false)

    fun createProject(
        groupId: String,
        project: Project,
        onSuccess: (Boolean) -> Unit,
    ) {
        projectRepository.createProject(groupId, project)
            .setOnSuccess {
                it?.data?.let {
                    projects.add(it)
                    showAddAlertDialog = false
                    showEmptyList = projects.isEmpty()
                }
                onSuccess(it != null)
            }.execute()
    }
}