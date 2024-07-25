package com.mostafatamer.tasktogether.presentation.group_screen.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mostafatamer.tasktogether.data.repositories.GroupsRepository
import com.mostafatamer.tasktogether.domain.model.Announcement
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnnouncementViewModel @Inject constructor(
    private val groupsRepository: GroupsRepository,
) : GroupViewModel() {

    class AnnouncementState {
        var selectedItemIndex: Int by mutableIntStateOf(-1)
        var title: String by mutableStateOf("")
        var description: String by mutableStateOf("")
        fun clean() {
            selectedItemIndex = -1
            title = ""
            description = ""
        }
    }

    var announcements = mutableStateListOf<Announcement>()
        private set

    fun getAnnouncements(onLoad: () -> Unit = {}) {
        groupsRepository.getAnnouncements(group)
            .setOnSuccess {
                if (announcements.isEmpty()) {
                    announcements.addAll(it.data.reversed())
                }
                onLoad()
            }.execute()
    }

    var showDeleteAlert by mutableStateOf(false)
    var showEditAlert by mutableStateOf(false)
    var showCreateAlert by mutableStateOf(false)
    fun editAnnouncement() {
        val announcement = Announcement(
            id = announcements[announcementState.selectedItemIndex].id,
            title = announcementState.title,
            description = announcementState.description
        )
        groupsRepository.editAnnouncement(announcement)
            .setOnSuccess {
                announcements[announcementState.selectedItemIndex] = it.data
                announcementState.clean()
                showEditAlert = false
            }.execute()
    }


    fun deleteAnnouncement() {
        val announcement = announcements[announcementState.selectedItemIndex]
        groupsRepository.deleteAnnouncement(announcement)
            .setOnSuccess {
                announcements.removeAt(announcementState.selectedItemIndex)
                showDeleteAlert = false
            }.execute()
    }

    var announcementState by mutableStateOf(AnnouncementState())

    fun createAnnouncement() {
        val announcement = Announcement(
            title = announcementState.title,
            description = announcementState.description
        )

        groupsRepository.createAnnouncement(group, announcement)
            .setOnSuccess {
                announcements.add(it.data)
                announcementState.clean()
                showCreateAlert = false
            }.execute()
    }
}