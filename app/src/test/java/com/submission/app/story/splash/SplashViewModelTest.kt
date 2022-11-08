package com.submission.app.story.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.submission.app.story.MainDispatcherRule
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.auth.models.LoginResult
import com.submission.app.story.utils.DataDummy
import com.submission.app.story.utils.getOrAwaitValue
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {

    @Mock
    private lateinit var splashViewModel: SplashViewModel

    @Mock
    private lateinit var authPref: AuthPref
    private val dummyCredential = DataDummy.generateDummyCredential()

    @Before
    fun setUp() {
        splashViewModel = SplashViewModel(authPref)
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Get Credential should not null and return The Credential`() {
        val expectedCredential = MutableLiveData<LoginResult>()
        expectedCredential.value = dummyCredential
        `when`(authPref.getCredential()).thenReturn(flowOf(dummyCredential))

        val actualCredential = splashViewModel.getCredential().getOrAwaitValue()

        Mockito.verify(authPref).getCredential()
        Assert.assertNotNull(actualCredential)
        Assert.assertEquals(
            expectedCredential.value,
            actualCredential
        )
    }
}