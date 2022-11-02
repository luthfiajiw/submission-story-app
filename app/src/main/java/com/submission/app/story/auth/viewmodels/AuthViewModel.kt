package com.submission.app.story.auth.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.app.story.auth.AuthRepository
import com.submission.app.story.auth.models.AuthModel
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.auth.models.LoginResult
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val pref: AuthPref
) : ViewModel() {
    fun register(authModel: AuthModel) = authRepository.onRegister(authModel)

    fun login(authModel: AuthModel) = authRepository.onLogin(authModel)

    fun onSaveCredentials(loginResult: LoginResult) {
        viewModelScope.launch {
            pref.saveCredential(loginResult)
        }
    }
}