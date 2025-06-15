package com.hamalawey.domain.model

data class Podcast(
    val id: String,
    override val name: String,
    override val description: String?,
    override val avatarUrl: String?,
    val episodeCount: Int,
    val duration: Long,
    val language: String?,
    val priority: String,
    val popularityScore: String,
    override val score: String
) : Content
