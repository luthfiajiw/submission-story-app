package com.submission.app.story.story

data class StoryResponse(
    val error: Boolean = false,
    val message: String = "",
    val listStory: List<Story> = listOf<Story>()
)

data class Story(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String
)