package com.mostafatamer.tasktogether.presentation.group_screen.viewModels

import android.net.Uri
import com.mostafatamer.tasktogether.HttpStatusCode
import com.mostafatamer.tasktogether.data.repositories.GroupsRepository
import com.mostafatamer.tasktogether.data.repositories.ImageRepository
import com.mostafatamer.tasktogether.domain.model.Group
import com.mostafatamer.tasktogether.match
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject


@HiltViewModel
class GroupManagerViewModel @Inject constructor(
    private val groupsRepository: GroupsRepository,
    private val imageRepository: ImageRepository,
) : GroupViewModel() {
    fun deleteGroup(onDeleted: (Boolean) -> Unit) {
        groupsRepository.deleteGroup(group)
            .setOnResponse { _, code ->
                println(code)
                onDeleted(HttpStatusCode.NO_CONTENT.match(code))
            }.execute()
    }


    fun updateGroup(
        name: String?,
        description: String?,
        uri: Uri?,
        onUpdate: (Group?) -> Unit,
    ) {
        val namePart: MultipartBody.Part? =
            name?.let { MultipartBody.Part.createFormData("name", it) }
        val descriptionPart =
            description?.let { MultipartBody.Part.createFormData("description", it) }
        val imagePart = uri?.let { imageRepository.getImageMultiPart("photo",
            "image",it) }

        groupsRepository.updateGroup(group.id!!, namePart, descriptionPart, imagePart)
            .setOnResponse { response, code ->
                println(code)
                if (HttpStatusCode.OK.match(code)) {
                    init(response.data)
                }
                onUpdate(response?.data)
            }.execute()
    }
}

