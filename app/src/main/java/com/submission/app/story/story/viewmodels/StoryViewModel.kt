package com.submission.app.story.story.viewmodels

import androidx.lifecycle.*
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.auth.models.LoginResult
import com.submission.app.story.story.StoryRepository
import kotlinx.coroutines.launch
import java.io.File

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