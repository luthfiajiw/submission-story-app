package com.submission.app.story.utils

import com.submission.app.story.auth.models.LoginResponse
import com.submission.app.story.auth.models.LoginResult
import com.submission.app.story.shared.models.GenericResponse
import com.submission.app.story.story.Story
import com.submission.app.story.story.StoryResponse

object DataDummy {
    fun generateDummyCredential(): LoginResult {
        return LoginResult(
            userId = "user_id",
            token = "token",
            name = "name"
        )
    }

    fun dummySuccessResponse(): GenericResponse {
        return GenericResponse(
            error = false,
            message = "success"
        )
    }

    fun dummyLoginResponse(): LoginResponse {
        return LoginResponse(
            error = false,
            message = "success",
            loginResult = LoginResult(
                token = "token",
                name = "name",
                userId = "user_id"
            )
        )
    }

    fun generateDummyStoryResponse(): StoryResponse {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..10) {
            val story = Story(
                "id$i",
                "name $i",
                "description $i",
                "url",
                0.0,
                0.0,
                "12102022"
            )
            items.add(story)
        }
        return StoryResponse(
            error = false,
            message = "success",
            listStory = items
        )
    }
}