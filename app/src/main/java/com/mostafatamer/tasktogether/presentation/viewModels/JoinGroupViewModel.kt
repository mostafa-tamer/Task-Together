package com.mostafatamer.tasktogether.presentation.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mostafatamer.tasktogether.data.repositories.GroupsRepository
import com.mostafatamer.tasktogether.domain.model.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JoinGroupViewModel @Inject constructor(private val groupsRepository: GroupsRepository) :
    ViewModel() {

    private var _groups by mutableStateOf(listOf<Group>())

    val groups get() = _groups
    fun getGroupsToJoin(searchPattern: String) {
        groupsRepository.discoverGroups(searchPattern)
            .setOnSuccess {
                _groups = it.data
            }.execute()
    }

    fun getAllAvailableGroups() {
        getGroupsToJoin("")
    }

    var groupToJoin: Group? by mutableStateOf(null)
    fun joinGroup(group: Group) {
        groupsRepository.joinGroup(group.id!!)
            .setOnSuccess {
                groupToJoin = it.data
            }.execute()
    }

    var showSearchBar by mutableStateOf(false)
}
