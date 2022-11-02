package com.submission.app.story.story

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.submission.app.story.shared.models.GenericResponse
import com.submission.app.story.shared.utils.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class StoryRepository(private val storyRequest: StoryRequest) {
    fun getStories(token: String): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = storyRequest.getStories(token)
            emit(Result.Success(response))
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun uploadImage(token: String, getFile: File, desc: String): LiveData<Result<GenericResponse>> = liveData {
        emit(Result.Loading)
        try {
            val file = reduceFileImage(getFile as File)

            val description =   desc.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val response = storyRequest.uploadImage("Bearer $token", imageMultipart, description)
            emit(Result.Success(response))
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)

        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }
}