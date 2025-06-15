package com.hamalawey.data.dto

import com.google.gson.annotations.SerializedName

data class EpisodeDto(
    @SerializedName("podcastPopularityScore")
    val podcastPopularityScore: Int,
    @SerializedName("podcastPriority")
    val podcastPriority: Int,
    @SerializedName("episode_id")
    val episodeId: String,
    override val name: String,
    @SerializedName("season_number")
    val seasonNumber: Int?,
    @SerializedName("episode_type")
    val episodeType: String,
    @SerializedName("podcast_name")
    val podcastName: String,
    @SerializedName("author_name")
    val authorName: String,
    override val description: String?,
    val number: Int?,
    val duration: Int,
    @SerializedName("avatar_url")
    override val avatarUrl: String?,
    @SerializedName("separated_audio_url")
    val separatedAudioUrl: String?,
    @SerializedName("audio_url")
    val audioUrl: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("podcast_id")
    val podcastId: String,
    val chapters: List<Any>, // Adjust if chapters have a specific structure
    @SerializedName("paid_is_early_access")
    val paidIsEarlyAccess: Boolean,
    @SerializedName("paid_is_now_early_access")
    val paidIsNowEarly_access: Boolean,
    @SerializedName("paid_is_exclusive")
    val paidIsExclusive: Boolean,
    @SerializedName("paid_transcript_url")
    val paidTranscriptUrl: String?,
    @SerializedName("free_transcript_url")
    val freeTranscriptUrl: String?,
    @SerializedName("paid_is_exclusive_partially")
    val paidIsExclusivePartially: Boolean,
    @SerializedName("paid_exclusive_start_time")
    val paidExclusiveStartTime: Int,
    @SerializedName("paid_early_access_date")
    val paidEarlyAccessDate: String?,
    @SerializedName("paid_early_access_audio_url")
    val paidEarlyAccessAudioUrl: String?,
    @SerializedName("paid_exclusivity_type")
    val paidExclusivityType: String?,
    override val score: String
) : ContentDto
