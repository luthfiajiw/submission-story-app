package com.submission.app.story.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.submission.app.story.MainDispatcherRule
import com.submission.app.story.auth.models.AuthModel
import com.submission.app.story.shared.utils.Result
import com.submission.app.story.utils.DataDummy
import com.submission.app.story.utils.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var authRequest: AuthRequest
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        authRequest = FakeAuthRequest()
        authRepository = AuthRepository(authRequest)
    }

    @Test
    fun `when Register should not error`() = runTest {
        val registerModel = AuthModel(
            name = "name",
            email = "name@email.com",
            password = "123456"
        )

        val expectedResponse = DataDummy.dummyRegisterResponse()
        val actualResponse = authRepository.onRegister(registerModel)

        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse)
            Assert.assertEquals(
                expectedResponse.error,
                (actualResponse.value as Result.Success).data.error
            )
        }
    }

    @Test
    fun `when Login should not error`() = runTest {
        val loginModel = AuthModel(
            name = "",
            email = "name@email.com",
            password = "123456"
        )

        val expectedResponse = DataDummy.dummyLoginResponse()
        val actualResponse = authRepository.onLogin(loginModel)

        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse)
            Assert.assertEquals(
                expectedResponse.error,
                (actualResponse.value as Result.Success).data.error
            )
        }
    }
}