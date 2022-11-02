package com.submission.app.story.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.submission.app.story.auth.models.AuthModel
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.auth.models.LoginResponse
import com.submission.app.story.shared.models.GenericResponse
import com.submission.app.story.shared.utils.Result
import java.lang.Exception

class AuthRepository(
    private val authRequest: AuthRequest,
) {
    fun onRegister(authModel: AuthModel): LiveData<Result<GenericResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = authRequest.postRegister(
                authModel.name,
                authModel.email,
                authModel.password
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun onLogin(authModel: AuthModel): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = authRequest.postLogin(
                authModel.email,
                authModel.password
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            emit(Result.Error(errorMessage))
        }
    }
}