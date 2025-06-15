package com.hamalawey.domain.model

data class UnknownContent(
    override val name: String,
    override val description: String?,
    override val avatarUrl: String?,
    override val score: String,
    val rawData: String // To pass the raw JSON data to the UI if needed
) : Content
