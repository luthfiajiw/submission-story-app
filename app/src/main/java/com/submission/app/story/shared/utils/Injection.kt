package com.submission.app.story.shared.utils

import com.submission.app.story.auth.AuthRepository
import com.submission.app.story.auth.AuthService
import com.submission.app.story.story.StoryRepository
import com.submission.app.story.story.StoryService

object Injection {
    fun provideAuthRepository(): AuthRepository {
        val authRequest = AuthService().getAuthRequest()
        return AuthRepository(authRequest)
    }

    fun provideStoryRepository(): StoryRepository {
        val storyRequest = StoryService().getStoryRequest()
        return StoryRepository(storyRequest)
    }
}