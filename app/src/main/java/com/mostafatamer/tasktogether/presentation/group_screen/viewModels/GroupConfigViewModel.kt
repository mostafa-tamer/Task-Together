package com.mostafatamer.tasktogether.presentation.group_screen.viewModels

import androidx.compose.runtime.mutableStateListOf
import com.mostafatamer.tasktogether.HttpStatusCode
import com.mostafatamer.tasktogether.data.repositories.GroupsRepository
import com.mostafatamer.tasktogether.domain.model.GroupInvitation
import com.mostafatamer.tasktogether.domain.model.User
import com.mostafatamer.tasktogether.match
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

typealias OnEvent = (Boolean) -> Unit

@HiltViewModel
class GroupConfigViewModel @Inject constructor(
    private val groupsRepository: GroupsRepository,
) : GroupViewModel() {

    val members = mutableStateListOf<User>()

    fun loadMembers(onLoad: () -> Unit = {}) {
        groupsRepository.getGroupMembers(group)
            .setOnSuccess {
                members.clear()
                members.addAll(it.data)
                onLoad()
            }.execute()
    }

    fun removeMemberFromGroup(user: User, onRemoved: (Boolean) -> Unit) {
        groupsRepository.removeMemberFromGroup(user.id, group)
            .setOnResponse { _, code ->
                onRemoved(code == HttpStatusCode.NO_CONTENT.code)
            }.execute()
    }


    fun addMemberToGroup(userName: String, message: String, onAdded: OnEvent) {
        groupsRepository.sendMemberInvitation(group.id!!, userName, GroupInvitation(message))
            .setOnResponse { _, code ->
                onAdded(HttpStatusCode.OK.match(code))
            }.execute()
    }


}