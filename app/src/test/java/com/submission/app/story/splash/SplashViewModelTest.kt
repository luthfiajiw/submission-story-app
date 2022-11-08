package com.submission.app.story.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.submission.app.story.auth.models.LoginResult
import com.submission.app.story.utils.DataDummy
import com.submission.app.story.utils.getOrAwaitValue
import org.junit.Assert
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
    private val dummyCredential = DataDummy.generateDummyCredential()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `when Get Credential should not null and return The Credential`() {
        val expectedCredential = MutableLiveData<LoginResult>()
        expectedCredential.value = dummyCredential
        `when`(splashViewModel.getCredential()).thenReturn(expectedCredential)

        val actualCredential = splashViewModel.getCredential().getOrAwaitValue()

        Mockito.verify(splashViewModel).getCredential()
        Assert.assertNotNull(actualCredential)
    }
}