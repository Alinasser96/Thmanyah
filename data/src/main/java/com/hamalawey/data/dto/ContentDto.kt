package com.hamalawey.data.dto

sealed interface ContentDto {
    val name: String
    val description: String?
    val avatarUrl: String?
    val score: String
}