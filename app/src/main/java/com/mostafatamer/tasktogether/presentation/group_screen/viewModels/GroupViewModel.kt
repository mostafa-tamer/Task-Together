package com.mostafatamer.tasktogether.presentation.group_screen.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mostafatamer.tasktogether.domain.model.Group

abstract class GroupViewModel : ViewModel() {
    var group by mutableStateOf(Group())



    fun init(group: Group) {
        this.group = group
    }
}