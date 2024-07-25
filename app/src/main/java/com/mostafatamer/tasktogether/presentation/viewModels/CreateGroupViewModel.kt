package com.mostafatamer.tasktogether.presentation.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.mostafatamer.tasktogether.HttpStatusCode
import com.mostafatamer.tasktogether.data.repositories.GroupsRepository
import com.mostafatamer.tasktogether.data.repositories.ImageRepository
import com.mostafatamer.tasktogether.match
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val groupsRepository: GroupsRepository,
    private val imageRepository: ImageRepository,
) : ViewModel() {

    fun createGroup(
        groupName: String,
        groupDescription: String,
        imageUri: Uri?,
        onCreated: (Boolean) -> Unit,
    ) {
        val namePart = imageRepository.createMultipart("name", groupName)
        val descriptionPart = imageRepository.createMultipart("description", groupDescription)
        val imagePart = imageUri?.let { imageRepository.getImageMultiPart("photo", "image",it) }

        groupsRepository.createGroup(namePart,descriptionPart,imagePart)
            .setOnResponse { response, code ->
                println(code.toString() + " ${response?.message}")
                onCreated(HttpStatusCode.CREATED.match(code))
            }.execute()
    }
}