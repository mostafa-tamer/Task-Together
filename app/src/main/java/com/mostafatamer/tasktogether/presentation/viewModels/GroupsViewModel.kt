package com.mostafatamer.tasktogether.presentation.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mostafatamer.tasktogether.data.repositories.GroupsRepository
import com.mostafatamer.tasktogether.domain.model.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    private val groupsRepository: GroupsRepository,
) : ViewModel() {

    var showEmptyList: Boolean by mutableStateOf(false)

    val groups = mutableStateListOf<Group>()

    fun getGroups(onLoad: () -> Unit = {}) {
        groupsRepository.getGroups()
            .setOnSuccess {
                groups.clear()
                groups.addAll(it.data)
                showEmptyList = groups.isEmpty()
                onLoad()
            }.execute()
    }
}