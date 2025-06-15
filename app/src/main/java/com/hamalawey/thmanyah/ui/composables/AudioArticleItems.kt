package com.hamalawey.thmanyah.ui.composables

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hamalawey.domain.model.AudioArticle

@Composable
fun QueueAudioArticleItem(audioArticle: AudioArticle, modifier: Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.onSecondary
                )
                .padding(8.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = audioArticle.avatarUrl,
                contentDescription = audioArticle.name,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = audioArticle.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = audioArticle.authorName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun SquareAudioArticleItem(audioArticle: AudioArticle, modifier: Modifier) {
    Column(
        modifier = modifier
            .padding(bottom = 8.dp)
    ) {
        AsyncImage(
            model = audioArticle.avatarUrl,
            contentDescription = audioArticle.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
        )

        Text(
            text = audioArticle.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )


        Text(
            text = audioArticle.authorName,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp)
        )
    }
}

@Composable
fun BigSquareAudioArticleItem(audioArticle: AudioArticle, modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(bottom = 8.dp)
            .aspectRatio(1f)
    ) {
        AsyncImage(
            model = audioArticle.avatarUrl,
            contentDescription = audioArticle.name,
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
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        ),
                        startY = 0.6f
                    )
                )
                .padding(16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column {
                Text(
                    text = audioArticle.name,
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = audioArticle.authorName,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.9f)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewQueued() {
    val audioArticle = AudioArticle(
        id = "",
        name = "Audio Book",
        description = "description",
        avatarUrl = "https://media.npr.org/assets/img/2024/01/11/podcast-politics_2023_update1_sq-be7ef464dd058fe663d9e4cfe836fb9309ad0a4d.jpg?s=1400&c=66&f=jpg",
        authorName = "Aly",
        duration = 20,
        score = "1"
    )
    QueueAudioArticleItem(audioArticle, Modifier)
}

@Preview
@Composable
private fun PreviewSquare() {
    val audioArticle = AudioArticle(
        id = "",
        name = "Audio Book",
        description = "description",
        avatarUrl = "https://media.npr.org/assets/img/2024/01/11/podcast-politics_2023_update1_sq-be7ef464dd058fe663d9e4cfe836fb9309ad0a4d.jpg?s=1400&c=66&f=jpg",
        authorName = "Aly",
        duration = 20,
        score = "1"
    )
    SquareAudioArticleItem(audioArticle, Modifier)
}

@Preview
@Composable
private fun PreviewBigSquare() {
    val audioArticle = AudioArticle(
        id = "",
        name = "Audio Book",
        description = "description",
        avatarUrl = "https://media.npr.org/assets/img/2024/01/11/podcast-politics_2023_update1_sq-be7ef464dd058fe663d9e4cfe836fb9309ad0a4d.jpg?s=1400&c=66&f=jpg",
        authorName = "Aly",
        duration = 20,
        score = "1"
    )
    BigSquareAudioArticleItem(audioArticle, Modifier)
}
