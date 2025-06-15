package com.hamalawey.domain.model

data class Audiobook(
    val id: String,
    override val name: String,
    override val description: String?,
    override val avatarUrl: String?,
    val authorName: String,
    val duration: Int,
    val language: String?,
    override val score: String
) : Content
