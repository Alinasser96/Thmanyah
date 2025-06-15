package com.hamalawey.data.dto

import com.google.gson.annotations.SerializedName

data class PodcastDto(
    @SerializedName("podcast_id")
    val podcastId: String,
    override val name: String,
    override val description: String?,
    @SerializedName("avatar_url")
    override val avatarUrl: String?,
    @SerializedName("episode_count")
    val episodeCount: Int,
    val duration: Long,
    val language: String?,
    val priority: String,
    @SerializedName("popularityScore")
    val popularityScore: String,
    override val score: String
) : ContentDto
