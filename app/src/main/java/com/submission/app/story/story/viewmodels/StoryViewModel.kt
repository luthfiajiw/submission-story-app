package com.submission.app.story.story.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.*
import com.google.gson.Gson
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.auth.models.LoginResult
import com.submission.app.story.shared.models.GenericResponse
import com.submission.app.story.shared.utils.Event
import com.submission.app.story.story.StoryResponse
import com.submission.app.story.story.StoryService
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class StoryViewModel(private val pref: AuthPref) : ViewModel() {
    private val storyService = StoryService().getStoryRequest()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _storyResponse = MutableLiveData<StoryResponse?>()
    val storyResponse : LiveData<StoryResponse?> = _storyResponse

    private val _genericResponse = MutableLiveData<Event<GenericResponse>>()
    val genericResponse: LiveData<Event<GenericResponse>> = _genericResponse

    fun getCredential(): LiveData<LoginResult> {
        return pref.getCredential().asLiveData()
    }

    fun signOut() {
        viewModelScope.launch {
            pref.deleteCredential()
        }
    }

    fun getStories(token: String) {
        _isLoading.postValue(true)
        val service = storyService.getStories(token)

        service.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _storyResponse.postValue(responseBody)
                        _isLoading.postValue(false)
                    } else _isLoading.postValue(false)

                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.postValue(false)
            }
        })
    }

    fun uploadImage(token: String, getFile: File, desc: String) {
        _isLoading.postValue(true)
        val file = reduceFileImage(getFile as File)

        val description =   desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        val service = storyService.uploadImage("Bearer $token", imageMultipart, description)
        service.enqueue(object : Callback<GenericResponse> {
            override fun onResponse(
                call: Call<GenericResponse>,
                response: Response<GenericResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _genericResponse.postValue(Event(responseBody))
                    }

                } else {
                    val errorBody = response.errorBody()
                    if (errorBody != null) {
                        val response = Gson().fromJson<GenericResponse>(errorBody.string(), GenericResponse::class.java)
                        _genericResponse.postValue(Event(response))
                    }
                }

                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                _isLoading.postValue(false)
                val response = GenericResponse(
                    error = true,
                    message = "Gagal instance retrofit"
                )
                _genericResponse.postValue(Event(response))
            }


        })
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