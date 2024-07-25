package com.mostafatamer.tasktogether.presentation.viewModels

import androidx.compose.runtime.mutableStateListOf
import com.mostafatamer.tasktogether.data.repositories.ProjectRepository
import com.mostafatamer.tasktogether.domain.model.User
import com.mostafatamer.tasktogether.presentation.project_screen.viewModels.ProjectViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectInfoViewModel @Inject constructor(private val projectRepository: ProjectRepository) :
    ProjectViewModel() {
    var topicMembers = mutableStateListOf<User>()
        private set

    fun getMembers() {
        projectRepository.getMembers(project)
            .setOnSuccess {
                if (topicMembers.isEmpty())
                    topicMembers.addAll(it.data)
            }.execute()
    }
}