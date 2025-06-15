package com.hamalawey.data

import com.google.common.truth.Truth.assertThat
import com.hamalawey.data.dto.ApiResponseDto
import com.hamalawey.data.dto.EpisodeDto
import com.hamalawey.data.dto.PaginationDto
import com.hamalawey.data.dto.PodcastDto
import com.hamalawey.data.dto.SectionDto
import com.hamalawey.data.network.ApiService
import com.hamalawey.data.repository.AppRepositoryImpl
import com.hamalawey.domain.model.Episode
import com.hamalawey.domain.model.Podcast
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AppRepositoryImplTest {

    private lateinit var repository: AppRepositoryImpl
    private val mockApiService: ApiService = mockk()

    //region Test Data (Adjust package names if different, ensure types match DTOs)
    private val mockPodcastDto = PodcastDto(
        podcastId = "1", name = "Pod 1", description = "Desc", avatarUrl = "url",
        episodeCount = 10, duration = 60000, language = "en", priority = "1", popularityScore = "5", score = "100.0"
    )
    private val mockEpisodeDto = EpisodeDto(
        podcastPopularityScore = 9, podcastPriority = 5, episodeId = "e1",
        name = "Ep 1", seasonNumber = 1, episodeType = "full", podcastName = "Pod 1",
        authorName = "Auth", description = "Ep Desc", number = 1, duration = 1200,
        avatarUrl = "epurl", separatedAudioUrl = null, audioUrl = null, releaseDate = "2024-01-01",
        podcastId = "1", chapters = emptyList(), paidIsExclusive = false,
        paidIsEarlyAccess = false, paidIsNowEarly_access = false, paidTranscriptUrl = null,
        freeTranscriptUrl = null, paidIsExclusivePartially = false, paidExclusiveStartTime = 0,
        paidEarlyAccessDate = null, paidEarlyAccessAudioUrl = null, paidExclusivityType = null, score = "50.0"
    )
    private val mockSectionDto = SectionDto(
        name = "Test Section", type = "grid", contentType = "podcast", order = "1",
        content = listOf(mockPodcastDto)
    )
    private val mockApiResponseDto = ApiResponseDto(
        sections = listOf(mockSectionDto),
        pagination = PaginationDto(nextPage = null, totalPages = 1)
    )
    //endregion

    @Before
    fun setup() {
        repository = AppRepositoryImpl(mockApiService)
    }

    @Test
    fun `getHomeSections maps success response to domain models`() = runTest {
        coEvery { mockApiService.getHomeSections() } returns mockApiResponseDto

        val result = repository.getHomeSections()

        assertThat(result.isSuccess).isTrue()
        val sections = result.getOrThrow()
        assertThat(sections).hasSize(1)
        assertThat(sections[0].name).isEqualTo(mockSectionDto.name)
        assertThat(sections[0].content).hasSize(1)
        assertThat(sections[0].content[0]).isInstanceOf(Podcast::class.java)
        assertThat((sections[0].content[0] as Podcast).name).isEqualTo(mockPodcastDto.name)

        coVerify(exactly = 1) { mockApiService.getHomeSections() }
    }

    @Test
    fun `getHomeSections returns failure on API error`() = runTest {
        val exception = RuntimeException("Network error")
        coEvery { mockApiService.getHomeSections() } throws exception

        val result = repository.getHomeSections()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)

        coVerify(exactly = 1) { mockApiService.getHomeSections() }
    }

    @Test
    fun `searchContent maps success response to domain models`() = runTest {
        val searchQuery = "NPR"
        val searchType = "episode"
        val expectedApiResponse = mockApiResponseDto.copy(
            sections = listOf(
                mockSectionDto.copy(
                    name = "Search results", contentType = searchType, content = listOf(mockEpisodeDto)
                )
            )
        )
        coEvery { mockApiService.searchContent(searchQuery, searchType, null, null) } returns expectedApiResponse

        val result = repository.searchContent(searchQuery, searchType)

        assertThat(result.isSuccess).isTrue()
        val sections = result.getOrThrow()
        assertThat(sections).hasSize(1)
        assertThat(sections[0].content).hasSize(1)
        assertThat(sections[0].content[0]).isInstanceOf(Episode::class.java)
        assertThat((sections[0].content[0] as Episode).name).isEqualTo(mockEpisodeDto.name)

        coVerify(exactly = 1) { mockApiService.searchContent(eq(searchQuery), eq(searchType), isNull(), isNull()) }
    }

    @Test
    fun `searchContent returns failure on API error`() = runTest {
        val exception = RuntimeException("Search failed")
        coEvery { mockApiService.searchContent(any(), any(), any(), any()) } throws exception

        val result = repository.searchContent("query", null)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)

        coVerify(exactly = 1) { mockApiService.searchContent(eq("query"), isNull(), isNull(), isNull()) }
    }
}