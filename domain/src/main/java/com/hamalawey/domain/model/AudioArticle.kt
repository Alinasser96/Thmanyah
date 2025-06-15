package com.hamalawey.domain.model

data class AudioArticle(
    val id: String,
    override val name: String,
    override val description: String?,
    override val avatarUrl: String?,
    val authorName: String,
    val duration: Int,
    override val score: String
) : Content
