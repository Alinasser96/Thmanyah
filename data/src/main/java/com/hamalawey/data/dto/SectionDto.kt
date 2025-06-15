package com.hamalawey.data.dto

import com.google.gson.annotations.SerializedName

data class SectionDto(
    val name: String,
    val type: String,
    @SerializedName("content_type")
    val contentType: String,
    val order: String,
    val content: List<ContentDto> // Note: This will need custom deserialization logic
)
