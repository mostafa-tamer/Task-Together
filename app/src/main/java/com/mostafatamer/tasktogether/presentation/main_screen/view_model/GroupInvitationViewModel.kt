package com.mostafatamer.tasktogether.presentation.main_screen.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mostafatamer.tasktogether.HttpStatusCode
import com.mostafatamer.tasktogether.data.repositories.GroupsRepository
import com.mostafatamer.tasktogether.domain.model.GroupInvitation
import com.mostafatamer.tasktogether.match
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupInvitationViewModel @Inject constructor(
    private val groupsRepository: GroupsRepository,
) : ViewModel() {

    val groupInvitations = mutableStateListOf<GroupInvitation>()

    var isLoading by mutableStateOf(false)

    var isThereNoFriendRequests by mutableStateOf(false)


    private fun notifyFriendRequests() {
        isThereNoFriendRequests = groupInvitations.isEmpty()
    }

    fun loadInvitations(onLoad: () -> Unit = {}) {
        groupsRepository.getGroupInvitations()
            .setOnResponse { response, code ->
                groupInvitations.clear()
                groupInvitations.addAll(response.data)
                notifyFriendRequests()
                onLoad()
            }.execute()
    }

    fun acceptInvitation(
        friendRequestDto: GroupInvitation,
        onAcceptFriendRequest: (succeeded: Boolean) -> Unit,
    ) {
        groupsRepository.respondToGroupInvitation(friendRequestDto.id!!, true)
            .setOnResponse { _, code ->
                onAcceptFriendRequest(HttpStatusCode.OK.match(code))
                groupInvitations.removeIf { it.id == friendRequestDto.id }
                notifyFriendRequests()
            }.execute()
    }

    fun rejectInvitation(
        friendRequestDto: GroupInvitation,
        onFriendRequestRemoved: (Boolean) -> Unit,
    ) {
        groupsRepository.respondToGroupInvitation(friendRequestDto.id!!, false)
            .setOnResponse { _, code ->
                onFriendRequestRemoved(HttpStatusCode.OK.match(code))
                groupInvitations.removeIf { it.id == friendRequestDto.id }
                notifyFriendRequests()
            }.execute()
    }

    fun reset() {
        groupInvitations.clear()
        isLoading = false
        isThereNoFriendRequests = false
    }
}