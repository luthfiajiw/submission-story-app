package com.submission.app.story.story.viewmodels

import androidx.lifecycle.*
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.auth.models.LoginResult
import com.submission.app.story.shared.utils.Result
import com.submission.app.story.story.StoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File

class StoryViewModel(
    private val storyRepository: StoryRepository,
    private val pref: AuthPref
) : ViewModel() {

    fun getStories(token: String, location: Int) = storyRepository.getStories(token, location).cachedIn(viewModelScope)

    fun getStoriesWithLocation(token: String) = storyRepository.getStoriesWithLocation(token)

    fun uploadImage(token: String, getFile: File, desc: String, lat: Double, lng: Double) = storyRepository.uploadImage(token, getFile, desc, lat, lng)

    fun loadStateListener(loadStates: CombinedLoadStates) : LiveData<Result<Unit>> = liveData {
        when(val result = loadStates.source.refresh) {
            is LoadState.Loading -> emit(Result.Loading)
            is LoadState.NotLoading -> emit(Result.Success(Unit))
            is LoadState.Error -> {
                val error = result.error.message?.split(" - ")
                val message = error?.get(0)
                emit(Result.Error(message.toString()))
            }
        }
    }

    fun getCredential(): LiveData<LoginResult> {
        return pref.getCredential().asLiveData()
    }

    fun signOut() {
        viewModelScope.launch {
            pref.deleteCredential()
        }
    }
}