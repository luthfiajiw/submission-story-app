package com.submission.app.story.shared.utils

import com.submission.app.story.auth.AuthRepository
import com.submission.app.story.auth.AuthService

object Injection {
    fun provideAuthRepository(): AuthRepository {
        val authService = AuthService().getAuthRequest()
        return AuthRepository(authService)
    }
}