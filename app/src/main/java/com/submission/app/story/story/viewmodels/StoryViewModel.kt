package com.submission.app.story.story.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.*
import com.google.gson.Gson
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.auth.models.LoginResult
import com.submission.app.story.shared.models.GenericResponse
import com.submission.app.story.shared.utils.Event
import com.submission.app.story.story.StoryRepository
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

class StoryViewModel(
    private val storyRepository: StoryRepository,
    private val pref: AuthPref
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStories(token: String) = storyRepository.getStories(token)

    fun uploadImage(token: String, getFile: File, desc: String) = storyRepository.uploadImage(token, getFile, desc)

    fun getCredential(): LiveData<LoginResult> {
        return pref.getCredential().asLiveData()
    }

    fun signOut() {
        viewModelScope.launch {
            pref.deleteCredential()
        }
    }
}