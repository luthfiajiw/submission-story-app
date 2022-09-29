package com.submission.app.story.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.submission.app.story.auth.models.AuthModel
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.auth.models.LoginResponse
import com.submission.app.story.shared.models.GenericResponse
import com.submission.app.story.shared.utils.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val pref: AuthPref) : ViewModel() {
    private val authService = AuthService().getAuthRequest()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _genericResponse = MutableLiveData<Event<GenericResponse>>()
    val genericResponse: MutableLiveData<Event<GenericResponse>> = _genericResponse

    private val _loginResponse = MutableLiveData<Event<LoginResponse>>()
    val loginResponse: LiveData<Event<LoginResponse>> = _loginResponse

    fun onRegister(authModel: AuthModel) {
        _isLoading.postValue(true)
        val service = authService.postRegister(
            authModel.name,
            authModel.email,
            authModel.password
        )

        service.enqueue(object : Callback<GenericResponse> {
            override fun onResponse(
                call: Call<GenericResponse>,
                response: Response<GenericResponse>,
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _genericResponse.postValue(Event(responseBody))
                    }
                } else {
                    val errorBody = response.errorBody()
                    if (errorBody != null) {
                        val response = Gson().fromJson<GenericResponse>(errorBody.string(), GenericResponse::class.java)
                        _genericResponse.postValue(Event(response))
                    }
                }

                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                _isLoading.postValue(false)
                val response = GenericResponse(
                    error = true,
                    message = "Gagal instance retrofit"
                )
                _genericResponse.postValue(Event(response))
            }

        })
    }

    fun onLogin(authModel: AuthModel) {
        _isLoading.postValue(true)
        val service = authService.postLogin(
            authModel.email,
            authModel.password
        )

        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _loginResponse.postValue(Event(responseBody))
                        viewModelScope.launch {
                            pref.saveCredential(responseBody.loginResult)
                        }
                    }
                } else {
                    val errorBody = response.errorBody()
                    if (errorBody != null) {
                        val response = Gson().fromJson(errorBody.string(), GenericResponse::class.java)
                        _genericResponse.postValue(Event(response))
                    }
                }

                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.postValue(false)
                val response = GenericResponse(
                    error = true,
                    message = "Gagal instance retrofit"
                )
                _genericResponse.postValue(Event(response))
            }

        })
    }
}