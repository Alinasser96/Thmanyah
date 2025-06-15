package com.hamalawey.data

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.hamalawey.data.dto.AudioArticleDto
import com.hamalawey.data.dto.AudiobookDto
import com.hamalawey.data.dto.ContentDto
import com.hamalawey.data.dto.EpisodeDto
import com.hamalawey.data.dto.PodcastDto
import com.hamalawey.data.dto.UnknownContentDto
import com.hamalawey.data.network.ContentTypeAdapter
import org.junit.Before
import org.junit.Test

class ContentTypeAdapterTest {

    private lateinit var gson: Gson

    @Before
    fun setup() {
        gson = GsonBuilder()
            .registerTypeAdapter(ContentDto::class.java, ContentTypeAdapter())
            .create()
    }

    @Test
    fun `ContentTypeAdapter deserializes valid Podcast JSON to PodcastDto`() {
        val json = """
            {
              "podcast_id": "123",
              "name": "NPR News Now",
              "description": "The latest news",
              "avatar_url": "url.png",
              "episode_count": 2,
              "duration": 600,
              "language": "en",
              "priority": 5,
              "popularityScore": 9,
              "score": 220.0845
            }
        """.trimIndent()

        val contentDto = gson.fromJson(json, ContentDto::class.java)

        assertThat(contentDto).isInstanceOf(PodcastDto::class.java)
        val podcastDto = contentDto as PodcastDto
        assertThat(podcastDto.podcastId).isEqualTo("123")
        assertThat(podcastDto.name).isEqualTo("NPR News Now")
        assertThat(podcastDto.language).isEqualTo("en")
        assertThat(podcastDto.duration).isEqualTo(600)
    }

    @Test
    fun `ContentTypeAdapter deserializes valid Episode JSON to EpisodeDto`() {
        val json = """
            {
              "podcastPopularityScore": 9,
              "podcastPriority": 5,
              "episode_id": "ep456",
              "name": "Live From Cleveland",
              "season_number": 1,
              "episode_type": "full",
              "podcast_name": "The NPR Politics Podcast",
              "author_name": "NPR",
              "description": "A special episode",
              "number": 10,
              "duration": 1846,
              "avatar_url": "ep_url.jpg",
              "release_date": "2018-02-24T16:31:00.000Z",
              "podcast_id": "pod789",
              "chapters": [],
              "paid_is_early_access": false,
              "paid_is_now_early_access": false,
              "paid_is_exclusive": false,
              "paid_transcript_url": null,
              "free_transcript_url": null,
              "paid_is_exclusive_partially": false,
              "paid_exclusive_start_time": 0,
              "paid_early_access_date": null,
              "paid_early_access_audio_url": null,
              "paid_exclusivity_type": null,
              "score": 216.7661
            }
        """.trimIndent()

        val contentDto = gson.fromJson(json, ContentDto::class.java)

        assertThat(contentDto).isInstanceOf(EpisodeDto::class.java)
        val episodeDto = contentDto as EpisodeDto
        assertThat(episodeDto.episodeId).isEqualTo("ep456")
        assertThat(episodeDto.name).isEqualTo("Live From Cleveland")
        assertThat(episodeDto.podcastId).isEqualTo("pod789") // Verify podcast_id is also parsed correctly
    }

    @Test
    fun `ContentTypeAdapter deserializes valid Audiobook JSON to AudiobookDto`() {
        val json = """
            {
              "audiobook_id": "book1",
              "name": "The Art of War",
              "author_name": "Sun Tzu",
              "description": "Ancient military text",
              "avatar_url": "book_url.jpg",
              "duration": 36000,
              "language": "en",
              "release_date": "2023-01-10T08:00:00Z",
              "score": 500
            }
        """.trimIndent()

        val contentDto = gson.fromJson(json, ContentDto::class.java)

        assertThat(contentDto).isInstanceOf(AudiobookDto::class.java)
        val audiobookDto = contentDto as AudiobookDto
        assertThat(audiobookDto.audiobookId).isEqualTo("book1")
        assertThat(audiobookDto.name).isEqualTo("The Art of War")
        assertThat(audiobookDto.authorName).isEqualTo("Sun Tzu")
    }

    @Test
    fun `ContentTypeAdapter deserializes valid AudioArticle JSON to AudioArticleDto`() {
        val json = """
            {
              "article_id": "art1",
              "name": "The Future of AI",
              "author_name": "Tech World",
              "description": "In-depth look",
              "avatar_url": "article_url.jpg",
              "duration": 1200,
              "release_date": "2023-05-10T10:00:00Z",
              "score": 300
            }
        """.trimIndent()

        val contentDto = gson.fromJson(json, ContentDto::class.java)

        assertThat(contentDto).isInstanceOf(AudioArticleDto::class.java)
        val audioArticleDto = contentDto as AudioArticleDto
        assertThat(audioArticleDto.articleId).isEqualTo("art1")
        assertThat(audioArticleDto.name).isEqualTo("The Future of AI")
    }

    @Test
    fun `ContentTypeAdapter deserializes unknown JSON to UnknownContentDto`() {
        val json = """
            {
              "unknown_id": "xyz",
              "name": "Mystery Item",
              "description": "This type is not recognized",
              "avatar_url": "mystery.jpg",
              "some_other_field": "value",
              "score": 0.0
            }
        """.trimIndent()

        val contentDto = gson.fromJson(json, ContentDto::class.java)

        assertThat(contentDto).isInstanceOf(UnknownContentDto::class.java)
        val unknownDto = contentDto as UnknownContentDto
        assertThat(unknownDto.name).isEqualTo("Mystery Item")
        assertThat(unknownDto.description).isEqualTo("This type is not recognized")

        // FIX: Normalize the expected JSON string to match Gson's compact output format.
        // 1. Parse the expected `json` string literal into a Gson JsonElement.
        // 2. Serialize that JsonElement back to a string using Gson. This will produce a compact string.
        val expectedNormalizedJson = gson.toJson(gson.fromJson(json, JsonElement::class.java))

        // Now compare the actual rawJsonData with the normalized expected JSON.
        assertThat(unknownDto.rawJsonData).isEqualTo(expectedNormalizedJson)
    }
}