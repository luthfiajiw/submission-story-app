package com.submission.app.story.story.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.submission.app.story.MainDispatcherRule
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.auth.models.LoginResult
import com.submission.app.story.shared.models.GenericResponse
import com.submission.app.story.shared.utils.Result
import com.submission.app.story.story.StoryRepository
import com.submission.app.story.story.StoryResponse
import com.submission.app.story.utils.DataDummy
import com.submission.app.story.utils.getOrAwaitValue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var storyViewModel: StoryViewModel

    @Mock
    private lateinit var authPref: AuthPref
    @Mock
    private lateinit var storyRepository: StoryRepository
    @Mock
    private lateinit var file: File
    private val token = "token"

    @Before
    fun setUp() {
        storyViewModel = StoryViewModel(storyRepository, authPref)
    }

    @Test
    fun `when Get Stories with location should return list of story`() {
        val expectedResponse = MutableLiveData<Result<StoryResponse>>()
        expectedResponse.value = Result.Success(DataDummy.generateDummyStoryResponse())
        `when`(storyRepository.getStoriesWithLocation(token)).thenReturn(expectedResponse)

        val actualResponse = storyViewModel.getStoriesWithLocation(token).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoriesWithLocation(token)
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(
            (expectedResponse.value as Result.Success).data.listStory,
            (actualResponse as Result.Success).data.listStory
        )
    }

    @Test
    fun `when Upload Story should not error`() {
        val expectedResponse = MutableLiveData<Result<GenericResponse>>()
        expectedResponse.value = Result.Success(DataDummy.dummySuccessResponse())
        `when`(storyRepository.uploadImage(token, file, "desc", 0.0, 0.0)).thenReturn(
            expectedResponse
        )

        val actualResponse = storyViewModel.uploadImage(token, file, "desc", 0.0, 0.0).getOrAwaitValue()

        verify(storyRepository).uploadImage(token, file, "desc", 0.0, 0.0)
        Assert.assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Get Credential should not null and return The Credential`() {
        val dummyCredential = DataDummy.generateDummyCredential()

        val expectedCredential = MutableLiveData<LoginResult>()
        expectedCredential.value = dummyCredential
        `when`(authPref.getCredential()).thenReturn(flowOf(dummyCredential))

        val actualCredential = storyViewModel.getCredential().getOrAwaitValue()

        verify(authPref).getCredential()
        Assert.assertNotNull(actualCredential)
        Assert.assertEquals(
            expectedCredential.value,
            actualCredential
        )
    }

    @Test
    fun `verify Sign out is working`() = runTest {
        storyViewModel.signOut()
        verify(authPref, times(1)).deleteCredential()
    }
}