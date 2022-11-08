package com.submission.app.story.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.submission.app.story.MainDispatcherRule
import com.submission.app.story.shared.models.GenericResponse
import com.submission.app.story.shared.utils.Result
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
import java.io.File
import kotlin.math.exp

@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    @Mock
    private lateinit var file: File
    private val token = "token"

    @Test
    fun `when Get Stories with location should not error`() {
        val expectedResponse = MutableLiveData<Result<StoryResponse>>()
        expectedResponse.value = Result.Success(DataDummy.generateDummyStoryResponse())
        `when`(storyRepository.getStoriesWithLocation(token)).thenReturn(expectedResponse)

        val actualResponse = storyRepository.getStoriesWithLocation(token).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoriesWithLocation(token)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Upload Story should not error`() {
        val expectedResponse = MutableLiveData<Result<GenericResponse>>()
        expectedResponse.value = Result.Success(DataDummy.dummySuccessResponse())
        `when`(storyRepository.uploadImage(token, file, "desc", 0.0, 0.0)).thenReturn(
            expectedResponse
        )

        val actualResponse = storyRepository.uploadImage(token, file, "desc", 0.0, 0.0).getOrAwaitValue()

        Mockito.verify(storyRepository).uploadImage(token, file, "desc", 0.0, 0.0)
        Assert.assertTrue(actualResponse is Result.Success)
    }
}