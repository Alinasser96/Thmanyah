package com.hamalawey.data.dto

import com.google.gson.annotations.SerializedName

data class AudiobookDto(
    @SerializedName("audiobook_id")
    val audiobookId: String,
    override val name: String,
    @SerializedName("author_name")
    val authorName: String,
    override val description: String?,
    @SerializedName("avatar_url")
    override val avatarUrl: String?,
    val duration: Int,
    val language: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    override val score: String
) : ContentDto
