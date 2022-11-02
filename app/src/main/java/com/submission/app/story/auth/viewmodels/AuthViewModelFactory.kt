package com.submission.app.story.auth.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.submission.app.story.auth.AuthRepository
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.shared.utils.Injection

class AuthViewModelFactory(
    private val authRepository: AuthRepository,
    private val pref: AuthPref
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: AuthViewModelFactory? = null

        @JvmStatic
        fun getInstance(pref: AuthPref): AuthViewModelFactory {
            if (INSTANCE == null) {
                synchronized(AuthViewModelFactory::class.java) {
                    INSTANCE = AuthViewModelFactory(Injection.provideAuthRepository(), pref)
                }
            }

            return INSTANCE as AuthViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository, pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}