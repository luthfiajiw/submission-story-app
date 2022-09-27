package com.submission.app.story.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.auth.models.LoginResult


class SplashViewModel(private val pref: AuthPref) : ViewModel() {
    fun getCredential(): LiveData<LoginResult> {
        return pref.getCredential().asLiveData()
    }
}