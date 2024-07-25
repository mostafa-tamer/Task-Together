package com.mostafatamer.tasktogether.presentation.project_screen.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mostafatamer.tasktogether.data.repositories.ProjectRepository
import com.mostafatamer.tasktogether.domain.model.Project
import com.mostafatamer.tasktogether.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AddMembersToProjectViewModel
@Inject constructor(
    private val projectRepository: ProjectRepository,
) : ProjectViewModel() {

    var showEmptyList: Boolean by mutableStateOf(false)

    var members = mutableStateListOf<UserSelection>()
        private set

    var membersAdded by mutableStateOf(false)


    fun getTopicCandidates() {
        projectRepository.getTopicCandidates(project)
            .setOnResponse { response, code ->
                println(code)
                println(response.data)
                if (members.isEmpty())
                    members.addAll(response.data.map { user -> UserSelection(user, false) })
                showEmptyList = members.isEmpty()
            }.execute()
    }

    fun toggleUserSelection(index: Int) {
        members[index] = members[index].copy(isSelected = !members[index].isSelected)
    }

    fun addMembersToTopic(project: Project) {
        val userIds = members.filter { it.isSelected }.map { it.user.id!! }
        projectRepository.addMembers(project, userIds)
            .setOnSuccess {
                println(it)
                membersAdded = true
            }.execute()
    }

}

data class UserSelection(val user: User, val isSelected: Boolean)
