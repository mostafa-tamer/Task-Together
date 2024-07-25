package com.mostafatamer.tasktogether.presentation.navigation

import com.mostafatamer.tasktogether.domain.model.Group
import com.mostafatamer.tasktogether.domain.model.Project
import com.mostafatamer.tasktogether.domain.model.User
import com.mostafatamer.tasktogether.jsonConverter

const val GROUP = "group"
const val PROJECT = "topic"
const val TASK = "task"
const val USER = "user"

sealed class MainNavRoutes(val route: String) {

    data object MainScreen : MainNavRoutes("main_screen")

    data object Login : MainNavRoutes("login")
    data object SignUp : MainNavRoutes("sign_up")
    data object JoinGroup : MainNavRoutes("join_group")
    data object CreateGroup : MainNavRoutes("create_group")
    data object GroupMainNavHost : MainNavRoutes("subjects/{$GROUP}") {
        fun passGroup(group: Group): String {
            group.photo = group.photo?.replace('/', '\\')   //the slash makes a navigation confusion

            group.members?.forEach {
                it.photo = it.photo?.replace('/', '\\')
            }

            val serializedGroup = jsonConverter.toJson(group)
            return route.replace("{$GROUP}", serializedGroup)
        }
    }

    data object ProjectNavRoute : MainNavRoutes("topics/{$GROUP}/{$PROJECT}") {
        fun navigateToTasks(group: Group, project: Project): String {
            val serializedGroup = jsonConverter.toJson(group)
            val serializedTopic = jsonConverter.toJson(project)
            return route.replace("{$GROUP}", serializedGroup).replace("{$PROJECT}", serializedTopic)
        }
    }

    data object CreateTopic : MainNavRoutes("create_subject/{$GROUP}") {
        fun navigateToCreateTopics(group: Group): String {
            val serializedGroup = jsonConverter.toJson(group)
            return route.replace("{$GROUP}", serializedGroup)
        }
    }

    data object AddMembersToProject : MainNavRoutes("add_topics_member/{$PROJECT}/{$GROUP}") {
        fun withProjectAndGroup(project: Project, group: Group): String {
            val serializedTopic = jsonConverter.toJson(project)
            val serializedGroup = jsonConverter.toJson(group)
            return route.replace("{$PROJECT}", serializedTopic).replace("{$GROUP}", serializedGroup)
        }
    }

    data object ProjectInfo : MainNavRoutes("project_info/{$PROJECT}") {
        fun withProject(project: Project): String {
            val serializedTopic = jsonConverter.toJson(project)
            return route.replace("{$PROJECT}", serializedTopic)
        }
    }

    data object AddUserTasks : MainNavRoutes("add_user_tasks/{$PROJECT}/{$USER}") {
        fun withProjectAndUser(project: Project, member: User): String {
            val newMember = member.copy()
            newMember.photo = null
            val serializedProject = jsonConverter.toJson(project)
            val serializedUser = jsonConverter.toJson(newMember)

            return route.replace("{$PROJECT}", serializedProject).replace("{$USER}", serializedUser)
        }
    }
}
