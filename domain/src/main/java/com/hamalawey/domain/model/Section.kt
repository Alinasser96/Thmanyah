package com.hamalawey.domain.model

data class Section(
    val name: String,
    val type: String,
    val contentType: String,
    val order: String,
    val content: List<Content>
)
