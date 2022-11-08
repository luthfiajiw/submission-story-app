package com.submission.app.story.utils

import com.submission.app.story.auth.models.LoginResponse
import com.submission.app.story.auth.models.LoginResult
import com.submission.app.story.shared.models.GenericResponse

object DataDummy {
    fun generateDummyCredential(): LoginResult {
        return LoginResult(
            userId = "user_id",
            token = "token",
            name = "name"
        )
    }

    fun dummyRegisterResponse(): GenericResponse {
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
}