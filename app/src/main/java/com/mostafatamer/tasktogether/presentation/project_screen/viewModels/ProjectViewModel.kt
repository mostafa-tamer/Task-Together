package com.mostafatamer.tasktogether.presentation.project_screen.viewModels

import androidx.lifecycle.ViewModel
import com.mostafatamer.tasktogether.domain.model.Group
import com.mostafatamer.tasktogether.domain.model.Project

abstract class ProjectViewModel : ViewModel() {

    lateinit var project: Project
    lateinit var group: Group

    open fun init(project: Project, group: Group) {
        this.project = project
        this.group = group
    }
}