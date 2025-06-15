package com.hamalawey.data.network

import com.google.gson.*
import com.hamalawey.data.dto.AudioArticleDto
import com.hamalawey.data.dto.AudiobookDto
import com.hamalawey.data.dto.ContentDto
import com.hamalawey.data.dto.EpisodeDto
import com.hamalawey.data.dto.PodcastDto
import com.hamalawey.data.dto.UnknownContentDto
import java.lang.reflect.Type

class ContentTypeAdapter : JsonDeserializer<ContentDto> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ContentDto? {
        if (json == null || context == null) return null

        val jsonObject = json.asJsonObject

        // CRITICAL: Reorder the 'when' statement to check for more specific IDs first.
        // An Episode *has* an episode_id. It also has a podcast_id.
        // A Podcast *only* has a podcast_id (not an episode_id).
        return when {
            // Check for Episode first, as it has a unique 'episode_id'
            jsonObject.has("episode_id") -> context.deserialize(jsonObject, EpisodeDto::class.java)

            // Check for Audiobook next
            jsonObject.has("audiobook_id") -> context.deserialize(jsonObject, AudiobookDto::class.java)

            // Check for AudioArticle next
            jsonObject.has("article_id") -> context.deserialize(jsonObject, AudioArticleDto::class.java)

            // Check for Podcast last, as its 'podcast_id' might be present in Episodes too
            // but if none of the more specific IDs are found, it must be a Podcast.
            jsonObject.has("podcast_id") -> context.deserialize(jsonObject, PodcastDto::class.java)

            else -> {
                // Safely extract common fields and store the raw JSON
                val name = jsonObject.get("name")?.asString ?: "Unknown Item"
                val description = jsonObject.get("description")?.asString
                val avatarUrl = jsonObject.get("avatar_url")?.asString
                val score = jsonObject.get("score")?.asString ?: ""

                // Store the entire raw JSON object as a string for debugging
                val rawJsonString = json.toString()

                UnknownContentDto(name, description, avatarUrl, score, rawJsonString)
            }
        }
    }
}