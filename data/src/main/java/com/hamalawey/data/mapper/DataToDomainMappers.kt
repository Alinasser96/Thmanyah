package com.hamalawey.data.mapper

import com.hamalawey.data.dto.ApiResponseDto
import com.hamalawey.data.dto.AudioArticleDto
import com.hamalawey.data.dto.AudiobookDto
import com.hamalawey.data.dto.ContentDto
import com.hamalawey.data.dto.EpisodeDto
import com.hamalawey.data.dto.PodcastDto
import com.hamalawey.data.dto.SectionDto
import com.hamalawey.data.dto.UnknownContentDto
import com.hamalawey.domain.model.AudioArticle
import com.hamalawey.domain.model.Audiobook
import com.hamalawey.domain.model.Content
import com.hamalawey.domain.model.Episode
import com.hamalawey.domain.model.Podcast
import com.hamalawey.domain.model.Section
import com.hamalawey.domain.model.UnknownContent


fun ApiResponseDto.toDomain(): List<Section> {
    return sections.map { it.toDomain() }
}

fun SectionDto.toDomain(): Section {
    return Section(
        name = name,
        type = type,
        contentType = contentType,
        order = order,
        content = content.map { it.toDomain() } // Call the updated toDomain()
    )
}


fun ContentDto.toDomain(): Content { // Removed parentContentType parameter
    return when (this) { // Use 'this' to pattern match the actual DTO type
        is PodcastDto -> this.toDomain()
        is EpisodeDto -> this.toDomain()
        is AudiobookDto -> this.toDomain()
        is AudioArticleDto -> this.toDomain()
        is UnknownContentDto -> this.toDomain() // Handle the new UnknownContentDto
    }
}

fun PodcastDto.toDomain(): Podcast {
    return Podcast(
        id = podcastId,
        name = name,
        description = description,
        avatarUrl = avatarUrl,
        episodeCount = episodeCount,
        duration = duration,
        language = language ?:"Unknown",
        priority = priority,
        popularityScore = popularityScore,
        score = score
    )
}

fun EpisodeDto.toDomain(): Episode {
    return Episode(
        id = episodeId,
        name = name,
        description = description,
        avatarUrl = avatarUrl,
        podcastName = podcastName,
        authorName = authorName,
        duration = duration,
        score = score
    )
}

fun AudiobookDto.toDomain(): Audiobook {
    return Audiobook(
        id = audiobookId,
        name = name,
        description = description,
        avatarUrl = avatarUrl,
        authorName = authorName,
        duration = duration,
        language = language ?: "unknown",
        score = score
    )
}

fun AudioArticleDto.toDomain(): AudioArticle {
    return AudioArticle(
        id = articleId,
        name = name,
        description = description,
        avatarUrl = avatarUrl,
        authorName = authorName,
        duration = duration,
        score = score
    )
}

fun UnknownContentDto.toDomain(): UnknownContent {
    return UnknownContent(
        name = name,
        description = description,
        avatarUrl = avatarUrl,
        score = score,
        rawData = rawJsonData
    )
}