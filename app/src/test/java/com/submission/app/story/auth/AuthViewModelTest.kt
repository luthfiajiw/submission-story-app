package com.submission.app.story.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.submission.app.story.MainDispatcherRule
import com.submission.app.story.auth.models.AuthModel
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.auth.models.LoginResponse
import com.submission.app.story.auth.viewmodels.AuthViewModel
import com.submission.app.story.shared.models.GenericResponse
import com.submission.app.story.shared.utils.Result
import com.submission.app.story.utils.DataDummy
import com.submission.app.story.utils.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var authViewModel: AuthViewModel
    @Mock
    private lateinit var authPref: AuthPref
    @Mock
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        authViewModel = AuthViewModel(authRepository, authPref)
    }

    @Test
    fun `when Register should not return null and return success`() {
        val registerModel = AuthModel(
            name = "name",
            email = "name@email.com",
            password = "123456"
        )

        val expectedResponse = MutableLiveData<Result<GenericResponse>>()
        expectedResponse.value = Result.Success(DataDummy.dummySuccessResponse())
        `when`(authRepository.onRegister(registerModel)).thenReturn(expectedResponse)

        val actualResponse = authViewModel.register(registerModel).getOrAwaitValue()

        verify(authRepository).onRegister(registerModel)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Login should not error and return login result`  () {
        val loginModel = AuthModel(
            name = "",
            email = "name@email.com",
            password = "123456"
        )

        val expectedResponse = MutableLiveData<Result<LoginResponse>>()
        expectedResponse.value = Result.Success(DataDummy.dummyLoginResponse())
        `when`(authRepository.onLogin(loginModel)).thenReturn(expectedResponse)

        val actualResponse = authViewModel.login(loginModel).getOrAwaitValue()

        verify(authRepository).onLogin(loginModel)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
        Assert.assertEquals(
            (expectedResponse.value as Result.Success).data.loginResult,
            (actualResponse as Result.Success).data.loginResult
        )
    }

    @Test
    fun `verify Save Credential is working`() = runTest {
        val dummyLoginResult = DataDummy.dummyLoginResponse().loginResult
        authViewModel.onSaveCredentials(dummyLoginResult)
        verify(authPref, times(1)).saveCredential(dummyLoginResult)
    }
}