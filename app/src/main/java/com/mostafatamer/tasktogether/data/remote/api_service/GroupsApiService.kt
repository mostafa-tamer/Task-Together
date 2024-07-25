package com.mostafatamer.tasktogether.data.remote.api_service

import com.mostafatamer.tasktogether.domain.model.Announcement
import com.mostafatamer.tasktogether.domain.model.Group
import com.mostafatamer.tasktogether.domain.model.GroupInvitation
import com.mostafatamer.tasktogether.domain.model.MessagePage
import com.mostafatamer.tasktogether.domain.model.Response
import com.mostafatamer.tasktogether.domain.model.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupsApiService : BaseApiService {
    @GET("/groups")
    fun getGroups(): Call<Response<List<Group>>>

    @Multipart
    @POST("/groups")
    fun createGroup(
        @Part group: MultipartBody.Part,
        @Part descriptionPart: MultipartBody.Part,
        @Part imagePart: MultipartBody.Part?,
    ): Call<Response<Group>>

    @Multipart
    @PATCH("groups/{GROUP_ID}")
    fun updateGroup(
        @Path("GROUP_ID") groupId: String,
        @Part name: MultipartBody.Part?,
        @Part description: MultipartBody.Part?,
        @Part image: MultipartBody.Part?,
    ): Call<Response<Group>>

    @GET("/groups/discover")
    fun discoverGroups(@Query("search") searchPattern: String?): Call<Response<List<Group>>>

    @POST("/groups/{id}/join")
    fun joinGroup(@Path("id") groupId: String): Call<Response<Group>>

    @GET("/groups/{GROUP_ID}/announcements")
    fun getAnnouncements(@Path("GROUP_ID") groupId: String): Call<Response<List<Announcement>>>

    @POST("/groups/{GROUP_ID}/announcements")
    fun createAnnouncements(
        @Path("GROUP_ID") groupId: String,
        @Body announcement: Announcement,
    ): Call<Response<Announcement>>

    @DELETE("/announcements/{Announcement_ID}")
    fun deleteAnnouncement(
        @Path("Announcement_ID") announcementId: String,
    ): Call<Response<Announcement>>

    @PATCH("announcements/{Announcement_ID}")
    fun updateAnnouncement(
        @Path("Announcement_ID") announcementId: String,
        @Body announcement: Announcement,
    ): Call<Response<Announcement>>

    @GET("groups/{GROUP_ID}/members")
    fun getGroupMembers(@Path("GROUP_ID") groupId: String): Call<Response<List<User>>>


    @DELETE("groups/{GROUP_ID}/members")
    fun removeMemberFromGroup(
        @Path("GROUP_ID") groupId: String,
        @Query("userId") userId: String,
    ): Call<Any>

    @POST("/groups/{GROUP_ID}/invite")
    fun inviteToGroup(
        @Path("GROUP_ID") groupId: String,
        @Query("username") username: String,
        @Body groupInvitation: GroupInvitation,
    ): Call<Response<GroupInvitation>>

    @GET("groups/groupInvites")
    fun getGroupInvitations(): Call<Response<List<GroupInvitation>>>

    //{{URL}}/groups/groupInvites/{{GroupInvite_ID}}?accept=true
    // HTTP Post
    @POST("groups/groupInvites/{GroupInvite_ID}")
    fun respondToGroupInvitation(
        @Path("GroupInvite_ID") groupId: String,
        @Query("accept") accept: Boolean,
    ): Call<Response<GroupInvitation>>

    //{{URL}}/groups/6687007f2655c145c50cb5ac/messages?pageSize=10&page=6
    @GET("groups/{GROUP_ID}/messages")
    fun getGroupChat(
        @Path("GROUP_ID") groupId: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): Call<Response<MessagePage>>

    /*
    HTTP DELETE/PATCH

    URL/groups/{{GROUP_ID}

    URL/projects/{{Project_ID}}
     */
    @DELETE("/groups/{GROUP_ID}")
    fun deleteGroup(@Path("GROUP_ID") groupId: String): Call<Void>

    //{{URL}}/messages/translation?text=I am youssef I love computer science and my friends and I go to school by bus&targetLang=en
    @GET("/messages/translation")
    fun translateMessage(
        @Query("text") text: String,
        @Query("targetLang") targetLang: String,
    ): Call<Response<String>>

}