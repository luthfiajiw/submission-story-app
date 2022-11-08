package com.submission.app.story.auth

import com.submission.app.story.auth.models.LoginResponse
import com.submission.app.story.auth.models.LoginResult
import com.submission.app.story.shared.models.GenericResponse

class FakeAuthRequest : AuthRequest {
    override suspend fun postRegister(
        name: String,
        email: String,
        password: String
    ): GenericResponse {
        return GenericResponse(
            error = false,
            message = "success"
        )
    }

    override suspend fun postLogin(email: String, password: String): LoginResponse {
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