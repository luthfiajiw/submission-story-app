package com.submission.app.story.auth

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.submission.app.story.auth.models.AuthModel
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.auth.models.LoginResponse
import com.submission.app.story.auth.models.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(application: Application, pref: AuthPref) : ViewModel() {
    private val _application = application

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerResponse = MutableLiveData<RegisterResponse?>()
    val registerResponse: MutableLiveData<RegisterResponse?> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    fun onRegister(authModel: AuthModel) {
        _isLoading.postValue(true)
        val service = AuthService().getAuthRequest().postRegister(
            authModel.name,
            authModel.email,
            authModel.password
        )

        service.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _isLoading.postValue(false)
                        _registerResponse.postValue(responseBody)
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.postValue(false)
            }

        })
    }
}