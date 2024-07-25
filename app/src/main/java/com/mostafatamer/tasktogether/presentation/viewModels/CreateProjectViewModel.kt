package com.mostafatamer.tasktogether.presentation.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mostafatamer.tasktogether.data.repositories.GroupsRepository
import com.mostafatamer.tasktogether.data.repositories.ProjectRepository
import com.mostafatamer.tasktogether.domain.model.Group
import com.mostafatamer.tasktogether.domain.model.Project
import com.mostafatamer.tasktogether.domain.model.User
import com.mostafatamer.tasktogether.presentation.group_screen.viewModels.GroupViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val groupsRepository: GroupsRepository,
) : GroupViewModel() {

    var subjectName: String by mutableStateOf("")
    var subjectDescription: String by mutableStateOf("")

    var subjectCreated by mutableStateOf(false)

    var fillAllGroupPropertiesFlag by mutableStateOf(false)

    fun createGroup() {
        fillAllGroupPropertiesFlag = listOf(
            subjectName,
            subjectDescription,
        ).any { it.isNullOrBlank() }

        if (!fillAllGroupPropertiesFlag) {
            val project = Project(
                title = subjectName,
                description = subjectDescription,
            )

            projectRepository.createProject(
                group.id!!,
                project
            ).setOnSuccess {
                subjectCreated = true
            }.execute()
        }
    }

    var candidates = mutableStateListOf<User>()
        private set


    fun getCandidates(group: Group) {

        groupsRepository.getGroupMembers(group)
            .setOnSuccess {
                if (candidates.isEmpty())
                    candidates.addAll(it.data)
            }.execute()
    }
}