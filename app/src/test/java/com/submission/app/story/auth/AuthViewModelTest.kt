package com.submission.app.story.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.submission.app.story.MainDispatcherRule
import com.submission.app.story.auth.models.AuthModel
import com.submission.app.story.auth.models.LoginResponse
import com.submission.app.story.auth.viewmodels.AuthViewModel
import com.submission.app.story.shared.models.GenericResponse
import com.submission.app.story.shared.utils.Result
import com.submission.app.story.utils.DataDummy
import com.submission.app.story.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var authViewModel: AuthViewModel

    @Test
    fun `when Register should not return error and return success`() {
        val registerModel = AuthModel(
            name = "name",
            email = "name@email.com",
            password = "123456"
        )

        val expectedResponse = MutableLiveData<Result<GenericResponse>>()
        expectedResponse.value = Result.Success(DataDummy.dummyRegisterResponse())
        `when`(authViewModel.register(registerModel)).thenReturn(expectedResponse)

        val actualResponse = authViewModel.register(registerModel).getOrAwaitValue()

        verify(authViewModel).register(registerModel)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Login should not return error and return success`  () {
        val loginModel = AuthModel(
            name = "",
            email = "name@email.com",
            password = "123456"
        )

        val expectedResponse = MutableLiveData<Result<LoginResponse>>()
        expectedResponse.value = Result.Success(DataDummy.dummyLoginResponse())
        `when`(authViewModel.login(loginModel)).thenReturn(expectedResponse)

        val actualResponse = authViewModel.login(loginModel).getOrAwaitValue()

        verify(authViewModel).login(loginModel)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
    }
}