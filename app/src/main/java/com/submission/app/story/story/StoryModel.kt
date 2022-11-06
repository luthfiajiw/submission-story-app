package com.submission.app.story.story

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoryResponse(
    @field:SerializedName("error")
    val error: Boolean = false,
    @field:SerializedName("message")
    val message: String = "",
    @field:SerializedName("listStory")
    val listStory: List<Story> = listOf<Story>()
)

@Parcelize
data class Story(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("description")
    val description: String,
    @field:SerializedName("photoUrl")
    val photoUrl: String,
    @field:SerializedName("lat")
    val lat: Double,
    @field:SerializedName("lon")
    val lon: Double,
    @field:SerializedName("createdAt")
    val createdAt: String
) : Parcelable