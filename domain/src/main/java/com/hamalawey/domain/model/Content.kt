package com.hamalawey.domain.model

sealed interface Content {
    val name: String
    val description: String?
    val avatarUrl: String?
    val score: String
}
