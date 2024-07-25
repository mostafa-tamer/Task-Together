package com.mostafatamer.tasktogether.data.repositories;

import com.mostafatamer.tasktogether.data.remote.api_service.ProjectApiService
import com.mostafatamer.tasktogether.domain.model.Colleague
import com.mostafatamer.tasktogether.domain.model.Project
import com.mostafatamer.tasktogether.domain.model.ProjectStatistics
import com.mostafatamer.tasktogether.domain.model.Response
import com.mostafatamer.tasktogether.domain.model.Task
import com.mostafatamer.tasktogether.domain.model.User
import com.mostafatamer.tasktogether.lib.CallDecorator


class ProjectRepository(private val apiService: ProjectApiService) {

    fun getProjects(categoryId: String): CallDecorator<Response<List<Project>>> {
        return CallDecorator(
            apiService.getProjects(
                categoryId
            )
        )
    }

    fun createProject(categoryId: String, project: Project): CallDecorator<Response<Project>> {
        return CallDecorator(
            apiService.createProject(
                categoryId,
                project
            )
        )
    }


    fun getTopicCandidates(group: Project): CallDecorator<Response<List<User>>> =
        CallDecorator(apiService.getProjectCandidates(group.id!!))

    fun getMembers(project: Project): CallDecorator<Response<List<User>>> =
        CallDecorator(
            apiService.getProjectMembers(
                project.id!!
            )
        )

    fun addMembers(
        project: Project,
        usersIds: List<String>,
    ): CallDecorator<Response<List<String>>> =
        CallDecorator(
            apiService.addCandidates(project.id!!, usersIds)
        )


    fun getMembersStatistics(projectId: String): CallDecorator<Response<List<Colleague>>> {
        return CallDecorator(
            apiService.getProjectMembersStatistics(
                projectId
            )
        )
    }

    fun getProjectDashBoard(projectId: String): CallDecorator<Response<ProjectStatistics>> {
        return CallDecorator(
            apiService.getProjectDashboard(
                projectId
            )
        )
    }


    fun assignTask(
        projectId: String,
        task: Task,
    ): CallDecorator<Response<Task>> {
        return CallDecorator(apiService.assignTask(projectId, task))
    }


    fun tick(taskId: String) =
        CallDecorator(apiService.tick(taskId))

    fun getTasks(projectId: String) =
        CallDecorator(
            apiService.getTasks(projectId)
        )

    fun removeMemberFromProject(
        projectId: String,
        memberId: String,
    ): CallDecorator<Void> {
        return CallDecorator(
            apiService.removeMemberFromProject(
                projectId,
                memberId
            )
        )
    }

    fun deleteProject(projectId: String): CallDecorator<Any> {
        return CallDecorator(apiService.deleteProject(projectId))
    }

    fun deleteTask(task: Task): CallDecorator<Any> {
        return CallDecorator(apiService.deleteTask(task.id!!))
    }

    fun updateProject(project: Project): CallDecorator<Response<Project>> {
        return CallDecorator(apiService.updateProject(project.id!!, project))
    }
}