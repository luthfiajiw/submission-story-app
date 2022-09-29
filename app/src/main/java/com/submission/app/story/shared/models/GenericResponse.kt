package com.submission.app.story.shared.models

import com.google.gson.annotations.SerializedName

data class GenericResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,
)
