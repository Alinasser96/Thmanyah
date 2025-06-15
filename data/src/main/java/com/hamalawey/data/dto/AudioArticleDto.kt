package com.hamalawey.data.dto

import com.google.gson.annotations.SerializedName

data class AudioArticleDto(
    @SerializedName("article_id")
    val articleId: String,
    override val name: String,
    @SerializedName("author_name")
    val authorName: String,
    override val description: String?,
    @SerializedName("avatar_url")
    override val avatarUrl: String?,
    val duration: Int,
    @SerializedName("release_date")
    val releaseDate: String,
    override val score: String
) : ContentDto
