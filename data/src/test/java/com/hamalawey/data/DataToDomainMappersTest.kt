package com.hamalawey.data

import com.google.common.truth.Truth.assertThat
import com.hamalawey.data.dto.ApiResponseDto
import com.hamalawey.data.dto.AudioArticleDto
import com.hamalawey.data.dto.AudiobookDto
import com.hamalawey.data.dto.EpisodeDto
import com.hamalawey.data.dto.PaginationDto
import com.hamalawey.data.dto.PodcastDto
import com.hamalawey.data.dto.SectionDto
import com.hamalawey.data.dto.UnknownContentDto
import com.hamalawey.data.mapper.toDomain
import com.hamalawey.domain.model.AudioArticle
import com.hamalawey.domain.model.Audiobook
import com.hamalawey.domain.model.Episode
import com.hamalawey.domain.model.Podcast
import com.hamalawey.domain.model.UnknownContent
import org.junit.Test

class DataToDomainMappersTest {

    //region DTO Test Data (Adjust package names if different)
    private val podcastDto = PodcastDto(
        podcastId = "p1", name = "Test Podcast", description = "A test", avatarUrl = "url.jpg",
        episodeCount = 5, duration = 300000, language = "en", priority = "1", popularityScore = "7", score = "250.0"
    )
    private val podcastDtoNullLanguage = podcastDto.copy(language = null)

    private val episodeDto = EpisodeDto(
        podcastPopularityScore = 8, podcastPriority = 4, episodeId = "e1",
        name = "Test Episode", seasonNumber = 1, episodeType = "full", podcastName = "Parent Pod",
        authorName = "Author", description = "Ep desc", number = 10, duration = 1500,
        avatarUrl = "epurl.jpg", separatedAudioUrl = null, audioUrl = null, releaseDate = "2024-01-01",
        podcastId = "p1", chapters = emptyList(), paidIsExclusive = false,
        paidIsEarlyAccess = false, paidIsNowEarly_access = false, paidTranscriptUrl = null,
        freeTranscriptUrl = null, paidIsExclusivePartially = false, paidExclusiveStartTime = 0,
        paidEarlyAccessDate = null, paidEarlyAccessAudioUrl = null, paidExclusivityType = null, score = "120.0"
    )
    private val audiobookDto = AudiobookDto(
        audiobookId = "a1", name = "Test Audiobook", authorName = "Book Author", description = "Book Desc",
        avatarUrl = "bookurl.jpg", duration = 3600000, language = "en", score = "500.0", releaseDate = ""
    )
    private val audioArticleDto = AudioArticleDto(
        articleId = "ar1", name = "Test Article", authorName = "Art Author", description = "Art Desc",
        avatarUrl = "arturl.jpg", duration = 600000, score = "300.0", releaseDate = ""
    )
    private val unknownContentDto = UnknownContentDto(
        name = "Unknown Item", description = "Some desc", avatarUrl = "unknown.jpg", score = "0.0",
        rawJsonData = "{ \"some_new_field\": 123 }"
    )
    private val sectionDto = SectionDto(
        name = "Mixed Content", type = "queue", contentType = "unknown", order = "1",
        content = listOf(podcastDto, episodeDto, audiobookDto, audioArticleDto, unknownContentDto)
    )
    private val apiResponseDto = ApiResponseDto(
        sections = listOf(sectionDto), pagination = PaginationDto(null, 1)
    )
    //endregion

    @Test
    fun `PodcastDto toDomain maps all fields correctly`() {
        val domain = podcastDto.toDomain()
        assertThat(domain.id).isEqualTo("p1")
        assertThat(domain.name).isEqualTo("Test Podcast")
        assertThat(domain.language).isEqualTo("en")
        assertThat(domain.episodeCount).isEqualTo(5)
        assertThat(domain.duration).isEqualTo(300000L)
    }

    @Test
    fun `PodcastDto toDomain handles null language gracefully`() {
        val domain = podcastDtoNullLanguage.toDomain()
        assertThat(domain.language).isEqualTo("Unknown")
    }

    @Test
    fun `EpisodeDto toDomain maps all fields correctly`() {
        val domain = episodeDto.toDomain()
        assertThat(domain.id).isEqualTo("e1")
        assertThat(domain.name).isEqualTo("Test Episode")
        assertThat(domain.podcastName).isEqualTo("Parent Pod")
        assertThat(domain.authorName).isEqualTo("Author")
        assertThat(domain.duration).isEqualTo(1500)
    }

    @Test
    fun `AudiobookDto toDomain maps all fields correctly`() {
        val domain = audiobookDto.toDomain()
        assertThat(domain.id).isEqualTo("a1")
        assertThat(domain.name).isEqualTo("Test Audiobook")
        assertThat(domain.authorName).isEqualTo("Book Author")
    }

    @Test
    fun `AudioArticleDto toDomain maps all fields correctly`() {
        val domain = audioArticleDto.toDomain()
        assertThat(domain.id).isEqualTo("ar1")
        assertThat(domain.name).isEqualTo("Test Article")
        assertThat(domain.authorName).isEqualTo("Art Author")
    }

    @Test
    fun `UnknownContentDto toDomain maps all fields correctly`() {
        val domain = unknownContentDto.toDomain()
        assertThat(domain.name).isEqualTo("Unknown Item")
        assertThat(domain.rawData).isEqualTo("{ \"some_new_field\": 123 }")
    }

    @Test
    fun `ContentDto toDomain dispatches to correct specific domain type`() {
        assertThat(podcastDto.toDomain()).isInstanceOf(Podcast::class.java)
        assertThat(episodeDto.toDomain()).isInstanceOf(Episode::class.java)
        assertThat(audiobookDto.toDomain()).isInstanceOf(Audiobook::class.java)
        assertThat(audioArticleDto.toDomain()).isInstanceOf(AudioArticle::class.java)
        assertThat(unknownContentDto.toDomain()).isInstanceOf(UnknownContent::class.java)
    }

    @Test
    fun `SectionDto toDomain maps sections and their content correctly`() {
        val domain = sectionDto.toDomain()
        assertThat(domain.name).isEqualTo("Mixed Content")
        assertThat(domain.contentType).isEqualTo("unknown")
        assertThat(domain.content).hasSize(5)
        assertThat(domain.content[0]).isInstanceOf(Podcast::class.java)
        assertThat(domain.content[1]).isInstanceOf(Episode::class.java)
        assertThat(domain.content[2]).isInstanceOf(Audiobook::class.java)
        assertThat(domain.content[3]).isInstanceOf(AudioArticle::class.java)
        assertThat(domain.content[4]).isInstanceOf(UnknownContent::class.java)
    }

    @Test
    fun `ApiResponseDto toDomain maps sections correctly`() {
        val domainSections = apiResponseDto.toDomain()
        assertThat(domainSections).hasSize(1)
        assertThat(domainSections[0].name).isEqualTo("Mixed Content")
    }
}