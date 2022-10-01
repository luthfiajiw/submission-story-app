package com.submission.app.story.story.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.auth.models.LoginResult
import com.submission.app.story.story.StoryResponse
import com.submission.app.story.story.StoryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(private val pref: AuthPref) : ViewModel() {
    private val storyService = StoryService().getStoryRequest()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _storyResponse = MutableLiveData<StoryResponse?>()
    val storyResponse : LiveData<StoryResponse?> = _storyResponse

    fun getCredential(): LiveData<LoginResult> {
        return pref.getCredential().asLiveData()
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
}