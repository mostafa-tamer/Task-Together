package com.mostafatamer.tasktogether.data.repositories;

import com.mostafatamer.tasktogether.data.remote.api_service.GroupsApiService
import com.mostafatamer.tasktogether.domain.model.Announcement
import com.mostafatamer.tasktogether.domain.model.Group
import com.mostafatamer.tasktogether.domain.model.GroupInvitation
import com.mostafatamer.tasktogether.domain.model.MessagePage
import com.mostafatamer.tasktogether.domain.model.Response
import com.mostafatamer.tasktogether.lib.CallDecorator
import okhttp3.MultipartBody

/*


 */

class GroupsRepository(private val apiService: GroupsApiService) {

    fun createGroup(
        namePart: MultipartBody.Part,
        descriptionPart: MultipartBody.Part,
        imagePart: MultipartBody.Part?
    ): CallDecorator<Response<Group>> {
        return CallDecorator(apiService.createGroup(namePart,descriptionPart,imagePart))
    }

    fun updateGroup(
        groupId: String,
        nameRequestBody: MultipartBody.Part?,
        description: MultipartBody.Part?,
        image: MultipartBody.Part?,
    ): CallDecorator<Response<Group>> {
        return CallDecorator(apiService.updateGroup(groupId, description, nameRequestBody, image))
    }

    fun getGroups(): CallDecorator<Response<List<Group>>> {
        return CallDecorator(apiService.getGroups())
    }

    fun discoverGroups(searchPattern: String?): CallDecorator<Response<List<Group>>> {
        return CallDecorator(
            apiService.discoverGroups(
                searchPattern
            )
        )
    }

    fun joinGroup(groupId: String): CallDecorator<Response<Group>> {
        return CallDecorator(
            apiService.joinGroup(
                groupId
            )
        )
    }

    fun getAnnouncements(group: Group): CallDecorator<Response<List<Announcement>>> {
        return CallDecorator(
            apiService.getAnnouncements(
                group.id!!
            )
        )
    }

    fun editAnnouncement(announcement: Announcement): CallDecorator<Response<Announcement>> {
        return CallDecorator(
            apiService.updateAnnouncement(
                announcement.id!!,
                announcement
            )
        )
    }

    fun createAnnouncement(
        group: Group,
        announcement: Announcement,
    ): CallDecorator<Response<Announcement>> {
        return CallDecorator(
            apiService.createAnnouncements(
                group.id!!,
                announcement
            )
        )
    }

    fun deleteAnnouncement(announcement: Announcement): CallDecorator<Response<Announcement>> {
        return CallDecorator(
            apiService.deleteAnnouncement(
                announcement.id!!
            )
        )
    }

    fun getGroupMembers(group: Group) =
        CallDecorator(
            apiService.getGroupMembers(group.id!!)
        )

    fun removeMemberFromGroup(userId: String, group: Group): CallDecorator<Any> {
        return CallDecorator(
            apiService.removeMemberFromGroup(
                group.id!!,
                userId
            )
        )
    }

    fun sendMemberInvitation(
        groupId: String,
        username: String,
        groupInvitation: GroupInvitation,
    ): CallDecorator<Response<GroupInvitation>> {
        return CallDecorator(apiService.inviteToGroup(groupId, username, groupInvitation))
    }

    fun getGroupInvitations(): CallDecorator<Response<List<GroupInvitation>>> {
        return CallDecorator(apiService.getGroupInvitations())
    }

    fun respondToGroupInvitation(
        groupInvitationId: String,
        accept: Boolean,
    ): CallDecorator<Response<GroupInvitation>> {
        return CallDecorator(apiService.respondToGroupInvitation(groupInvitationId, accept))
    }

    fun deleteGroup(group: Group): CallDecorator<Void> {
        return CallDecorator(apiService.deleteGroup(group.id!!))
    }

    fun getGroupChat(
        groupId: String,
        currentPage: Int,
        pageSize: Int,
    ): CallDecorator<Response<MessagePage>> {
        return CallDecorator(apiService.getGroupChat(groupId, currentPage, pageSize))
    }

    fun translateMessage(message: String, local: String): CallDecorator<Response<String>> {
        return CallDecorator(apiService.translateMessage(message, local))
    }
}
