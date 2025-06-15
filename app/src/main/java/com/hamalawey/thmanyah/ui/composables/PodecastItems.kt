package com.hamalawey.thmanyah.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hamalawey.domain.model.Podcast

@Composable
fun QueuePodcastItem(podcast: Podcast, modifier: Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = podcast.avatarUrl,
                contentDescription = podcast.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(96.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = podcast.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${podcast.episodeCount} episodes",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp).padding(8.dp)
            )
        }
    }
}

@Composable
fun SquarePodcastItem(podcast: Podcast, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = podcast.avatarUrl,
            contentDescription = podcast.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(130.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Text(
            text = podcast.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )

        Text(
            text = "${podcast.episodeCount} episodes",
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp)
        )
    }
}

@Composable
fun BigSquarePodcastItem(podcast: Podcast, modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(bottom = 8.dp)
            .aspectRatio(1f)
    ) {
        AsyncImage(
            model = podcast.avatarUrl,
            contentDescription = podcast.name,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent, Color.Black.copy(alpha = 0.7f)
                        ), startY = 0.6f
                    )
                )
                .padding(16.dp), contentAlignment = Alignment.BottomStart
        ) {
            Column {
                Text(
                    text = podcast.name,
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = "${podcast.episodeCount} episodes",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    shape = CircleShape,
                    colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
                        containerColor = Color.Black.copy(alpha = 0.5f)
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewQueued() {
    val podcast = Podcast(
        id = "",
        name = "Audio Book",
        description = "description",
        avatarUrl = "https://media.npr.org/assets/img/2024/01/11/podcast-politics_2023_update1_sq-be7ef464dd058fe663d9e4cfe836fb9309ad0a4d.jpg?s=1400&c=66&f=jpg",
        duration = 20,
        episodeCount = 20,
        language = "",
        priority = "",
        popularityScore = "",
        score = ""
    )
    QueuePodcastItem(podcast, Modifier)
}

@Preview
@Composable
private fun PreviewSquare() {
    val podcast = Podcast(
        id = "",
        name = "Audio Book",
        description = "description",
        avatarUrl = "https://media.npr.org/assets/img/2024/01/11/podcast-politics_2023_update1_sq-be7ef464dd058fe663d9e4cfe836fb9309ad0a4d.jpg?s=1400&c=66&f=jpg",
        duration = 20,
        episodeCount = 20,
        language = "",
        priority = "",
        popularityScore = "",
        score = ""
    )
    SquarePodcastItem(podcast, Modifier)
}

@Preview
@Composable
private fun PreviewBigSquare() {
    val podcast = Podcast(
        id = "",
        name = "Audio Book",
        description = "description",
        avatarUrl = "https://media.npr.org/assets/img/2024/01/11/podcast-politics_2023_update1_sq-be7ef464dd058fe663d9e4cfe836fb9309ad0a4d.jpg?s=1400&c=66&f=jpg",
        duration = 20,
        episodeCount = 20,
        language = "",
        priority = "",
        popularityScore = "",
        score = ""
    )
    BigSquarePodcastItem(podcast, Modifier)
}
