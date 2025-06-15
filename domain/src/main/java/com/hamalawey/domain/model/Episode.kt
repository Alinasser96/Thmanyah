package com.hamalawey.domain.model

data class Episode(
    val id: String,
    override val name: String,
    override val description: String?,
    override val avatarUrl: String?,
    val podcastName: String,
    val authorName: String,
    val duration: Int,
    override val score: String
) : Content
