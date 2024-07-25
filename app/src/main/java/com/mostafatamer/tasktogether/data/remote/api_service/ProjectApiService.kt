package com.mostafatamer.tasktogether.data.remote.api_service


import com.mostafatamer.tasktogether.domain.model.Colleague
import com.mostafatamer.tasktogether.domain.model.Project
import com.mostafatamer.tasktogether.domain.model.ProjectStatistics
import com.mostafatamer.tasktogether.domain.model.Response
import com.mostafatamer.tasktogether.domain.model.Task
import com.mostafatamer.tasktogether.domain.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectApiService {
    @GET("groups/{group_id}/projects")
    fun getProjects(@Path("group_id") id: String): Call<Response<List<Project>>>

    @POST("/groups/{group_id}/projects")
    fun createProject(
        @Path("group_id") id: String,
        @Body project: Project,
    ): Call<Response<Project>>

    @GET("projects/{Project_id}/candidates")
    fun getProjectCandidates(@Path("Project_id") projectId: String): Call<Response<List<User>>>

    @GET("projects/{Topic_ID}/members")
    fun getProjectMembers(@Path("Topic_ID") topicId: String): Call<Response<List<User>>>

    @POST("projects/{projectId}/members")
    fun addCandidates(
        @Path("projectId") project: String,
        @Body usersIds: List<String>,
    ): Call<Response<List<String>>>

    @GET("projects/{Project_ID}/memberStatistics")
    fun getProjectMembersStatistics(
        @Path("Project_ID") projectId: String,
    ): Call<Response<List<Colleague>>>

    @GET("projects/{Project_ID}/projectStatistics")
    fun getProjectDashboard(
        @Path("Project_ID") projectId: String,
    ): Call<Response<ProjectStatistics>>

    @POST("projects/{project_ID}/tasks")
    fun assignTask(
        @Path("project_ID") projectId: String,
        @Body task: Task,
    ): Call<Response<Task>>

    @PATCH("/tasks/{task_id}/tick")
    fun tick(@Path("task_id") taskId: String): Call<Response<Task>>

    @GET("/projects/{projectId}/tasks")
    fun getTasks(@Path("projectId") projectId: String): Call<Response<List<Task>>>

    @DELETE("/projects/{Project_ID}/members")
    fun removeMemberFromProject(
        @Path("Project_ID") projectId: String,
        @Query("userId") memberId: String,
    ): Call<Void>

    @DELETE("/projects/{Project_ID}")
    fun deleteProject(@Path("Project_ID") projectId: String): Call<Any>

    @DELETE("/tasks/{task_id}")
    fun deleteTask(@Path("task_id") taskId: String): Call<Any>

    @PATCH("projects/{project_id}")
    fun updateProject(
        @Path("project_id") projectId: String,
        @Body project: Project,
    ): Call<Response<Project>>
}
