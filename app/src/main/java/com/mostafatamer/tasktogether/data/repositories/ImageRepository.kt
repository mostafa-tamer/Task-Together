package com.mostafatamer.tasktogether.data.repositories

import android.app.Application
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ImageRepository(private val application: Application) {

    fun getImageMultiPart(name: String, filename: String, imageUri: Uri): MultipartBody.Part {
        val stream = application.contentResolver.openInputStream(imageUri)!!
        val requestBody = stream.readBytes().toRequestBody("image/jpeg".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData(name, filename, requestBody)
        return photoPart
    }

    fun createMultipart(name: String, value: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(name, value)
    }
}