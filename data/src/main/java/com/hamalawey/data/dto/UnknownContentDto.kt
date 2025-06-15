package com.hamalawey.data.dto

import com.google.gson.annotations.SerializedName

data class UnknownContentDto(
    // Include common fields that all content items might have
    override val name: String,
    override val description: String?,
    @SerializedName("avatar_url")
    override val avatarUrl: String?,
    override val score: String,
    // Add a field to store the raw JSON for debugging or detailed display
    val rawJsonData: String
) : ContentDto
